# 4. Estructura Git y repositorios

## Selección de arquitectura
| Modelo | Cuándo usarlo | Consideración |
| --- | --- | --- |
| Monorepo por programa | Piloto con políticas y releases compartidos | CODEOWNERS no impide leer otros directorios |
| Repos por dominio | Permisos o cadencias distintas | Requiere catálogo central y política común |
| Collections compartidas | Roles usados por varios dominios | Versionado semántico y compatibilidad |
| Inventarios separados | Ciclo de vida y acceso propios | No almacenar secretos, limitar acceso a topología |

Este workshop usa un monorepo con `playbooks/`, `roles/`, `inventories/`, `examples/`, `aap/`, `templates/`, `docs/`, `slides/`, `molecule/` y `.github/workflows/`. En adopción se puede separar en `ansible-playbooks-<dominio>`, `ansible-collections`, `ansible-inventories` y `ansible-ci`.

## Branching recomendado
Para equipos pequeños: `main` protegida, ramas cortas `feature/<ticket>-<descripcion>`, PR y releases etiquetados `vX.Y.Z`. Opcional `release/x.y` para mantenimiento. El equipo construye un candidato desde un commit, prueba y promueve esa misma revisión. No desplegar producción desde una rama mutable.

Cuando se requieren ventanas de integración, usar `develop` y promover a `main` por PR. Evitar introducir `develop` sin una necesidad concreta. Los dos modelos son alternativas documentadas, no ramas obligatorias simultáneas.

```bash
git switch -c feature/UC-NET-001-vlan
# Editar y validar el caso
git add playbooks/network.yml examples/network.yml
git commit -m "feat(network): add governed VLAN use case"
git push -u origin feature/UC-NET-001-vlan
```

## Protección efectiva de ramas
SCM configura un ruleset que exige PR, revisiones de CODEOWNERS, invalidación de aprobaciones después de cambios y resolución de conversaciones. Restringir pushes directos y bypass, incluso administradores cuando la política lo permita. Exigir checks con nombres `content`, `ansible-lint`, `molecule` y `security-scan` del workflow CI. Confirmar los nombres reales en el primer run antes de convertirlos en checks obligatorios.

CODEOWNERS solicita revisiones, pero no garantiza que Seguridad y el dueño aprueben ambos: varios owners en un path pueden satisfacerse con uno. Para riesgo alto agregar gates organizacionales o un control específico que verifique ambas aprobaciones. GitHub no aplica un ruleset por la existencia de un archivo en este repositorio.

`.github/CODEOWNERS.example` contiene equipos que deben existir en la organización y tener permisos sobre el repo. Copiarlo a `CODEOWNERS` sólo después de sustituir equipos y verificar visibilidad. No inventar equipos en la organización.

## Pipeline y evidencia
- `content`: valida RACI, YAML, links del portal local y contratos de output.
- `ansible-lint`: nombres FQCN, sintaxis de playbooks y reglas de estilo. `yamllint` incluye workflows y plantillas.
- `molecule`: converge, verificación e idempotencia del rol local sobre siete dominios. No prueba APIs reales.
- `security-scan`: detección de secrets con Gitleaks. No sustituye revisión de IAM, hardening o dependencias.
- `docs`: construye y publica Antora con tema Showroom y marca Fedora.

El pipeline no publica credentials ni hace cambios en destinos reales. Las pruebas de integración del proveedor usan runners controlados, cuentas mínimas y un entorno no productivo aislado.

## Release y rollback
Registrar commit, versión de collections, digest del EE y evidencia. AAP debe sincronizar el Project con ese commit. Volver al código anterior no revierte un recurso ya creado: ejecutar la compensación diseñada y verificar el destino. Restaurar un snapshot puede borrar cambios de terceros, por lo que requiere una decisión explícita.

## Salida
ADR sobre repos y branching, owners por path, lista de checks y evidencia del primer PR.
