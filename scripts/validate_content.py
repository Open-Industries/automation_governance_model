"""Validate repository contracts and generated local links."""
from pathlib import Path
from urllib.parse import urlsplit, unquote
import json
import re
import sys
import yaml
from validate_raci import validate
from check_lab_outputs import check

ROOT = Path(__file__).resolve().parents[1]
validate(ROOT / 'templates/raci-global.csv')
check(json.loads((ROOT / 'templates/output-contract.json').read_text()))
paths = [p for p in ROOT.rglob('*') if not any(part in {'.git', '.venv', 'public', 'www', 'node_modules'} for part in p.parts)]
for path in paths:
    if path.suffix in {'.yml', '.yaml'} or path.name in {'.ansible-lint', '.yamllint'}:
        list(yaml.safe_load_all(path.read_text()))
for path in (ROOT / 'public').rglob('*.html'):
    for ref in re.findall(r'(?:href|src)="([^"]+)"', path.read_text()):
        url = urlsplit(ref)
        if url.scheme or url.netloc or not url.path:
            continue
        target = (path.parent / unquote(url.path)).resolve()
        assert target.exists(), f'Broken local link in {path.name}: {ref}'
for chapter in json.loads((ROOT / 'docs/navigation.json').read_text()):
    assert (ROOT / 'docs' / (chapter['slug'] + '.md')).exists()
for domain in ['virtualizacion', 'network', 'cloud', 'plataformas', 'seguridad', 'infraestructura', 'storage']:
    assert (ROOT / 'playbooks' / (domain + '.yml')).exists()
    assert (ROOT / 'examples' / (domain + '.md')).exists()
print('Content, YAML, RACI, output contract and local links valid')
