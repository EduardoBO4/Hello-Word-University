const { Given, When, Then } = require('@cucumber/cucumber');
const assert = require('node:assert/strict');

Given('que sou um assinante ativo no aplicativo', function () {
  this.assinanteApp = { ativo: true };
});

Given(/^que o horário atual é (\d+)h(\d+)$/, function (hora, min) {
  this.horaAtual = parseInt(hora, 10);
});

Given('que recebi {int} notificação hoje', function (qtd) {
  this.notificacoesHoje = qtd;
});

Given('que já recebi {int} notificações hoje', function (qtd) {
  this.notificacoesHoje = qtd;
});

When('uma nova notificação é gerada', function () {
  if (this.notificacoesHoje >= 3) {
    this.acaoNotificacao = 'descartar a notificação por atingir o limite diário';
  } else if (this.horaAtual >= 22 || this.horaAtual < 8) {
    this.notificacaoAgendada = '08h00';
    this.acaoNotificacao = 'agendada';
  } else {
    this.notificacoesHoje += 1;
    this.acaoNotificacao = 'entregar a notificação';
  }
});

Then('a notificação deve ser entregue imediatamente no meu aplicativo', function () {
  assert.equal(this.notificacoesHoje, 2);
});

Then('a notificação não deve ser emitida imediatamente', function () {
  assert.equal(this.notificacaoAgendada, '08h00');
});

Then('a notificação deve ser agendada para entrega às 08h00 do dia seguinte', function () {
  assert.equal(this.notificacaoAgendada, '08h00');
});

Then('o sistema deve entregar a notificação', function () {
  assert.equal(this.acaoNotificacao, 'entregar a notificação');
});

Then('o sistema deve descartar a notificação por atingir o limite diário', function () {
  assert.equal(this.acaoNotificacao, 'descartar a notificação por atingir o limite diário');
});

Then('o sistema deve {string}', function (acaoEsperada) {
  assert.equal(this.acaoNotificacao, acaoEsperada);
});