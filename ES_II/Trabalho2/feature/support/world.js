const { setWorldConstructor } = require('@cucumber/cucumber');

class MundoDoNewsFlow {
  constructor() {
    this.edicao = null;
    this.usuario = null;
    this.versoes = [];
    this.mensagem = null;
    this.assinante = null;
    this.tentativaAnterior = 0;
    this.edicaoStatus = null;
    this.ultimoErro = null;
    this.relatorioJustificativa = null;
    this.horaAtual = 12;
    this.notificacoesHoje = 0;
    this.notificacaoAgendada = null;
    this.acaoNotificacao = null;
  }
}

setWorldConstructor(MundoDoNewsFlow);