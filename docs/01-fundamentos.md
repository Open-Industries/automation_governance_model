# 1. Fundamentos y contexto

## Gobierno de automatización
El gobierno define quién decide, quién ejecuta, qué controles aplican y qué evidencia demuestra que una automatización produce un resultado autorizado. Incluye su definición, diseño, ejecución, entrega de resultados, operación y retiro.

Una automatización con gobierno tiene dueño de negocio y técnico, alcance limitado, código revisado, entradas controladas y evidencia vinculada a una versión. AAP aporta ejecución centralizada y control de acceso. Las decisiones sobre riesgo, propiedad y aceptación corresponden a la organización.

## Riesgos y controles
| Riesgo | Ejemplo | Control | Evidencia |
| --- | --- | --- | --- |
| Privilegios excesivos | Cuenta cloud puede borrar cualquier proyecto | Credencial acotada por dominio y entorno | Revisión de IAM y RBAC |
| Drift | Operador edita configuración fuera de Git | Reconciliación y detección de diferencias | Reporte de drift y ticket |
| Deuda técnica | Playbook sin dueño ni pruebas | Catálogo y revisión periódica | Fecha de revisión y tests |
| Incumplimiento | Logs contienen datos personales | Minimización y retención aprobada | Clasificación del output |
| Cambios no autorizados | Ejecución nocturna sin change record | Workflow y ventana de cambio | Approval y job ID |
| Falsa confianza | Check mode marca éxito pero API falla | Prueba funcional sobre destino | Lectura posterior y aceptación |

## Modelo federado
Los dominios poseen sus casos de uso y desarrollan contenido. El equipo transversal mantiene políticas comunes, plataforma y controles. Un foro mensual resuelve excepciones, prioriza deuda y evalúa métricas. El sponsor financia el modelo y designa reemplazos para owners ausentes.

## Beneficios medibles
Medir tiempo desde caso aprobado hasta release, tasa de éxito, reprocesos, porcentaje de casos con owner y evidencia completa, y horas manuales evitadas. Tomar una línea base antes del piloto. No atribuir ahorro al tiempo completo de una tarea si todavía requiere supervisión humana.

## Actividad de apertura
Cada equipo presenta un incidente o dificultad de automatización en dos minutos. Identifica causa, impacto y evidencia faltante. El facilitador conecta esos ejemplos con los controles propuestos.

## Evidencia y aceptación
Entregar un mapa de riesgos y una línea base. El dueño del dominio valida el problema y el sponsor confirma el alcance del piloto.
