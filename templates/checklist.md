# Checklist de onboarding y validación

- [ ] Caso y owners registrados, con suplentes.
- [ ] RACI válido, un A y al menos un R por etapa.
- [ ] Inputs, outputs y límites de destino documentados.
- [ ] Threat model y permisos revisados por Seguridad.
- [ ] Secrets externos, no_log y diff revisados.
- [ ] FQCN, syntax, yamllint y ansible-lint pasan.
- [ ] Prueba funcional e idempotencia con evidencia.
- [ ] Pruebas negativas y recuperación ensayadas.
- [ ] PR revisado y release vinculado a commit y EE.
- [ ] Inventory, credentials y RBAC acotados.
- [ ] JT, survey y workflow configurados y probados en no producción.
- [ ] Gate de producción y autorización de ejecución completos.
- [ ] Job ID y revisión efectiva capturados.
- [ ] Output saneado, persistido y notificado con acceso mínimo.
- [ ] Consumidor acepta el resultado.
- [ ] Métricas, revisión periódica y retiro definidos.

Por cada punto registrar responsable, fecha y enlace a evidencia en `execution-evidence.md`. Las excepciones requieren owner, aprobación y caducidad.
