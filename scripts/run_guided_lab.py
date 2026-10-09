"""Run a domain's synthetic lab and retain behavioral evidence for the workshop."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import re
import subprocess
import sys
import yaml
from check_lab_outputs import check

ROOT = Path(__file__).resolve().parents[1]
LAB = Path('/tmp/automation-governance-lab')
CHANGES = {
    'virtualizacion': ('cpu', 4),
    'network': ('vlan_id', 121),
    'cloud': ('flavor', 'medium'),
    'plataformas': ('replicas', 3),
    'seguridad': ('profile', 'lab-baseline-v2'),
    'infraestructura': ('monitoring_profile', 'extended'),
    'storage': ('size_gib', 30),
}


def snapshot(domain):
    root = LAB / domain
    return {str(p): hashlib.sha256(p.read_bytes()).hexdigest()
            for p in root.rglob('*') if p.is_file()} if root.exists() else {}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--domain', required=True, choices=CHANGES)
    parser.add_argument('--request-id', required=True)
    parser.add_argument('--resource-name', required=True)
    args = parser.parse_args()
    if not re.fullmatch(r'[a-z0-9][a-z0-9-]{0,62}', args.resource_name):
        parser.error('resource-name must match the lab naming policy')
    if not re.fullmatch(r'[a-zA-Z0-9][a-zA-Z0-9_-]{0,63}', args.request_id):
        parser.error('request-id must match the lab identifier policy')
    evidence = ROOT / 'teams' / args.domain / args.request_id / args.resource_name / 'evidence'
    evidence.mkdir(parents=True, exist_ok=True)
    env = dict(os.environ, ANSIBLE_NOCOLOR='1', ANSIBLE_FORCE_COLOR='0')
    command = ['ansible-playbook', '-i', 'inventories/lab/hosts.yml', f'playbooks/{args.domain}.yml']
    records = []

    def run(label, extra=None, flags=None, negative=False, unchanged=False):
        variables = {'resource_name': args.resource_name, 'request_id': args.request_id}
        variables.update(extra or {})
        inputs = evidence / f'{label}-inputs.json'
        inputs.write_text(json.dumps(variables, indent=2) + '\n')
        proc = subprocess.run(command + ['-e', '@' + str(inputs)] + (flags or []),
                              cwd=ROOT, env=env, text=True, stdout=subprocess.PIPE,
                              stderr=subprocess.STDOUT, check=False)
        (evidence / f'{label}.log').write_text(proc.stdout)
        recaps = re.findall(r'changed=(\d+)', proc.stdout)
        if negative:
            assert proc.returncode != 0, 'Invalid input unexpectedly succeeded'
            assert 'Invalid workshop inputs or execution boundary' in proc.stdout, 'Failure was not input validation'
        else:
            assert proc.returncode == 0, f'{label} failed; read {evidence / (label + ".log")}'
        if unchanged:
            assert recaps and all(int(n) == 0 for n in recaps), f'{label}: expected changed=0'
        records.append({'step': label, 'exit_code': proc.returncode, 'changed': recaps,
                        'expected_failure': negative, 'passed': True})
        print(f'{label}: PASS')

    original = yaml.safe_load((ROOT / 'examples' / f'{args.domain}.yml').read_text())
    expected = original['governed_resource_desired_state']
    output_path = LAB / args.domain / 'outputs' / f'{args.request_id}-{args.resource_name}.json'
    resource_path = LAB / args.domain / 'resources' / f'{args.resource_name}.json'

    def verify(label, desired):
        output = json.loads(output_path.read_text())
        check(output)
        assert output['domain'] == args.domain and output['request_id'] == args.request_id
        assert output['resource_name'] == args.resource_name
        assert output['use_case_id'] == original['governed_resource_use_case_id']
        assert output['resource'] == desired == json.loads(resource_path.read_text())
        assert output['evidence']['resource_path'] == str(resource_path)
        (evidence / f'{label}-output.json').write_text(json.dumps(output, indent=2) + '\n')

    run('01-syntax', flags=['--syntax-check'])
    run('02-baseline')
    verify('02-baseline', expected)
    before = snapshot(args.domain)
    run('03-repeat', unchanged=True)
    assert snapshot(args.domain) == before, 'Repeat modified lab files'
    run('04-invalid', {'resource_name': 'INVALID_NAME'}, negative=True)
    assert snapshot(args.domain) == before, 'Invalid input modified lab files'
    revised = json.loads(json.dumps(expected))
    field, value = CHANGES[args.domain]
    revised[field] = value
    assert revised != expected, 'Change exercise is identical to baseline'
    run('05-change', {'governed_resource_desired_state': revised})
    verify('05-change', revised)
    before = snapshot(args.domain)
    run('06-repeat-change', {'governed_resource_desired_state': revised}, unchanged=True)
    assert snapshot(args.domain) == before, 'Repeated controlled change modified lab files'
    revision = subprocess.run(['git', 'rev-parse', 'HEAD'], cwd=ROOT, text=True,
                              stdout=subprocess.PIPE, stderr=subprocess.DEVNULL, check=False)
    report = {'domain': args.domain, 'request_id': args.request_id,
              'resource_name': args.resource_name, 'simulation': True,
              'commit': revision.stdout.strip() if revision.returncode == 0 else 'unversioned-copy',
              'change': {'field': field, 'before': expected[field], 'after': value},
              'steps': records, 'status': 'passed'}
    (evidence / 'summary.json').write_text(json.dumps(report, indent=2) + '\n')
    print(f'Evidence: {evidence.relative_to(ROOT)}')
    print('Synthetic laboratory only. Provider integration and AAP are separate gates.')


if __name__ == '__main__':
    try:
        main()
    except (AssertionError, OSError, subprocess.SubprocessError) as error:
        print(f'FAIL: {error}', file=sys.stderr)
        sys.exit(1)
