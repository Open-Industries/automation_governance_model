# Clonar VM estándar

Dominio: virtualizacion. Caso: UC-VIRT-001.

## Laboratorio
```bash
ansible-playbook -i inventories/lab/hosts.yml playbooks/virtualizacion.yml
ansible-playbook -i inventories/lab/hosts.yml playbooks/virtualizacion.yml
```
Resultado: recurso JSON y output saneado en `/tmp/automation-governance-lab/virtualizacion/`. La segunda ejecución debe reportar changed=0. Examinar `examples/virtualizacion.yml` para inputs del dominio. El estado local constituye una simulación explícita.

## Adaptador real
Collection candidata: vmware.vmware o collection aprobada del hipervisor. Confirmar módulo, versión, licencia y soporte para el proveedor. Fijar versión y EE, sin asumir compatibilidad entre distintas APIs.

Consultar template, permisos de clonación, cuotas y conectividad. Verificar VM, NIC, datastore y tags. Compensar eliminando sólo una VM creada por la solicitud después de autorización.

## Entregas del equipo
Caso, threat model, RACI, PR, evidencia de idempotencia y fallo controlado, contrato AAP y output aceptado. El cambio de estado real se implementa en una rama de piloto con autorización del dueño y Seguridad.
