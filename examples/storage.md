# Crear volumen y mapping

Dominio: storage. Caso: UC-STO-001.

## Laboratorio
```bash
ansible-playbook -i inventories/lab/hosts.yml playbooks/storage.yml
ansible-playbook -i inventories/lab/hosts.yml playbooks/storage.yml
```
Resultado: recurso JSON y output saneado en `/tmp/automation-governance-lab/storage/`. La segunda ejecución debe reportar changed=0. Examinar `examples/storage.yml` para inputs del dominio. El estado local constituye una simulación explícita.

## Adaptador real
Collection candidata: collection del fabricante de storage. Confirmar módulo, versión, licencia y soporte para el proveedor. Fijar versión y EE, sin asumir compatibilidad entre distintas APIs.

Validar pool, capacidad y host_group. Verificar tamaño, snapshots y mapping. No reducir ni borrar volúmenes con datos para simular rollback. Recuperar mapping o escalar si la operación es irreversible.

## Entregas del equipo
Caso, threat model, RACI, PR, evidencia de idempotencia y fallo controlado, contrato AAP y output aceptado. El cambio de estado real se implementa en una rama de piloto con autorización del dueño y Seguridad.
