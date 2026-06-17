#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>

#define BRANCO 0
#define PRETO 1

typedef struct Data {
    int dia;
    int mes;
    int ano;
} Data;

typedef struct Hora {
    int hora;
    int minuto;
} Hora;

typedef struct Restaurante {
    int id_restaurante;
    char* nome;
    char* cidade;
    int capacidade;
    double avaliacao;
    char** tipo_cozinha;
    int faixa_preco;
    Hora hora_abertura;
    Hora hora_fechamento;
    Data data_abertura;
    bool aberto;
} Restaurante;

typedef struct ColecaoRestaurante {
    int tamanho;
    Restaurante* restaurante;
} Colecao_Restaurante;

typedef struct NoArvore {
    Restaurante* dados;
    int cor;
    struct NoArvore* esquerdo;
    struct NoArvore* direito;
} NoArvore;

NoArvore* topo_arvore = NULL;
long comparacoes = 0;

NoArvore* criar_novo_no(Restaurante* res, int cor) {
    NoArvore* novo = (NoArvore*)malloc(sizeof(NoArvore));
    if (novo != NULL) {
        novo->dados = res;
        novo->cor = cor;
        novo->esquerdo = NULL;
        novo->direito = NULL;
    }
    return novo;
}

Data parse_data(char *s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

void formatar_data(Data* data, char* buffer) {
    sprintf(buffer, "%02d/%02d/%04d", data->dia, data->mes, data->ano);
}

Hora parse_hora(char *s) {
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}

void formatar_hora(Hora* hora, char* buffer) {
    sprintf(buffer, "%02d:%02d", hora->hora, hora->minuto);
}

void liberar_restaurante(Restaurante* r) {
    free(r->nome);
    free(r->cidade);
    free(r->tipo_cozinha[0]);
    free(r->tipo_cozinha);
}

Restaurante* parse_restaurante(char *s) {
    Restaurante* r = (Restaurante*)malloc(sizeof(Restaurante));
    if (r == NULL) return NULL;
    char hora_a[6], hora_f[6], data_a[11], nome[100], cidade[100], preco[10], tipo[40], aberto[10];

    sscanf(s, "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^-]-%[^,],%[^,],%[^\n]",
           &r->id_restaurante, nome, cidade, &r->capacidade,
           &r->avaliacao, tipo, preco, hora_a, hora_f,
           data_a, aberto);

    for (int i = 0; aberto[i] != '\0'; i++) {
        if (aberto[i] == '\r' || aberto[i] == '\n' || aberto[i] == ' ')
            aberto[i] = '\0';
    }
    r->aberto = (strcmp(aberto, "true") == 0);
    r->hora_abertura = parse_hora(hora_a);
    r->hora_fechamento = parse_hora(hora_f);
    r->data_abertura = parse_data(data_a);

    int tam = 0;
    while (nome[tam] != '\0') tam++;
    r->nome = (char*)malloc((tam + 1) * sizeof(char));
    sprintf(r->nome, "%s", nome);

    tam = 0;
    while (cidade[tam] != '\0') tam++;
    r->cidade = (char*)malloc((tam + 1) * sizeof(char));
    sprintf(r->cidade, "%s", cidade);

    tam = 0;
    while (preco[tam] != '\0') tam++;
    r->faixa_preco = tam;

    tam = 0;
    while (tipo[tam] != '\0') tam++;
    for (int i = 0; tipo[i] != '\0'; i++)
        if (tipo[i] == ';') tipo[i] = ',';

    r->tipo_cozinha = (char**)malloc(1 * sizeof(char*));
    r->tipo_cozinha[0] = (char*)malloc((tam + 1) * sizeof(char));
    sprintf(r->tipo_cozinha[0], "%s", tipo);

    return r;
}

void formatar_restaurante(Restaurante* restaurante, char* buffer) {
    char hora_fechamento[7], hora_abertura[7], data_abertura[12], str_aberto[6];

    formatar_hora(&restaurante->hora_abertura, hora_abertura);
    formatar_hora(&restaurante->hora_fechamento, hora_fechamento);
    formatar_data(&restaurante->data_abertura, data_abertura);

    char f_preco[5];
    int i;
    for (i = 0; i < restaurante->faixa_preco; i++) f_preco[i] = '$';
    f_preco[i] = '\0';

    sprintf(str_aberto, "%s", restaurante->aberto ? "true" : "false");
    sprintf(buffer, "[%d ## %s ## %s ## %d ## %.1lf ## [%s] ## %s ## %s-%s ## %s ## %s]",
        restaurante->id_restaurante, restaurante->nome, restaurante->cidade,
        restaurante->capacidade, restaurante->avaliacao, restaurante->tipo_cozinha[0],
        f_preco, hora_abertura, hora_fechamento, data_abertura, str_aberto);
}

void ler_csv_colecao(Colecao_Restaurante* colecao, char* path) {
    FILE *arq = fopen(path, "r");
    if (arq == NULL) return;
    char linha[200];
    fgets(linha, sizeof(linha), arq);
    int i = 0;
    while (fgets(linha, sizeof(linha), arq) != NULL) {
        Restaurante* aux = parse_restaurante(linha);
        colecao->restaurante[i] = *aux;
        i++;
        free(aux);
    }
    fclose(arq);
}

Colecao_Restaurante* ler_csv() {
    FILE *arq = fopen("/tmp/restaurantes.csv", "r");
    if (arq == NULL) return NULL;
    int tam = 0;
    char linha[200];
    while (fgets(linha, sizeof(linha), arq) != NULL) tam++;
    fclose(arq);

    Colecao_Restaurante* novaCole = (Colecao_Restaurante*)malloc(sizeof(Colecao_Restaurante));
    if (novaCole == NULL) return NULL;
    novaCole->tamanho = tam - 1;
    novaCole->restaurante = (Restaurante*)malloc((tam - 1) * sizeof(Restaurante));
    if (novaCole->restaurante == NULL) return NULL;
    ler_csv_colecao(novaCole, "/tmp/restaurantes.csv");
    return novaCole;
}

int buscarId(Colecao_Restaurante* colecao, int id_buscado) {
    for (int i = 0; i < colecao->tamanho; i++) {
        if (colecao->restaurante[i].id_restaurante == id_buscado) return i;
    }
    return -1;
}

int transformarInt(char *s) {
    int qtdCaracteres;
    for (qtdCaracteres = 0; s[qtdCaracteres] != '\0' && s[qtdCaracteres] != '\n'; qtdCaracteres++);
    int contador = 1, resposta = 0;
    for (int i = qtdCaracteres - 1; i >= 0; i--) {
        resposta += (s[i] - '0') * contador;
        contador *= 10;
    }
    return resposta;
}

NoArvore* rotacaoDir(NoArvore* no) {
    NoArvore* noEsq = no->esquerdo;
    NoArvore* noEsqDir = noEsq->direito;
    noEsq->direito = no;
    no->esquerdo = noEsqDir;
    return noEsq;
}

NoArvore* rotacaoEsq(NoArvore* no) {
    NoArvore* noDir = no->direito;
    NoArvore* noDirEsq = noDir->esquerdo;
    noDir->esquerdo = no;
    no->direito = noDirEsq;
    return noDir;
}

NoArvore* rotacaoDirEsq(NoArvore* no) {
    no->direito = rotacaoDir(no->direito);
    return rotacaoEsq(no);
}

NoArvore* rotacaoEsqDir(NoArvore* no) {
    no->esquerdo = rotacaoEsq(no->esquerdo);
    return rotacaoDir(no);
}

void balancear(NoArvore* bisavo, NoArvore* avo, NoArvore* pai, NoArvore* i) {
    if (pai->cor == PRETO) {
        if (strcmp(pai->dados->nome, avo->dados->nome) > 0) {
            if (strcmp(i->dados->nome, pai->dados->nome) > 0) {
                avo = rotacaoEsq(avo);
            } else {
                avo = rotacaoDirEsq(avo);
            }
        } else {
            if (strcmp(i->dados->nome, pai->dados->nome) < 0) {
                avo = rotacaoDir(avo);
            } else {
                avo = rotacaoEsqDir(avo);
            }
        }
        if (bisavo == NULL) {
            topo_arvore = avo;
        } else if (strcmp(avo->dados->nome, bisavo->dados->nome) < 0) {
            bisavo->esquerdo = avo;
        } else {
            bisavo->direito = avo;
        }
        avo->cor = BRANCO;
        avo->esquerdo->cor = avo->direito->cor = PRETO;
    }
}

void inserirRec(Restaurante* x, NoArvore* bisavo, NoArvore* avo, NoArvore* pai, NoArvore* i) {
    if (i == NULL) {
        if (strcmp(x->nome, pai->dados->nome) < 0)
            i = pai->esquerdo = criar_novo_no(x, PRETO);
        else
            i = pai->direito = criar_novo_no(x, PRETO);
        if (pai->cor == PRETO) balancear(bisavo, avo, pai, i);
    } else {
        if (i->esquerdo != NULL && i->direito != NULL &&
            i->esquerdo->cor == PRETO && i->direito->cor == PRETO) {
            i->cor = PRETO;
            i->esquerdo->cor = i->direito->cor = BRANCO;
            if (i == topo_arvore) {
                i->cor = BRANCO;
            } else if (pai->cor == PRETO) {
                balancear(bisavo, avo, pai, i);
            }
        }
        int cmp = strcmp(x->nome, i->dados->nome);
        if (cmp < 0) inserirRec(x, avo, pai, i, i->esquerdo);
        else if (cmp > 0) inserirRec(x, avo, pai, i, i->direito);
    }
}

void inserir(Restaurante* x) {
    if (topo_arvore == NULL) {
        topo_arvore = criar_novo_no(x, BRANCO);
    } else if (topo_arvore->esquerdo == NULL && topo_arvore->direito == NULL) {
        if (strcmp(x->nome, topo_arvore->dados->nome) < 0)
            topo_arvore->esquerdo = criar_novo_no(x, BRANCO);
        else
            topo_arvore->direito = criar_novo_no(x, BRANCO);
    } else if (topo_arvore->esquerdo == NULL) {
        if (strcmp(x->nome, topo_arvore->dados->nome) < 0) {
            topo_arvore->esquerdo = criar_novo_no(x, BRANCO);
        } else if (strcmp(x->nome, topo_arvore->direito->dados->nome) < 0) {
            topo_arvore->esquerdo = criar_novo_no(topo_arvore->dados, BRANCO);
            topo_arvore->dados = x;
        } else {
            topo_arvore->esquerdo = criar_novo_no(topo_arvore->dados, BRANCO);
            topo_arvore->dados = topo_arvore->direito->dados;
            topo_arvore->direito->dados = x;
        }
        topo_arvore->esquerdo->cor = topo_arvore->direito->cor = BRANCO;
    } else if (topo_arvore->direito == NULL) {
        if (strcmp(x->nome, topo_arvore->dados->nome) > 0) {
            topo_arvore->direito = criar_novo_no(x, BRANCO);
        } else if (strcmp(x->nome, topo_arvore->esquerdo->dados->nome) > 0) {
            topo_arvore->direito = criar_novo_no(topo_arvore->dados, BRANCO);
            topo_arvore->dados = x;
        } else {
            topo_arvore->direito = criar_novo_no(topo_arvore->dados, BRANCO);
            topo_arvore->dados = topo_arvore->esquerdo->dados;
            topo_arvore->esquerdo->dados = x;
        }
        topo_arvore->esquerdo->cor = topo_arvore->direito->cor = BRANCO;
    } else {
        inserirRec(x, NULL, NULL, NULL, topo_arvore);
    }
    topo_arvore->cor = BRANCO;
}

int localizar_registro_recursivo(char* alvo, NoArvore* no_atual) {
    if (no_atual == NULL) return 1;
    comparacoes++;
    int cmp = strcmp(no_atual->dados->nome, alvo);
    if (cmp == 0) return 0;
    if (cmp > 0) { printf("esq "); return localizar_registro_recursivo(alvo, no_atual->esquerdo); }
    printf("dir "); return localizar_registro_recursivo(alvo, no_atual->direito);
}

int pesquisar(char* alvo) {
    printf("raiz ");
    return localizar_registro_recursivo(alvo, topo_arvore);
}

void caminharCentral(NoArvore* no_atual) {
    if (no_atual != NULL) {
        caminharCentral(no_atual->esquerdo);
        char buffer[300];
        formatar_restaurante(no_atual->dados, buffer);
        printf("%s\n", buffer);
        caminharCentral(no_atual->direito);
    }
}

void limpar_memoria_arvore(NoArvore* no_atual) {
    if (no_atual != NULL) {
        limpar_memoria_arvore(no_atual->esquerdo);
        limpar_memoria_arvore(no_atual->direito);
        free(no_atual);
    }
}

int main() {
    Colecao_Restaurante* cr = ler_csv();

    char buffer_entrada[50];
    scanf("%s", buffer_entrada);
    while (strcmp(buffer_entrada, "-1") != 0) {
        int id = transformarInt(buffer_entrada);
        int idx = buscarId(cr, id);
        if (idx != -1) inserir(&cr->restaurante[idx]);
        scanf("%s", buffer_entrada);
    }

    int caractere_limpeza;
    while ((caractere_limpeza = getchar()) != '\n' && caractere_limpeza != EOF);

    fgets(buffer_entrada, sizeof(buffer_entrada), stdin);
    buffer_entrada[strcspn(buffer_entrada, "\r\n")] = '\0';

    clock_t inicio = clock();

    while (strcmp(buffer_entrada, "FIM") != 0) {
        if (pesquisar(buffer_entrada) == 0)
            printf("SIM\n");
        else
            printf("NAO\n");
        fgets(buffer_entrada, sizeof(buffer_entrada), stdin);
        buffer_entrada[strcspn(buffer_entrada, "\r\n")] = '\0';
    }

    clock_t fim = clock();
    double tempoMs = (double)(fim - inicio) / CLOCKS_PER_SEC * 1000.0;

    caminharCentral(topo_arvore);

    FILE* arq = fopen("890309_arvore_bicolor.txt", "w");
    if (arq) {
        fprintf(arq, "890309\t%ld\t%.2f\n", comparacoes, tempoMs);
        fclose(arq);
    }

    limpar_memoria_arvore(topo_arvore);
    free(cr->restaurante);
    free(cr);

    return 0;
}
