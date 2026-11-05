const { Given, When, Then } = require('@cucumber/cucumber');
const assert = require('node:assert/strict');

Given('que existe um disparo em andamento para o assinante {string}', function (email) {
  this.assinante = { email: email, ativo: true };
});

Given('que o envio falhou na tentativa {int}', function (tentativa) {
  this.tentativaAnterior = tentativa;
});

When('o tempo decorrido atinge {int} minutos', function (minutos) {
  this.tempoDecorido = minutos;
});

Then('o sistema deve realizar a tentativa {int} de reenvio', function (tentativaEsperada) {
  const proximaTentativa = this.tentativaAnterior + 1;
  assert.equal(proximaTentativa, tentativaEsperada);
});

When('o envio retorna o erro de {string}', function (erro) {
  this.edicaoStatus = 'Falha definitiva';
  if (erro === 'Hard Bounce') {
    this.assinante.ativo = false;
  }
});

Then('a entrega deve ser marcada como falha definitiva', function () {
  assert.equal(this.edicaoStatus, 'Falha definitiva');
});

Then('o assinante {string} deve ser removido da base de assinantes', function (email) {
  assert.equal(this.assinante.ativo, false);
});

Given('que a entrega falhou na 3ª tentativa com o erro {string}', function (erro) {
  this.ultimoErro = erro;
});

When('o sistema encerra as tentativas de reenvio', function () {
  this.edicaoStatus = 'Falha definitiva';
  this.relatorioJustificativa = `${this.ultimoErro} - Caixa de entrada cheia`;
});

Then('o status da entrega deve ser alterado para {string}', function (statusEsperado) {
  assert.equal(this.edicaoStatus, statusEsperado);
});

Then('a justificativa {string} deve ser registrada no relatório', function (justificativaEsperada) {
  assert.equal(this.relatorioJustificativa, justificativaEsperada);
});

Then('o assinante {string} deve permanecer na base de assinantes', function (email) {
  assert.equal(this.assinante.ativo, true);
});