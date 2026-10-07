# 7. Ejercicio dinámico

## Formato y timebox
Un equipo por dominio más un equipo transversal. El compacto dedica 180 minutos al ejercicio: 20 a selección, 30 a diseño y RACI, 45 a desarrollo, 30 a pruebas, 25 a AAP o recorrido guiado y 30 a demo y revisión. El ampliado permite 240 minutos y un adaptador real si existe un destino autorizado.

## Pasos del equipo
1. Copiar `templates/use-case.md` y `templates/raci-editable.csv` al directorio `teams/<equipo>/` de su rama.
2. Elegir caso, definir inputs y outputs, establecer owner, riesgo y criterios de aceptación.
3. Documentar permisos, límites de destino, falla parcial y recuperación en `templates/threat-model.md`.
4. Ejecutar el ejemplo de su dominio y examinar recurso y output en `/tmp/automation-governance-lab`.
5. Cambiar una especificación del dominio en `examples/<dominio>.yml`, abrir PR y revisar checks.
6. Repetir la ejecución: sin cambios en la segunda pasada. Ejecutar una prueba negativa.
7. Configurar JT y survey en AAP de laboratorio o completar el contrato con capturas del recorrido guiado. El recorrido guiado no cuenta como evidencia de ejecución en AAP.
8. Completar solicitud, autorización, entrega de output y aceptación. Capturar revisión y job ID cuando exista controller.
9. Presentar la demo y defender RACI, riesgo y recuperación ante el equipo transversal.
10. Registrar brechas para el piloto de 90 días.

## Casos por dominio
| Dominio | Caso | Inputs particulares | Verificación real requerida |
| --- | --- | --- | --- |
| Virtualización | Clonar VM estándar | template, network, datastore, cpu, memory_mb | Estado VM, NIC, ubicación y tags |
| Network | VLAN, ACL y switchport | vlan_id, switchport, acl | Config running y conectividad autorizada |
| Cloud | Instancia gobernada | image, flavor, security_group, backup_policy | Instancia, IAM, puertos y backup |
| Plataformas | Aplicación con health check | image_digest, namespace, replicas, health_path | Readiness, endpoint y rollback |
| Seguridad | Hardening y certificados | profile, certificate_ref, compliance_policy | Perfil aplicado, cert válido y scan |
| Infraestructura | Host registrado | os_image, cmdb_class, monitoring_profile | Host, CMDB y agente activo |
| Storage | Volumen y mapping | size_gib, snapshot_policy, host_group | Tamaño, política y acceso del host |

`examples/` contiene datos sintéticos y una ficha técnica por dominio. El rol compartido convierte cada especificación en un archivo de estado con resultado verificable. Sustituir esa fase por módulos de una collection apropiada sólo en una rama con acceso a proveedor autorizado. Documentar y fijar la versión de esa collection y probar en EE.

## Tarjetas de incidente
El facilitador asigna una a cada equipo después del diseño:
- Credencial vencida antes de ejecución: detener, rotar y repetir con evidencia.
- Solicitante pide saltar gate de producción: tramitar excepción o rechazar.
- Recurso ya existe con parámetros distintos: definir reconciliación y ownership.
- API tarda más del timeout: consultar estado antes de repetir una operación de creación.
- Output contiene un dato sensible: detener notificación, sanear y evaluar alcance.
- Cambio aprobado pierde su owner: asignar suplente antes de promover.
- Rollback afecta recursos compartidos: recuperar sólo el alcance autorizado o escalar.

## Evaluación
| Dimensión | Puntos | Criterio |
| --- | --- | --- |
| Caso y contratos | 20 | Objetivo, inputs, output y aceptación completos |
| RACI y gates | 20 | Un A y un R, separación y evidencias |
| Implementación | 20 | Código claro, variables y secrets gobernados |
| Pruebas | 20 | Positiva, negativa, idempotencia y evidencia |
| Operación | 20 | AAP o contrato guiado, recuperación y entrega |

Propuesta de aprobación: 80/100, sin secretos expuestos ni gate alto omitido. El facilitador decide adopción de este umbral. Un equipo sin AAP puede completar el aprendizaje, pero debe ejecutar el piloto en controller antes de promoción productiva.

## Demo final de cinco minutos
Presentar problema, cambio realizado, segunda ejecución, output, RACI y respuesta a la tarjeta de incidente. El consumidor confirma si el output cumple su necesidad.
