"""Build an offline docs view and Antora sources using Python standard library.

The Markdown subset supports headings, fenced code, tables, lists and links
used by this workshop. It is deliberately not a general Markdown renderer.
"""
from pathlib import Path
import html
import json
import re
import shutil
import zipfile

ROOT = Path(__file__).resolve().parents[1]
PUBLIC = ROOT / 'public'
NAV = json.loads((ROOT / 'docs/navigation.json').read_text())


def inline(text):
    value = html.escape(text)
    value = re.sub(r'`([^`]+)`', r'<code>\1</code>', value)
    value = re.sub(r'\*\*([^*]+)\*\*', r'<strong>\1</strong>', value)
    def link(match):
        label, url = match.groups()
        if url.endswith('.md') and (ROOT / 'docs' / url).exists():
            url = Path(url).stem + '.html'
        return f'<a href="{url}">{label}</a>'
    return re.sub(r'\[([^]]+)\]\(([^)]+)\)', link, value)


def markdown(text):
    lines = text.splitlines()
    out, toc = [], []
    i = 0
    while i < len(lines):
        line = lines[i]
        if not line.strip():
            i += 1
            continue
        if line.startswith('```'):
            code = []
            i += 1
            while i < len(lines) and not lines[i].startswith('```'):
                code.append(lines[i]); i += 1
            out.append('<pre><code>' + html.escape('\n'.join(code)) + '</code></pre>')
        elif line.startswith('#'):
            level = len(line) - len(line.lstrip('#'))
            title = line[level:].strip()
            anchor = 'section-' + str(len(toc))
            out.append(f'<h{level} id="{anchor}">{inline(title)}</h{level}>')
            if level == 2:
                toc.append((anchor, title))
        elif line.startswith('|'):
            table = []
            while i < len(lines) and lines[i].startswith('|'):
                cells = [s.strip() for s in re.split(r'(?<!\\)\|', lines[i].strip().strip('|'))]
                if not all(re.match(r'^:?-+:?$', c) for c in cells):
                    table.append(cells)
                i += 1
            rows = ['<tr>' + ''.join(f'<th scope="col">{inline(c)}</th>' for c in table[0]) + '</tr>']
            rows += ['<tr>' + ''.join(f'<td>{inline(c)}</td>' for c in row) + '</tr>' for row in table[1:]]
            out.append('<div class="table-wrap" tabindex="0"><table><thead>' + rows[0] + '</thead><tbody>' + ''.join(rows[1:]) + '</tbody></table></div>')
            continue
        elif re.match(r'^(- |\d+\. )', line):
            ordered = bool(re.match(r'^\d+\.', line))
            tag = 'ol' if ordered else 'ul'
            items = []
            while i < len(lines) and re.match(r'^(- |\d+\. )', lines[i]):
                items.append('<li>' + inline(re.sub(r'^(- |\d+\. )', '', lines[i])) + '</li>')
                i += 1
            out.append(f'<{tag}>' + ''.join(items) + f'</{tag}>')
            continue
        else:
            paragraph = [line]
            i += 1
            while i < len(lines) and lines[i].strip() and not re.match(r'^(#|\||```|- |\d+\. )', lines[i]):
                paragraph.append(lines[i]); i += 1
            out.append('<p>' + inline(' '.join(paragraph)) + '</p>')
            continue
        i += 1
    return '\n'.join(out), toc


def to_adoc(text):
    lines, out, code = text.splitlines(), [], False
    i = 0
    while i < len(lines):
        line = lines[i]
        if line.startswith('```'):
            if not code:
                out += ['[source,' + (line[3:].strip() or 'text') + ']', '----']
            else:
                out.append('----')
            code = not code
        elif code:
            out.append(line)
        elif line.startswith('|'):
            table = []
            while i < len(lines) and lines[i].startswith('|'):
                cells = [c.strip() for c in re.split(r'(?<!\\)\|', lines[i].strip().strip('|'))]
                if not all(re.match(r'^:?-+:?$', c) for c in cells):
                    table.append(cells)
                i += 1
            out += ['[cols="' + ','.join(['1'] * len(table[0])) + '",options="header"]', '|===']
            for cells in table:
                for cell in cells:
                    out.append('|' + adoc_inline(cell.replace(r'\|', '&#124;')))
            out += ['|===', '']
            continue
        else:
            if line.startswith('#'):
                n = len(line) - len(line.lstrip('#'))
                line = '=' * n + line[n:]
            elif re.match(r'^\d+\. ', line):
                line = re.sub(r'^\d+\. ', '. ', line)
            elif line.startswith('- '):
                line = '* ' + line[2:]
            out.append(adoc_inline(line))
        i += 1
    assert not code, 'Unclosed code fence'
    return '\n'.join(out) + '\n'


def adoc_inline(line):
    line = re.sub(r'\*\*([^*]+)\*\*', r'*\1*', line)
    return re.sub(r'\[([^]]+)\]\((https?://[^)]+)\)', r'\2[\1]', line)


def copy_downloads(target):
    for name in ['templates', 'aap', 'examples', 'playbooks', 'inventories', 'roles', 'scripts', 'molecule', 'docs', 'slides', 'web', 'ui-supplemental', 'workshop', '.github']:
        shutil.copytree(ROOT / name, target / name, dirs_exist_ok=True, ignore=shutil.ignore_patterns('__pycache__'))
    for name in ['index.html', 'README.md', 'LICENSE', 'requirements-dev.txt', 'ansible.cfg', 'default-site.yml', '.gitignore', '.ansible-lint', '.yamllint']:
        shutil.copyfile(ROOT / name, target / name)


def main():
    PUBLIC.mkdir(exist_ok=True)
    (PUBLIC / 'assets').mkdir(exist_ok=True)
    shutil.copyfile(ROOT / 'ui-supplemental/img/open-demo-platform.png', PUBLIC / 'assets/open-demo-platform.png')
    shutil.copytree(ROOT / 'slides', PUBLIC / 'slides', dirs_exist_ok=True)
    shutil.copyfile(ROOT / 'web/style.css', PUBLIC / 'assets/style.css')
    shutil.copyfile(ROOT / 'web/app.js', PUBLIC / 'assets/app.js')
    copy_downloads(PUBLIC / 'downloads')
    archive = PUBLIC / 'downloads/workshop-lab.zip'
    with zipfile.ZipFile(archive, 'w', zipfile.ZIP_DEFLATED) as bundle:
        for item in sorted((PUBLIC / 'downloads').rglob('*')):
            if item.is_file() and item != archive:
                bundle.write(item, item.relative_to(PUBLIC / 'downloads'))
    pages = ROOT / 'workshop/documentation/modules/ROOT/pages'
    pages.mkdir(parents=True, exist_ok=True)
    nav_adoc = []
    for index, item in enumerate(NAV):
        slug, title = item['slug'], item['title']
        text = (ROOT / 'docs' / (slug + '.md')).read_text()
        body, toc = markdown(text)
        links = []
        for n, it in enumerate(NAV):
            active = 'active' if n == index else ''
            aria = 'aria-current="page"' if n == index else ''
            links.append(f'<a class="{active}" href="{it["slug"]}.html" {aria}>{html.escape(it["title"])}</a>')
        sidebar = ''.join(links)
        toc_html = ''.join(f'<a href="#{anchor}">{html.escape(label)}</a>' for anchor, label in toc)
        prev = f'<a href="{NAV[index-1]["slug"]}.html">Anterior</a>' if index else '<span></span>'
        nxt = f'<a href="{NAV[index+1]["slug"]}.html">Siguiente</a>' if index+1 < len(NAV) else '<span></span>'
        downloads = '<section class="downloads"><h2>Material editable</h2><p><a href="downloads/workshop-lab.zip">Descargar kit de laboratorio</a></p><p><a href="downloads/templates/use-case.md">Caso de uso</a> · <a href="downloads/templates/raci-editable.csv">RACI editable</a> · <a href="downloads/templates/checklist.md">Checklist</a></p></section>' if index == 0 else ''
        hero = '<div class="hero"><div><p class="eyebrow">WORKSHOP · ANSIBLE / AAP</p><p class="hero-title">Gobierno de<br>automatización</p><p>Decisiones claras. Evidencia desde el caso de uso hasta el output.</p></div><img src="assets/open-demo-platform.png" alt="Logo de Open Demo Platform — Modelo de gobierno de automatización"></div>' if index == 0 else ''
        page = f"""<!doctype html>
<html lang="es"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>{html.escape(title)} — Open Demo Platform</title><link rel="stylesheet" href="assets/style.css"><script src="assets/app.js" defer></script></head><body>
<a class="skip" href="#content">Saltar al contenido</a><header><button id="menu-toggle" aria-expanded="false" aria-controls="sidebar">Menú</button><a class="brand" href="index.html"><img src="assets/open-demo-platform.png" alt="Logo de Open Demo Platform"><span>Open <small>Demo Platform</small></span></a><span class="header-title">Modelos de gobierno de automatización</span><a href="slides/index.html">Presentación</a><a href="https://github.com/Open-Industries/automation_governance_model">GitHub</a></header>
<nav id="sidebar" aria-label="Workshop"><p class="nav-title">GOBIERNO DE AUTOMATIZACIÓN</p><p class="version">Versión main · Español</p><label for="nav-search">Buscar sección</label><input id="nav-search" type="search" placeholder="Filtrar navegación">{sidebar}<a href="downloads/workshop-lab.zip">Kit descargable</a></nav>
<div class="workspace"><div class="toolbar">Open Demo Platform / Workshop / {html.escape(title)}</div><div class="content-layout"><main id="content">{hero}{body}{downloads}<nav class="pagination" aria-label="Entre secciones">{prev}{nxt}</nav></main><aside aria-label="En esta página"><p>EN ESTA PÁGINA</p>{toc_html}</aside></div><footer>Open Demo Platform · Open Industries<br><small>Workshop comunitario. Datos sintéticos de laboratorio.</small></footer></div></body></html>"""
        (PUBLIC / (slug + '.html')).write_text(page)
        adoc_name = 'index' if index == 0 else slug
        adoc = to_adoc(text)
        if index == 0:
            adoc += '\n== Material editable\n\nlink:../../downloads/workshop-lab.zip[Descargar kit de laboratorio]\n\nlink:../../slides/index.html[Presentación navegable]\n'
        (pages / (adoc_name + '.adoc')).write_text(adoc)
        nav_adoc.append(f'* xref:{adoc_name}.adoc[{title}]')
    (PUBLIC / 'index.html').write_text((PUBLIC / (NAV[0]['slug'] + '.html')).read_text())
    (pages.parent / 'nav.adoc').write_text('\n'.join(nav_adoc) + '\n')
    print(f'Built {len(NAV)} local pages and Antora sources')


if __name__ == '__main__':
    main()
