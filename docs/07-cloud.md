# Caso 3. Instancia gobernada: guion de cloud

## Misión y criterio de aceptación
**Solicitud recibida:** El consumidor necesita una instancia de laboratorio con imagen fedora-lab-image, flavor small, security group restricted-egress y backup diario.

**Ticket:** `AUTO-CLD-001`. **Caso:** `UC-CLD-001`. **Recurso de laboratorio:** `cloud-lab-001`.

**Aceptación en el piloto real:** Una instancia en la cuenta/proyecto y región permitidos, imagen y tamaño aprobados, red privada y acceso mínimo, tags completos y evidencia de backup según la política.

El laboratorio crea únicamente un archivo JSON y un output sintético en localhost. Sigue primero los doce pasos; en el paso 7 ejecuta el bloque de comandos. Los pasos de proveedor y AAP requieren entornos autorizados y evidencias propias. Consultar [el proceso completo y los gates del comité](07-ejercicio.md) para resolver una devolución o cambio de alcance.

## Preparar el expediente del caso
Desde la raíz de un checkout limpio del repositorio, ejecutar:

```bash
git switch main
git pull --ff-only
git switch -c feature/auto-cld-001-cloud
mkdir -p teams/cloud/AUTO-CLD-001
cp templates/use-case.md teams/cloud/AUTO-CLD-001/use-case.md
cp templates/raci-editable.csv teams/cloud/AUTO-CLD-001/raci.csv
cp templates/threat-model.md teams/cloud/AUTO-CLD-001/threat-model.md
cp templates/execution-evidence.md teams/cloud/AUTO-CLD-001/execution-evidence.md
```

Si la rama o expediente ya existe, continuar con su owner; no sobrescribir trabajo de otro equipo. Completar plantillas, crear `acta-comite.md` y asignar una persona a cada rol antes de desarrollar. Registrar decisiones en el ticket además de Git. Los comandos de laboratorio no abren tickets, no convocan el comité y no crean objetos de AAP automáticamente.

## Paso 1. Recibir y completar la solicitud
**Quién actúa:** Solicitante y Dueño.

1. Solicitante declara cuenta/proyecto, región, coste máximo, duración, conectividad y necesidad de backup. Dueño Cloud asigna owner y criterio de caducidad; aclarar si hay datos personales o regulados.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Ficha completa, owner y consumidor registrados.

## Paso 2. Validar precondiciones con las áreas
**Quién actúa:** Dominio y dependencias.

1. Cloud consulta cuota, imágenes permitidas y subred; Network valida rutas; Seguridad revisa IAM y reglas. Operaciones confirma cómo observar y restaurar. Si no hay cuota, registrar bloqueo antes de comité.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Precondiciones y dependencias confirmadas o bloqueos anotados.

## Paso 3. Revisar y decidir en comité
**Quién actúa:** Comité y Dueño.

1. Comité aprueba una instancia de laboratorio con presupuesto y tags obligatorios. Condiciona integración a cuenta aislada y backup restaurable. Rechaza IP pública o reglas amplias no justificadas.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Acta de admisión con alcance, condiciones, owner y fechas.

**Guion de la reunión:** Solicitante lee la solicitud; Dominio expone precondiciones; Dev muestra propuesta; Seguridad expone riesgos; AAP confirma entorno; SCM confirma revisiones; Operaciones valida recuperación. Dueño registra decisión y condiciones en `teams/cloud/AUTO-CLD-001/acta-comite.md`. El facilitador aprueba sólo el laboratorio cuando no existe integración autorizada.

## Paso 4. Diseñar solución y recuperación
**Quién actúa:** Dev y Dominio; A: Dueño.

1. Dev diseña lookup por ID/tags, creación idempotente, permisos mínimos y estados intermedios. Cloud define tratamiento de instancia existente y timeout. Recuperación distingue eliminación de instancia de retención de volúmenes y datos.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Diseño, threat model, output y recuperación aceptados.

## Paso 5. Preparar Git, accesos y responsabilidades
**Quién actúa:** SCM, Seguridad y Admin AAP.

1. Seguridad revisa identidad de servicio limitada a cuenta/proyecto y manejo de credenciales temporales. SCM asigna reviewer Cloud. AAP configura Credential sólo para el adaptador real y verifica confianza/conectividad del endpoint.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Rama del caso, reviewers, RACI y referencias de acceso.

## Paso 6. Desarrollar la automatización
**Quién actúa:** Dev Playbooks.

1. Dev lee examples/cloud.yml: image, flavor, security_group y backup_policy. Los nombres locales no configuran IAM ni backup. Adaptador debe consultar imagen, cuota, red y políticas efectivamente aplicadas.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Baseline local ejecutable; adaptador real separado y versionado.

## Paso 7. Ejecutar pruebas y cambio controlado
**Quién actúa:** Dev y Dueño; consulta al Dominio.

1. Cambiar flavor small a medium. Cloud revisa coste y si resize requiere reinicio; Dueño y consumidor aceptan impacto antes del piloto. El runner conserva imagen, security group, backup y tags.
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
python scripts/run_guided_lab.py --domain cloud --request-id AUTO-CLD-001 --resource-name cloud-lab-001
```

El runner se detiene al primer fallo. Ejecuta el playbook existente seis veces para verificar sintaxis, baseline, segunda ejecución, rechazo de `INVALID_NAME`, cambio de `flavor` de `small` a `medium` y repetición del cambio. Para no alterar el ejemplo canónico, la variante aprobada se pasa como estado deseado completo por JSON sólo en el laboratorio. El Survey de producción no debe aceptar ese objeto libremente.

```bash
cat teams/cloud/AUTO-CLD-001/cloud-lab-001/evidence/summary.json
cat /tmp/automation-governance-lab/cloud/resources/cloud-lab-001.json
cat /tmp/automation-governance-lab/cloud/outputs/AUTO-CLD-001-cloud-lab-001.json
```

| Prueba | Lo que comprueba Dev | Resultado esperado |
| --- | --- | --- |
| `01-syntax` | Sintaxis del playbook | Exit 0 |
| `02-baseline` | Inputs del ejemplo y read-back/output iguales | Estado baseline y `verified`, `simulation=true` |
| `03-repeat` | Misma solicitud y estado | Exit 0, `changed=0`, hashes sin modificación |
| `04-invalid` | Nombre fuera de política | Fallo esperado de validación y ninguna escritura |
| `05-change` | `flavor` cambia de `small` a `medium` | Read-back y output coinciden con variante |
| `06-repeat-change` | Variante repetida | Exit 0, `changed=0`, hashes estables |

Dev conserva `summary.json`, inputs, logs y outputs baseline/variante. Dominio comprueba el campo cambiado y tags. El runner termina con el estado de la variante; para repetir la baseline vuelve a ejecutar el runner. El número de cambios de la baseline puede ser cero si el recurso ya existía; no se exige creación cuando ya está conforme. Para la prueba de toda la suite ejecutar `molecule test` y revisar CI.

### Preparar el cambio que se enviaría a revisión
Después de validar la variante, Dev modifica manualmente sólo `flavor` en `examples/cloud.yml` y registra el motivo. Revisar `git diff -- examples/cloud.yml`, ejecutar lint y Molecule, abrir PR con ticket, diff y evidencias. La variante del runner usa valores fijos del ejercicio; ejecutar sus seis pasos antes de editar la baseline. Si el ejemplo canónico ya contiene el valor final, elegir un nuevo caso de cambio con su propio plan y pruebas; no falsificar evidencias.

## Paso 8. Revisar código y seguridad
**Quién actúa:** Peer reviewer y Seguridad.

1. Reviewer comprueba que no se duplican instancias ni quedan discos huérfanos al fallar. Seguridad valida acceso y salida de red con pruebas, no sólo el nombre restricted-egress.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** PR y Security review aprobados con observaciones cerradas.

## Paso 9. Publicar release y configurar AAP
**Quién actúa:** Admin AAP y Operaciones.

1. AAP fija Project, EE y cuenta del laboratorio; crear JT playbooks/cloud.yml para simulación. El piloto debe impedir que inputs cambien región/cuenta/identidad fuera de lo aprobado.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** SHA revisado, Project sincronizado, JT, Survey y RBAC verificados.

## Paso 10. Probar en proveedor no productivo
**Quién actúa:** Dev y Dominio; A: Dueño.

1. Cloud lee ID, estado, tamaño, imagen y tags en proveedor; Network y Seguridad prueban flujos permitidos y denegados. Operaciones verifica ejecución de backup y restauración de prueba; crear instancia no demuestra backup.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Acta funcional y recuperación demostrada; pendiente si no hay proveedor.

## Paso 11. Autorizar, solicitar y ejecutar
**Quién actúa:** Operaciones; A: Dueño.

1. Ante timeout, Operaciones busca por ID de operación/request ID y confirma estado con Cloud antes de reintentar. Registrar si creación terminó y evitar una segunda instancia facturable.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Autorización vigente, request ID, estado verificado y job ID.

## Paso 12. Entregar, aceptar y operar
**Quién actúa:** Operaciones y Consumidor.

1. Entregar ID, ubicación, tamaño, tags, conectividad, política y evidencia de backup. Consumidor valida uso autorizado; Dueño acepta coste. Programar revisión de caducidad y controlar recursos asociados en retiro.
2. Registrar en el expediente qué se observó, quién decidió y la evidencia. El siguiente responsable confirma recepción antes de continuar.
3. Si falta la precondición o hay una desviación respecto del alcance, dejar el caso bloqueado, asignar responsable/fecha y volver al gate correspondiente del proceso común.

**Entregable y gate:** Output saneado aceptado y runbook con owner/suplente.

## Incidente guiado: El proveedor devuelve timeout después de aceptar la creación.
**El facilitador lo introduce después del diseño o durante el piloto.**

1. Operaciones no relanza de inmediato. Cloud consulta por operation ID y tags únicos. Si existe, Dev continúa verificación; si no existe y el proveedor confirma fallo, pedir permiso para reintento. Si estado es incierto, escalar y mantener el caso abierto. Reviewer exige prueba de timeout en el adaptador y comprobación de una sola instancia.
2. Operaciones registra cronología, estado antes/después y consumidores afectados; Dueño decide si hay que volver al comité. Conservar job IDs y evidencia saneada.
3. Dev añade la prueba que hubiera detectado el incidente; Dominio la valida; Seguridad revisa impacto; SCM exige revisión de la corrección; AAP publica la revisión aprobada.
4. Consumidor acepta la nueva entrega. La simulación local no reproduce este incidente de proveedor: representarlo como ejercicio de mesa si no existe un destino autorizado y marcar la evidencia pendiente.

## Cierre y traspaso entre responsables
| Traspaso | Evidencia que entrega el responsable | Confirmación requerida |
| --- | --- | --- |
| Solicitante → Dueño | Ficha de AUTO-CLD-001 y aceptación esperada | Owner toma el caso |
| Dueño/comité → Dev | Acta, límites y condiciones | Diseño autorizado |
| Dev → reviewers | PR, diff, seis pruebas y RACI | Peer review y Security review |
| SCM → AAP | SHA revisado y dependencias | Project y EE coinciden |
| Dev/Dominio → Dueño | Pruebas del proveedor y recuperación | Candidato aprobado para el destino |
| Dueño → Operaciones | Autorización, ventana y runbook | Preflight y revisión correctos |
| Operaciones → Consumidor | Output saneado, verificaciones y job IDs | Aceptado o devuelto con motivo |
| Consumidor → Dueño | Aceptación del resultado | Cierre y fecha de revisión |

## Demo de cinco minutos
1. Mostrar solicitud y acta del comité, incluida cualquier condición.
2. Explicar responsables, scope y cambio de `flavor`.
3. Mostrar `summary.json`, comparación del estado y rechazo de input inválido.
4. Explicar verificación real y respuesta al incidente; marcar lo que sigue pendiente.
5. Consumidor lee su aceptación o devolución y Operaciones presenta runbook y próxima revisión.

No cerrar como producción lista si sólo se ejecutó el rol local. Anotar brechas con owner y fecha en el roadmap.
