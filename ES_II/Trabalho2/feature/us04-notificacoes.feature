# language: pt
Funcionalidade: Enviar notificações push com limite de horário e quantidade
  Como assinante
  Quero receber notificações push em horários adequados e em quantidade limitada
  Para não ter minha atenção interrompida em momentos de descanso ou sofrer com excesso de alertas

  Contexto:
    Dado que sou um assinante ativo no aplicativo

  @feliz
  Cenário: Entrega imediata de notificação em horário permitido
    Dado que o horário atual é 14h00
    E que recebi 1 notificação hoje
    Quando uma nova notificação é gerada
    Então a notificação deve ser entregue imediatamente no meu aplicativo

  @excecao
  Cenário: Retenção de notificação gerada na janela de silêncio
    Dado que o horário atual é 23h30
    Quando uma nova notificação é gerada
    Então a notificação não deve ser emitida imediatamente
    E a notificação deve ser agendada para entrega às 08h00 do dia seguinte

  @limite
  Esquema do Cenário: Controle do limite diário de notificações
    Dado que o horário atual é 15h00
    E que já recebi <recebidas> notificações hoje
    Quando uma nova notificação é gerada
    Então o sistema deve <acao>

    Exemplos:
      | recebidas | acao                                               |
      | 0         | entregar a notificação                             |
      | 2         | entregar a notificação                             |
      | 3         | descartar a notificação por atingir o limite diário |