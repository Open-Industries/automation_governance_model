# Crear instancia gobernada

Dominio: cloud. Caso: UC-CLD-001.

## Laboratorio
```bash
ansible-playbook -i inventories/lab/hosts.yml playbooks/cloud.yml
ansible-playbook -i inventories/lab/hosts.yml playbooks/cloud.yml
```
Resultado: recurso JSON y output saneado en `/tmp/automation-governance-lab/cloud/`. La segunda ejecución debe reportar changed=0. Examinar `examples/cloud.yml` para inputs del dominio. El estado local constituye una simulación explícita.

## Adaptador real
Collection candidata: amazon.aws, azure.azcollection u openstack.cloud según proveedor. Confirmar módulo, versión, licencia y soporte para el proveedor. Fijar versión y EE, sin asumir compatibilidad entre distintas APIs.

Validar cuota, IAM, imagen y puertos. Consultar instancia por identificador estable antes de crear. Verificar backup. Recuperación debe contemplar datos creados después del provisionamiento.

## Entregas del equipo
Caso, threat model, RACI, PR, evidencia de idempotencia y fallo controlado, contrato AAP y output aceptado. El cambio de estado real se implementa en una rama de piloto con autorización del dueño y Seguridad.
