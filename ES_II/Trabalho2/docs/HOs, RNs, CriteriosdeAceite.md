# Histórias de Usuário — Sistema NewsFlow

Este documento contém as 4 histórias de usuário selecionadas para o projeto NewsFlow, cobrindo o ciclo de escrita, aprovação, entrega por e-mail e notificações push.

---

## US01 — Salvar rascunho e recuperar versões

**Como** redator  
**Quero** que minhas edições sejam salvas automaticamente em versões numeradas  
**Para** não perder o trabalho em caso de queda de conexão e poder voltar a versões antigas  

### Critérios de Aceitação
1. O rascunho é salvo automaticamente a cada 30 segundos de inatividade.
2. Cada salvamento cria uma versão numerada em ordem (Versão 1, Versão 2, etc.).
3. O sistema guarda e permite restaurar até as últimas 20 versões salvas.
4. Restaurar uma versão antiga traz o texto daquela época sem apagar o histórico das versões mais recentes.

* **Regra coberta:** RN01 (Rascunho)
* **Prioridade:** Alta — Perder o texto digitado é o principal problema relatado pelos redatores.
* **Cenários associados:** `features/us01-rascunho.feature`

---

## US02 — Aprovação e revisão de edições

**Como** revisor  
**Quero** avaliar e aprovar as edições escritas pelos redatores  
**Para** garantir que o texto não tenha erros antes de ser enviado aos assinantes  

### Critérios de Aceitação
1. Nenhuma edição pode ser enviada sem a aprovação prévia de um revisor.
2. O próprio criador do texto fica proibido de aprovar sua própria edição.
3. Se o revisor reprovar a edição, ela volta para o status de rascunho.
4. Para reprovar uma edição, o revisor é obrigado a escrever um comentário explicando o motivo.

* **Regra coberta:** RN02 (Aprovação)
* **Prioridade:** Alta — Evita que notícias com erros ou dados incorretos sejam disparadas.
* **Cenários associados:** `features/us02-aprovacao.feature`

---

## US03 — Tratamento de falhas de entrega de e-mail

**Como** gestor de comunicação  
**Quero** que o sistema trate automaticamente os e-mails que falharem no envio  
**Para** manter a lista de assinantes limpa e registrar os motivos de erros não entregues  

### Critérios de Aceitação
1. Em caso de falha temporária no envio, o sistema tenta reenviar até 3 vezes (esperando 5, 15 e 45 minutos entre as tentativas).
2. Se o e-mail não existir (*hard bounce*), o assinante é removido da lista imediatamente para evitar envios inúteis.
3. Se a caixa de e-mail estiver cheia (*soft bounce*), o assinante continua na lista mesmo após as tentativas esgotadas.
4. Se o e-mail falhar após as 3 tentativas por outros motivos, o envio fica marcado como "falha definitiva" e o motivo fica gravado em um relatório para o gestor analisar.

* **Regra coberta:** RN07 (Falha de entrega)
* **Prioridade:** Média — Evita que a empresa continue tentando enviar e-mails para endereços quebrados ou sem espaço.
* **Cenários associados:** `features/us03-falhas-entrega.feature`

---

## US04 — Enviar notificações push com limite de horário e quantidade

**Como** assinante  
**Quero** receber notificações push em horários adequados e sem exagero  
**Para** não ser incomodado de madrugada nem receber avisos em excesso  

### Critérios de Aceitação
1. Notificações criadas durante o dia (das 8h às 22h) são enviadas na hora para o celular do assinante.
2. Notificações criadas à noite ou de madrugada (das 22h às 8h) ficam seguras e são entregues só às 8h da manhã.
3. Cada assinante recebe no máximo 3 notificações push por dia.
4. Qualquer notificação extra enviada no mesmo dia após a 3ª é descartada pelo sistema.

* **Regra coberta:** RN08 (Notificações)
* **Prioridade:** Média — Protege a tranquilidade do assinante para que ele não desinstale o aplicativo.
* **Cenários associados:** `features/us04-notificacoes.feature`

---

## Regras Fora do Escopo

As regras abaixo foram deixadas de fora nesta versão do trabalho para simplificar a entrega:

* **RN03 (Agendamento de envio):** Ficou de fora porque programar e-mails para saírem sozinhos no futuro precisa de um relógio agendador automático que será feito depois.
* **RN04 (Segmentação de público):** Ficou de fora porque separar assinantes por grupos (como cidade ou interesses) depende de um sistema de cadastro que está em manutenção.
* **RN05 (Limite semanal por assinante):** Ficou de fora porque a regra de travar o envio de mais de 2 newsletters por semana já é controlada direto pelo servidor de e-mails da empresa.
* **RN06 (Descadastro do e-mail):** Ficou de fora porque o botão de "cancelar assinatura" é gerenciado por uma ferramenta própria de privacidade de dados.