'use strict';
const slides = [...document.querySelectorAll('.slide')];
let current = 0;
const previous = document.getElementById('prev');
const next = document.getElementById('next');
function show(index) {
  current = Math.max(0, Math.min(index, slides.length - 1));
  slides.forEach((slide, i) => { slide.hidden = i !== current; });
  document.getElementById('counter').textContent = `${current + 1} / ${slides.length}`;
  previous.disabled = current === 0;
  next.disabled = current === slides.length - 1;
  history.replaceState(null, '', `#${current + 1}`);
}
previous.addEventListener('click', () => show(current - 1));
next.addEventListener('click', () => show(current + 1));
document.getElementById('print').addEventListener('click', () => window.print());
document.addEventListener('keydown', event => {
  if (event.key === 'ArrowRight' || event.key === 'PageDown') { event.preventDefault(); show(current + 1); }
  if (event.key === 'ArrowLeft' || event.key === 'PageUp') { event.preventDefault(); show(current - 1); }
  if (event.key === 'Home') show(0);
  if (event.key === 'End') show(slides.length - 1);
});
const initial = Number(location.hash.slice(1));
show(Number.isFinite(initial) && initial > 0 ? initial - 1 : 0);
