# Caso 4. Aplicación con health check y recuperación: guion de plataformas

## Misión y criterio de aceptación
**Solicitud recibida:** El consumidor solicita desplegar una versión inmutable de aplicación en governance-lab con dos réplicas y endpoint de salud /health.

**Ticket:** `AUTO-PLT-001`. **Caso:** `UC-PLT-001`. **Recurso de laboratorio:** `app-lab-001`.

**Aceptación en el piloto real:** Digest aprobado, réplicas disponibles, readiness correcta, endpoint accesible por el consumidor y recuperación a la versión anterior comprobada.

El laboratorio crea únicamente un archivo JSON y un output sintético en localhost. Sigue primero los doce pasos; en el paso 7 ejecuta el bloque de comandos. Los pasos de proveedor y AAP requieren entornos autorizados y evidencias propias. Consultar [el proceso completo y los gates del comité](07-ejercicio.md) para resolver una devolución o cambio de alcance.

## Preparar el expediente del caso
Desde la raíz de un checkout limpio del repositorio, ejecutar:

```bash
git switch main
git pull --ff-only
git switch -c feature/auto-plt-001-plataformas
mkdir -p teams/plataformas/AUTO-PLT-001
cp templates/use-case.md teams/plataformas/AUTO-PLT-001/use-case.md
cp templates/raci-editable.csv teams/plataformas/AUTO-PLT-001/raci.csv
cp templates/threat-model.md teams/plataformas/AUTO-PLT-001/threat-model.md
cp templates/execution-evidence.md teams/plataformas/AUTO-PLT-001/execution-evidence.md
```

Si la rama o expediente ya existe, continuar con su owner; no sobrescribir trabajo de otro equipo. Completar plantillas, crear `acta-comite.md` y asignar una persona a cada rol antes de desarrollar. Registrar decisiones en el ticket además de Git. Los comandos de laboratorio no abren tickets, no convocan el comité y no crean objetos de AAP automáticamente.

## Paso 1. Recibir y completar la solicitud
**Quién actúa:** Solicitante y Dueño.

1. Solicitante identifica aplicación, owner, versión/digest, dependencias, exposición y prueba de negocio. Dueño Plataformas define objetivo de disponibilidad y criterio de aborto del despliegue.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Ficha completa, owner y consumidor registrados.

## Paso 2. Validar precondiciones con las áreas
**Quién actúa:** Dominio y dependencias.

1. Plataformas consulta cuota, namespace, políticas y capacidad; Cloud/Infraestructura validan capacidad de nodos si aplica; Network confirma ruta e ingreso. Seguridad verifica procedencia de imagen y permisos.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Precondiciones y dependencias confirmadas o bloqueos anotados.

## Paso 3. Revisar y decidir en comité
**Quién actúa:** Comité y Dueño.

1. Comité aprueba namespace de laboratorio, imagen por digest y ventana. Condiciona cambios de base de datos a plan compatible hacia atrás; si no existe reversión segura de datos, devuelve el diseño.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Acta de admisión con alcance, condiciones, owner y fechas.

**Guion de la reunión:** Solicitante lee la solicitud; Dominio expone precondiciones; Dev muestra propuesta; Seguridad expone riesgos; AAP confirma entorno; SCM confirma revisiones; Operaciones valida recuperación. Dueño registra decisión y condiciones en `teams/plataformas/AUTO-PLT-001/acta-comite.md`. El facilitador aprueba sólo el laboratorio cuando no existe integración autorizada.

## Paso 4. Diseñar solución y recuperación
**Quién actúa:** Dev y Dominio; A: Dueño.

1. Dev diseña manifests declarativos, probes, rollout con timeout y health check externo. Plataformas define baseline y rollback de versión; separar rollback de aplicación de restauración de datos.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Diseño, threat model, output y recuperación aceptados.

## Paso 5. Preparar Git, accesos y responsabilidades
**Quién actúa:** SCM, Seguridad y Admin AAP.

1. Seguridad revisa service account limitada al namespace, políticas y secrets. SCM requiere reviewer Plataformas. AAP custodia identidad de cluster sólo para el adaptador real y no habilita selección libre de namespace.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Rama del caso, reviewers, RACI y referencias de acceso.

## Paso 6. Desarrollar la automatización
**Quién actúa:** Dev Playbooks.

1. Dev usa examples/plataformas.yml. El digest de letras a es sintético y no debe desplegarse en cluster. Para integración reemplazarlo por digest real aprobado, definir manifests y módulos soportados; el JSON local no crea Pods.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Baseline local ejecutable; adaptador real separado y versionado.

## Paso 7. Ejecutar pruebas y cambio controlado
**Quién actúa:** Dev y Dueño; consulta al Dominio.

1. Cambiar replicas de 2 a 3; comprobar cuota y capacidad antes de piloto. Runner demuestra actualización local y repetición estable. En cluster, esperar available replicas y validar reparto de tráfico.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Evidencias de las seis pruebas locales y diff revisado.

### Ejecutar el plan de pruebas local
Preparar una vez el entorno del taller:

```bash
python3.11 -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements-dev.txt
```

Ejecutar desde la raíz, con el entorno virtual activo:

```bash
yamllint .
ansible-lint playbooks roles molecule
python scripts/run_guided_lab.py --domain plataformas --request-id AUTO-PLT-001 --resource-name app-lab-001
```

El runner se detiene al primer fallo. Ejecuta el playbook existente seis veces para verificar sintaxis, baseline, segunda ejecución, rechazo de `INVALID_NAME`, cambio de `replicas` de `2` a `3` y repetición del cambio. Para no alterar el ejemplo canónico, la variante aprobada se pasa como estado deseado completo por JSON sólo en el laboratorio. El Survey de producción no debe aceptar ese objeto libremente.

```bash
cat teams/plataformas/AUTO-PLT-001/app-lab-001/evidence/summary.json
cat /tmp/automation-governance-lab/plataformas/resources/app-lab-001.json
cat /tmp/automation-governance-lab/plataformas/outputs/AUTO-PLT-001-app-lab-001.json
```

| Prueba | Lo que comprueba Dev | Resultado esperado |
| --- | --- | --- |
| `01-syntax` | Sintaxis del playbook | Exit 0 |
| `02-baseline` | Inputs del ejemplo y read-back/output iguales | Estado baseline y `verified`, `simulation=true` |
| `03-repeat` | Misma solicitud y estado | Exit 0, `changed=0`, hashes sin modificación |
| `04-invalid` | Nombre fuera de política | Fallo esperado de validación y ninguna escritura |
| `05-change` | `replicas` cambia de `2` a `3` | Read-back y output coinciden con variante |
| `06-repeat-change` | Variante repetida | Exit 0, `changed=0`, hashes estables |

Dev conserva `summary.json`, inputs, logs y outputs baseline/variante. Dominio comprueba el campo cambiado y tags. El runner termina con el estado de la variante; para repetir la baseline vuelve a ejecutar el runner. El número de cambios de la baseline puede ser cero si el recurso ya existía; no se exige creación cuando ya está conforme. Para la prueba de toda la suite ejecutar `molecule test` y revisar CI.

### Preparar el cambio que se enviaría a revisión
Después de validar la variante, Dev modifica manualmente sólo `replicas` en `examples/plataformas.yml` y registra el motivo. Revisar `git diff -- examples/plataformas.yml`, ejecutar lint y Molecule, abrir PR con ticket, diff y evidencias. La variante del runner usa valores fijos del ejercicio; ejecutar sus seis pasos antes de editar la baseline. Si el ejemplo canónico ya contiene el valor final, elegir un nuevo caso de cambio con su propio plan y pruebas; no falsificar evidencias.

## Paso 8. Revisar código y seguridad
**Quién actúa:** Peer reviewer y Seguridad.

1. Reviewer comprueba digest inmutable, probes, timeouts y recuperación. Seguridad verifica que no se usan privilegios de cluster-admin y que logs de health check no exponen secrets.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** PR y Security review aprobados con observaciones cerradas.

## Paso 9. Publicar release y configurar AAP
**Quién actúa:** Admin AAP y Operaciones.

1. AAP usa playbooks/plataformas.yml en simulación. El JT del adaptador real fija cluster y namespace, Credential mínima y EE; sólo inputs funcionales aprobados quedan en Survey.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** SHA revisado, Project sincronizado, JT, Survey y RBAC verificados.

## Paso 10. Probar en proveedor no productivo
**Quién actúa:** Dev y Dominio; A: Dueño.

1. Plataformas consulta Deployment/Pods y readiness; Operaciones prueba endpoint y transacción de negocio. Introducir una imagen de prueba que falle en laboratorio, verificar aborto y volver a digest anterior; consumidor valida recuperación.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Acta funcional y recuperación demostrada; pendiente si no hay proveedor.

## Paso 11. Autorizar, solicitar y ejecutar
**Quién actúa:** Operaciones; A: Dueño.

1. Operaciones controla rollout y aborta por timeout/readiness fallida. No marcar éxito porque manifests se aplicaron. Un cambio de esquema incompatible requiere escalar; no ejecutar rollback de datos improvisado.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Autorización vigente, request ID, estado verificado y job ID.

## Paso 12. Entregar, aceptar y operar
**Quién actúa:** Operaciones y Consumidor.

1. Entregar digest efectivo, réplicas sanas, checks, job ID y prueba de recuperación. Consumidor acepta la transacción; Operaciones incorpora dashboards, alertas y procedimiento de rollback al runbook.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Output saneado aceptado y runbook con owner/suplente.

## Incidente guiado: El despliegue aplica manifests, pero /health falla.
**El facilitador lo introduce después del diseño o durante el piloto.**

1. Operaciones marca ejecución no verificada y conserva logs saneados. Plataformas diagnostica probe, configuración y dependencia; Dueño autoriza rollback de aplicación al digest previo cuando sea compatible. Dev corrige y prueba nuevamente readiness y transacción; el comité revisa cambios de riesgo y el consumidor acepta sólo después de la recuperación.
2. Operaciones registra cronología, estado antes/después y consumidores afectados; Dueño decide si hay que volver al comité. Conservar job IDs y evidencia saneada.
3. Dev añade la prueba que hubiera detectado el incidente; Dominio la valida; Seguridad revisa impacto; SCM exige revisión de la corrección; AAP publica la revisión aprobada.
4. Consumidor acepta la nueva entrega. La simulación local no reproduce este incidente de proveedor: representarlo como ejercicio de mesa si no existe un destino autorizado y marcar la evidencia pendiente.

## Cierre y traspaso entre responsables
| Traspaso | Evidencia que entrega el responsable | Confirmación requerida |
| --- | --- | --- |
| Solicitante → Dueño | Ficha de AUTO-PLT-001 y aceptación esperada | Owner toma el caso |
| Dueño/comité → Dev | Acta, límites y condiciones | Diseño autorizado |
| Dev → reviewers | PR, diff, seis pruebas y RACI | Peer review y Security review |
| SCM → AAP | SHA revisado y dependencias | Project y EE coinciden |
| Dev/Dominio → Dueño | Pruebas del proveedor y recuperación | Candidato aprobado para el destino |
| Dueño → Operaciones | Autorización, ventana y runbook | Preflight y revisión correctos |
| Operaciones → Consumidor | Output saneado, verificaciones y job IDs | Aceptado o devuelto con motivo |
| Consumidor → Dueño | Aceptación del resultado | Cierre y fecha de revisión |

## Demo de cinco minutos
1. Mostrar solicitud y acta del comité, incluida cualquier condición.
2. Explicar responsables, scope y cambio de `replicas`.
3. Mostrar `summary.json`, comparación del estado y rechazo de input inválido.
4. Explicar verificación real y respuesta al incidente; marcar lo que sigue pendiente.
5. Consumidor lee su aceptación o devolución y Operaciones presenta runbook y próxima revisión.

No cerrar como producción lista si sólo se ejecutó el rol local. Anotar brechas con owner y fecha en el roadmap.
