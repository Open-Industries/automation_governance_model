# Provisionar VLAN y ACL

Dominio: network. Caso: UC-NET-001.

## Laboratorio
```bash
ansible-playbook -i inventories/lab/hosts.yml playbooks/network.yml
ansible-playbook -i inventories/lab/hosts.yml playbooks/network.yml
```
Resultado: recurso JSON y output saneado en `/tmp/automation-governance-lab/network/`. La segunda ejecución debe reportar changed=0. Examinar `examples/network.yml` para inputs del dominio. El estado local constituye una simulación explícita.

## Adaptador real
Collection candidata: ansible.netcommon y collection del fabricante. Confirmar módulo, versión, licencia y soporte para el proveedor. Fijar versión y EE, sin asumir compatibilidad entre distintas APIs.

Validar rango VLAN y ownership de switchport. Comparar running config, aplicar idempotentemente y probar tráfico permitido y bloqueado. Conservar configuración anterior y revertir sólo el diff autorizado.

## Entregas del equipo
Caso, threat model, RACI, PR, evidencia de idempotencia y fallo controlado, contrato AAP y output aceptado. El cambio de estado real se implementa en una rama de piloto con autorización del dueño y Seguridad.
