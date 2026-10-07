"""Check one accountable owner and at least one responsible owner per stage."""
import csv
import sys
from pathlib import Path


def validate(path):
    with Path(path).open(newline='', encoding='utf-8-sig') as stream:
        rows = list(csv.reader(stream))
    assert len(rows) > 1, 'Empty RACI'
    errors = []
    for line, row in enumerate(rows[1:], 2):
        if len(row) != len(rows[0]):
            errors.append(f'Line {line}: column count mismatch')
            continue
        values = [v.strip().upper() for v in row[1:]]
        if any(v not in {'A', 'R', 'C', 'I', 'A/R', 'R/A'} for v in values):
            errors.append(f'{row[0]}: incomplete or invalid role')
        if sum('A' in v.split('/') for v in values) != 1:
            errors.append(f'{row[0]}: exactly one A required')
        if not any('R' in v.split('/') for v in values):
            errors.append(f'{row[0]}: at least one R required')
    if errors:
        raise ValueError('\n'.join(errors))
    return len(rows) - 1


if __name__ == '__main__':
    path = sys.argv[1] if len(sys.argv) > 1 else 'templates/raci-global.csv'
    print(f'RACI valid: {validate(path)} stages')
