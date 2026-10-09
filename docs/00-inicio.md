# Modelos de gobierno de automatización

## Resultado del workshop
Cada equipo entrega un caso de uso versionado, un RACI firmado, un playbook de laboratorio, evidencia de pruebas, un contrato de output y un plan de promoción a AAP. El comité transversal revisa el conjunto y acuerda el modelo federado de gobierno para los siete dominios.

**Identidad:** Open Demo Platform, con el logo de gobierno de automatización proporcionado por Heber Romero. Iniciativa comunitaria de Open Industries. El nombre identifica este workshop comunitario. Ansible Automation Platform (AAP) conserva su nombre técnico.

## Dos recorridos
| Recorrido | Duración | Preparación | Resultado |
| --- | --- | --- | --- |
| Compacto | 8 horas transcurridas | Lectura previa de fundamentos, Git y roles | Un caso por equipo con revisión cruzada |
| Ampliado | 9 h 15 min–11 h 15 min más pausas | Laboratorio operativo | Discusión completa y extensión a un proveedor real |

Las duraciones originales suman 9 h 15 min a 11 h 15 min de contenido. Dos medios días de cuatro horas corresponden al recorrido compacto, no al ampliado.

## Antes de la sesión
1. El facilitador asigna un equipo por dominio y un equipo transversal de Seguridad, SCM y AAP. Una misma persona puede cubrir varios roles en organizaciones pequeñas, documentando los conflictos de interés.
2. Cada dominio propone un caso real sin datos sensibles y asigna dueño y operador.
3. SCM proporciona acceso a un fork o rama de equipo. Para PR desde forks privados, confirmar la política de la organización.
4. Los participantes disponen de Git, Python 3.11 o 3.12 y un entorno virtual. RHEL 9 puede requerir instalar Python 3.11 desde sus repositorios aprobados.
5. AAP Admin prepara una organización y equipos de laboratorio, un Execution Environment con ansible-core, inventory localhost y permisos mínimos. El laboratorio local funciona sin AAP.
6. Seguridad aprueba los datos sintéticos y la ubicación de evidencias.
7. Un board compartido contiene columnas: backlog, diseño, desarrollo, revisión, laboratorio, aceptación. No requiere Miro ni Mural para funcionar.

## Ejecución local
```bash
git clone https://github.com/Open-Industries/automation_governance_model.git
cd automation_governance_model
python3.11 -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements-dev.txt
ansible-playbook -i inventories/lab/hosts.yml playbooks/virtualizacion.yml
ansible-playbook -i inventories/lab/hosts.yml playbooks/virtualizacion.yml
python scripts/check_lab_outputs.py
```

La segunda ejecución debe reportar `changed=0`. Los archivos en `/tmp/automation-governance-lab` representan recursos sintéticos. Los ejemplos no se conectan a VMware, switches, nube ni storage real. El playbook de cada dominio es una base funcional para el ejercicio y una especificación para construir el adaptador real.

## Portal y presentación
```bash
python scripts/build_site.py
python -m http.server 8000 --directory public
```
Abrir `http://localhost:8000`. El portal local usa el mismo contenido del workshop. La publicación Antora utiliza el tema Showroom de la referencia y personalización Open Demo Platform. `slides/index.html` contiene la presentación navegable con flechas del teclado y vista imprimible.

## Criterios de salida
- Un A por etapa y al menos un R. A/R cuenta como ambos.
- Todas las entradas validadas, sin secrets en SCM ni output.
- PR revisado, pruebas técnicas y funcionales enlazadas a un commit.
- Proyecto de laboratorio sincronizado con una revisión conocida.
- Output entregado y aceptado por el consumidor.
- Roadmap de 90 días con owners y próxima revisión.
