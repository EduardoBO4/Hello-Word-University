# language: pt
Funcionalidade: Aprovação e revisão de edições
  Como revisor
  Quero avaliar e aprovar edições submetidas pelos redatores
  Para garantir a qualidade do conteúdo e autorizar o disparo para a lista de assinantes

  Contexto:
    Dado que existe uma edição "Lançamento do Produto X" em estado de rascunho

  @feliz
  Cenário: Aprovação de edição por um revisor autorizado
    Dado que estou autenticado como revisor
    E que eu não sou o autor da edição
    Quando aprovo a edição
    Então o status da edição deve mudar para "Aprovada"
    E a edição fica liberada para agendamento ou disparo

  @excecao
  Cenário: Impedimento de autoaprovação pelo próprio autor
    Dado que estou autenticado como o redator autor da edição
    Quando tento aprovar a edição
    Então a ação deve ser bloqueada
    E devo ver a mensagem "O autor não pode aprovar a própria edição"

  @excecao
  Cenário: Reprovação de edição com comentário obrigatório
    Dado que estou autenticado como revisor
    Quando reprovo a edição justificando "Ajustar o título e remover o segundo parágrafo"
    Então o status da edição deve retornar para "Rascunho"
    E o comentário "Ajustar o título e remover o segundo parágrafo" deve ficar registrado