# 3. Roles y responsabilidades

## Roles por ámbito
| Rol | Decide y entrega | Límite |
| --- | --- | --- |
| Dueño del caso de uso | Objetivo, riesgo de negocio, SLA, prioridad y retiro | No administra credenciales por defecto |
| Desarrollo de playbooks | Código, tests, contratos y recuperación | No aprueba su propio cambio alto |
| Seguridad y Compliance | Privilegios, secretos, threat model y evidencia regulatoria | No sustituye aceptación funcional |
| Git y SCM | Repos, owners, reglas de revisión y release | No demuestra que un playbook funciona |
| AAP Admin | Projects, inventories, EE, templates, credentials y RBAC | No decide necesidad de negocio |
| Dominio técnico | Especificación y prueba del destino | No controla todos los dominios |
| Operaciones y SRE | Ejecución, entrega, monitoreo e incidentes | Opera versiones autorizadas |
| Solicitante o consumidor | Solicitud válida y aceptación de resultados | No elige credencial ni inventario arbitrario |

Los dominios son Virtualización, Network, Cloud, Plataformas, Seguridad, Infraestructura y Storage. El dueño se asigna por dominio. El rol de dominio técnico representa a sus especialistas. Seguridad como dominio puede crear automatizaciones, pero su revisión de seguridad debe asignarse a una persona independiente.

## RACI global propuesto
A = responsable final de la decisión. R = responsable de ejecutar la actividad. C = consultado. I = informado. A/R cumple ambos roles. Debe existir exactamente un A y al menos un R por fila. Varias personas pueden ser R si sus entregas están delimitadas.

| Etapa | Dueño | Dev | Seguridad | SCM | AAP Admin | Dominio técnico | Operaciones/SRE | Solicitante |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Definición del caso | A/R | C | C | I | I | C | C | C |
| Diseño técnico | A | R | C | I | C | C | C | I |
| Desarrollo | C | A/R | C | C | I | C | I | I |
| Validación técnica | C | A/R | C | C | I | C | C | I |
| Revisión de seguridad | C | C | A/R | I | C | C | C | I |
| Merge y release | C | R | C | A | I | I | I | I |
| Configuración AAP | C | C | C | I | A/R | C | C | I |
| Pruebas no productivas | A | R | C | I | C | R | C | C |
| Autorización de producción | A/R | C | C | I | C | C | C | I |
| Solicitud y programación | C | I | I | I | C | I | R | A/R |
| Ejecución y recuperación | A | C | C | I | C | C | R | I |
| Entrega de output | A | C | C | I | C | I | R | C |
| Aceptación de output | C | I | I | I | I | C | R | A/R |
| Monitoreo y mejora | A | C | C | I | C | C | R | C |
| Retiro | A | C | C | C | R | C | R | I |

## Cambios respecto a la matriz inicial
Validaciones técnicas tienen Dev como A/R. Operaciones entrega output y monitorea. El consumidor acepta el output en una etapa separada. La autorización de producción, recuperación y retiro tienen responsables explícitos. El change advisory puede ser un aprobador adicional para casos de riesgo alto sin crear un segundo A en el RACI.

## Delegación y conflictos
El A registra su suplente y cobertura. AAP Admin y Operaciones pueden recaer en una persona para el laboratorio. En producción crítica, el autor, aprobador y ejecutor se separan cuando sea posible. Documentar controles compensatorios cuando la dotación no lo permita.

## Ejercicio RACI
Copiar `templates/raci-editable.csv`, sustituir nombres de roles por personas o equipos y acordar evidencias por fila. Ejecutar `python scripts/validate_raci.py templates/raci-editable.csv`. El validador comprueba letras y cobertura, pero no reemplaza la aceptación del comité.

## Salida
RACI aprobado por dueños de dominio y equipo transversal, con suplentes y fecha de revisión.
