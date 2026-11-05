# language: pt
Funcionalidade: Salvar rascunho e recuperar versões
  Como redator
  Quero que minhas edições sejam salvas automaticamente em versões numeradas
  Para não perder o trabalho em caso de instabilidades e poder restaurar alterações anteriores

  Contexto:
    Dado que estou autenticado como redator
    E que criei uma nova edição "Informativo Semanal #10"

  @feliz
  Cenário: Salvamento automático e geração de versão
    Quando permaneço 30 segundos sem digitar no editor
    Então o rascunho da edição deve ser salvo automaticamente
    E uma nova versão numerada sequencialmente deve ser gerada

  @excecao
  Cenário: Restauração de versão anterior mantendo o histórico
    Dado que a edição possui as versões 1, 2 e 3 salvas
    Quando solicito a restauração para a versão 2
    Então o conteúdo da edição deve retornar ao estado da versão 2
    E as versões 1, 2 e 3 devem permanecer registradas no histórico