# Provisionar host y registrar CMDB

Dominio: infraestructura. Caso: UC-INF-001.

## Laboratorio
```bash
ansible-playbook -i inventories/lab/hosts.yml playbooks/infraestructura.yml
ansible-playbook -i inventories/lab/hosts.yml playbooks/infraestructura.yml
```
Resultado: recurso JSON y output saneado en `/tmp/automation-governance-lab/infraestructura/`. La segunda ejecución debe reportar changed=0. Examinar `examples/infraestructura.yml` para inputs del dominio. El estado local constituye una simulación explícita.

## Adaptador real
Collection candidata: collection del proveedor y servicenow.itsm si aplica. Confirmar módulo, versión, licencia y soporte para el proveedor. Fijar versión y EE, sin asumir compatibilidad entre distintas APIs.

Definir clave CMDB estable. Probar host, registro sin duplicados y agente activo. Si CMDB falla después de crear host, registrar estado parcial y compensación aprobada.

## Entregas del equipo
Caso, threat model, RACI, PR, evidencia de idempotencia y fallo controlado, contrato AAP y output aceptado. El cambio de estado real se implementa en una rama de piloto con autorización del dueño y Seguridad.
