'use strict';
const toggle = document.getElementById('menu-toggle');
const sidebar = document.getElementById('sidebar');
toggle.addEventListener('click', () => {
  const open = sidebar.classList.toggle('open');
  toggle.setAttribute('aria-expanded', String(open));
});
document.getElementById('nav-search').addEventListener('input', event => {
  const query = event.target.value.toLocaleLowerCase('es');
  sidebar.querySelectorAll('a').forEach(link => {
    link.hidden = !link.textContent.toLocaleLowerCase('es').includes(query);
  });
});
document.addEventListener('keydown', event => {
  if (event.key === 'Escape') {
    sidebar.classList.remove('open');
    toggle.setAttribute('aria-expanded', 'false');
  }
});
