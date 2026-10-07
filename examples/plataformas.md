# Desplegar aplicación

Dominio: plataformas. Caso: UC-PLT-001.

## Laboratorio
```bash
ansible-playbook -i inventories/lab/hosts.yml playbooks/plataformas.yml
ansible-playbook -i inventories/lab/hosts.yml playbooks/plataformas.yml
```
Resultado: recurso JSON y output saneado en `/tmp/automation-governance-lab/plataformas/`. La segunda ejecución debe reportar changed=0. Examinar `examples/plataformas.yml` para inputs del dominio. El estado local constituye una simulación explícita.

## Adaptador real
Collection candidata: kubernetes.core. Confirmar módulo, versión, licencia y soporte para el proveedor. Fijar versión y EE, sin asumir compatibilidad entre distintas APIs.

Validar namespace, RBAC, imagen y recursos. Observar readiness y endpoint. Si falla, restaurar revisión conocida y verificar salud. El digest incluido es sintético y debe sustituirse antes del deploy real.

## Entregas del equipo
Caso, threat model, RACI, PR, evidencia de idempotencia y fallo controlado, contrato AAP y output aceptado. El cambio de estado real se implementa en una rama de piloto con autorización del dueño y Seguridad.
