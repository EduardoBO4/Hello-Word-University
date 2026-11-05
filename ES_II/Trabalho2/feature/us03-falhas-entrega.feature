# language: pt
Funcionalidade: Tratamento de falhas de entrega de e-mail
  Como gestor
  Quero que o sistema gerencie as tentativas de reenvio, falhas de entrega e limpezas da base
  Para manter a base de assinantes limpa, preservar a reputação do domínio e auditar erros atípicos

  Contexto:
    Dado que existe um disparo em andamento para o assinante "usuario@exemplo.com"

  @limite
  Esquema do Cenário: Tentativas de reenvio em falha temporária
    Dado que o envio falhou na tentativa <tentativa_anterior>
    Quando o tempo decorrido atinge <intervalo> minutos
    Então o sistema deve realizar a tentativa <tentativa_atual> de reenvio

    Exemplos:
      | tentativa_anterior | intervalo | tentativa_atual |
      | 0                  | 0         | 1               |
      | 1                  | 5         | 2               |
      | 2                  | 15        | 3               |

  @excecao
  Cenário: Remoção imediata por hard bounce
    Quando o envio retorna o erro de "Hard Bounce"
    Então a entrega deve ser marcada como falha definitiva
    E o assinante "usuario@exemplo.com" deve ser removido da base de assinantes

  @excecao
  Cenário: Manutenção de assinante e registro de auditoria em soft bounce
    Dado que a entrega falhou na 3ª tentativa com o erro "Soft Bounce"
    Quando o sistema encerra as tentativas de reenvio
    Então o status da entrega deve ser alterado para "Falha definitiva"
    E a justificativa "Soft Bounce - Caixa de entrada cheia" deve ser registrada no relatório
    E o assinante "usuario@exemplo.com" deve permanecer na base de assinantes