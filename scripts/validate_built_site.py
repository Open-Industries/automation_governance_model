"""Verify file links and Fedora branding in the generated Antora website."""
from html.parser import HTMLParser
from pathlib import Path
from urllib.parse import urlsplit, unquote
import sys


class Links(HTMLParser):
    def __init__(self):
        super().__init__()
        self.links = []

    def handle_starttag(self, tag, attrs):
        self.links.extend(value for key, value in attrs if key in {'href', 'src'} and value)


root = Path(sys.argv[1] if len(sys.argv) > 1 else 'www').resolve()
pages = list(root.rglob('*.html'))
assert pages, 'No built HTML pages found'
for page in pages:
    source = page.read_text()
    parser = Links()
    parser.feed(source)
    for ref in parser.links:
        url = urlsplit(ref)
        if url.scheme or url.netloc or not url.path:
            continue
        path = unquote(url.path)
        target = root / path.lstrip('/') if path.startswith('/') else page.parent / path
        assert target.exists(), f'Broken built link in {page.relative_to(root)}: {ref}'
    if 'automation-governance/main/' in str(page):
        assert '<html lang="es">' in source
        assert 'Fedora Demo Platform' in source and 'fedora-blue.png' in source
print(f'Validated {len(pages)} built HTML pages and Fedora branding')
