"""Validate lab output and read-back state; --all requires every domain."""
import argparse
import json
from pathlib import Path

DOMAINS = {'virtualizacion', 'network', 'cloud', 'plataformas', 'seguridad', 'infraestructura', 'storage'}
REQUIRED = {'schema_version', 'request_id', 'use_case_id', 'domain', 'environment', 'resource_name',
            'status', 'simulation', 'resource', 'evidence'}


def check(data):
    assert REQUIRED <= data.keys(), 'Output missing required fields'
    assert data['schema_version'] == '1.0'
    assert data['domain'] in DOMAINS
    assert data['simulation'] is True and data['environment'] == 'lab'
    assert data['status'] == 'verified'
    tags = data['resource']['tags']
    assert {'owner', 'domain', 'environment', 'cost_center', 'use_case_id', 'data_classification'} <= tags.keys()
    assert tags['domain'] == data['domain'] and tags['environment'] == data['environment']
    assert tags['use_case_id'] == data['use_case_id']
    assert tags['data_classification'] == 'synthetic'


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--all', action='store_true')
    args = parser.parse_args()
    root = Path('/tmp/automation-governance-lab')
    paths = list(root.glob('*/outputs/*.json'))
    assert paths, 'No lab outputs found. Run at least one playbook first.'
    seen = set()
    for path in paths:
        data = json.loads(path.read_text())
        check(data)
        assert path.parent.parent.name == data['domain']
        resource = root / data['domain'] / 'resources' / (data['resource_name'] + '.json')
        assert data['evidence']['resource_path'] == str(resource)
        assert json.loads(resource.read_text()) == data['resource'], f'Stale output: {path}'
        seen.add(data['domain'])
    if args.all:
        assert seen == DOMAINS, f'Missing domains: {DOMAINS - seen}'
    print(f'Validated {len(paths)} outputs across {len(seen)} domains')


if __name__ == '__main__':
    main()
