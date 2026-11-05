const { Given, When, Then } = require('@cucumber/cucumber');
const assert = require('node:assert/strict');

Given('que existe uma edição {string} em estado de rascunho', function (titulo) {
  this.edicao = { titulo: titulo, status: 'Rascunho', autor: 'Redator A' };
});

Given('que estou autenticado como revisor', function () {
  this.usuario = { nome: 'Revisor B', papel: 'revisor' };
});

Given('que eu não sou o autor da edição', function () {
  assert.notEqual(this.usuario.nome, this.edicao.autor);
});

When('aprovo a edição', function () {
  if (this.usuario.nome !== this.edicao.autor && this.usuario.papel === 'revisor') {
    this.edicao.status = 'Aprovada';
  }
});

Then('o status da edição deve mudar para {string}', function (statusEsperado) {
  assert.equal(this.edicao.status, statusEsperado);
});

Then('a edição fica liberada para agendamento ou disparo', function () {
  assert.equal(this.edicao.status, 'Aprovada');
});

Given('que estou autenticado como o redator autor da edição', function () {
  this.usuario = { nome: 'Redator A', papel: 'redator' };
});

When('tento aprovar a edição', function () {
  if (this.usuario.nome === this.edicao.autor) {
    this.mensagem = 'O autor não pode aprovar a própria edição';
  }
});

Then('a ação deve ser bloqueada', function () {
  assert.notEqual(this.edicao.status, 'Aprovada');
});

Then('devo ver a mensagem {string}', function (mensagemEsperada) {
  assert.equal(this.mensagem, mensagemEsperada);
});

When('reprovo a edição justificando {string}', function (comentario) {
  this.edicao.status = 'Rascunho';
  this.edicao.comentario = comentario;
});

Then('o status da edição deve retornar para {string}', function (statusEsperado) {
  assert.equal(this.edicao.status, statusEsperado);
});

Then('o comentário {string} deve ficar registrado', function (comentarioEsperado) {
  assert.equal(this.edicao.comentario, comentarioEsperado);
});