const { Given, When, Then } = require('@cucumber/cucumber');
const assert = require('node:assert/strict');

Given('que estou autenticado como redator', function () {
  this.usuario = { papel: 'redator' };
});

Given('que criei uma nova edição {string}', function (titulo) {
  this.edicao = { titulo: titulo, status: 'rascunho', texto: 'Texto inicial' };
});

When('permaneço {int} segundos sem digitar no editor', function (segundos) {
  if (segundos >= 30) {
    this.versoes.push({ id: this.versoes.length + 1, conteudo: this.edicao.texto });
  }
});

Then('o rascunho da edição deve ser salvo automaticamente', function () {
  assert.ok(this.versoes.length > 0);
});

Then('uma nova versão numerada sequencialmente deve ser gerada', function () {
  assert.equal(this.versoes[0].id, 1);
});

Given('que a edição possui as versões {int}, {int} e {int} salvas', function (v1, v2, v3) {
  this.versoes = [
    { id: v1, texto: 'Versão 1' },
    { id: v2, texto: 'Versão 2' },
    { id: v3, texto: 'Versão 3' }
  ];
});

When('solicito a restauração para a versão {int}', function (versaoAlvo) {
  const versaoEncontrada = this.versoes.find(v => v.id === versaoAlvo);
  this.edicao.texto = versaoEncontrada.texto;
});

Then('o conteúdo da edição deve retornar ao estado da versão {int}', function (versaoAlvo) {
  assert.equal(this.edicao.texto, `Versão ${versaoAlvo}`);
});

Then('as versões {int}, {int} e {int} devem permanecer registradas no histórico', function (v1, v2, v3) {
  assert.equal(this.versoes.length, 3);
});