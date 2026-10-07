# Hardening y certificados

Dominio: seguridad. Caso: UC-SEC-001.

## Laboratorio
```bash
ansible-playbook -i inventories/lab/hosts.yml playbooks/seguridad.yml
ansible-playbook -i inventories/lab/hosts.yml playbooks/seguridad.yml
```
Resultado: recurso JSON y output saneado en `/tmp/automation-governance-lab/seguridad/`. La segunda ejecución debe reportar changed=0. Examinar `examples/seguridad.yml` para inputs del dominio. El estado local constituye una simulación explícita.

## Adaptador real
Collection candidata: ansible.posix y community.crypto según plataforma. Confirmar módulo, versión, licencia y soporte para el proveedor. Fijar versión y EE, sin asumir compatibilidad entre distintas APIs.

Probar perfil en host descartable. Revisar acceso de emergencia antes de endurecer. Generar reporte de compliance. Para certificados, validar cadena, expiración y recarga de servicio. Nunca serializar claves privadas.

## Entregas del equipo
Caso, threat model, RACI, PR, evidencia de idempotencia y fallo controlado, contrato AAP y output aceptado. El cambio de estado real se implementa en una rama de piloto con autorización del dueño y Seguridad.
