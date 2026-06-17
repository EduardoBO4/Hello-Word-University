#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_STR 64
#define MAX_COZINHAS 10
#define VERMELHO 0
#define PRETO 1

typedef struct {
    int hora;
    int minuto;
} Hora;

typedef struct {
    int dia;
    int mes;
    int ano;
} Data;

typedef struct {
    int id;
    char nome[MAX_STR];
    char cidade[MAX_STR];
    int capacidade;
    double avaliacao;
    char tiposCozinha[MAX_COZINHAS][MAX_STR];
    int numCozinhas;
    int faixaPreco;
    Hora horarioAbertura;
    Hora horarioFechamento;
    Data dataAbertura;
    int aberto;
} Restaurante;

typedef struct NoRB {
    Restaurante elemento;
    int cor;
    struct NoRB *esq, *dir, *pai;
} NoRB;

NoRB* raiz = NULL;
long comparacoes = 0;

Hora parseHora(const char* s) {
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}

Data parseData(const char* s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

int contarDollar(const char* s) {
    int c = 0;
    for (int i = 0; s[i]; i++) if (s[i] == '$') c++;
    return c;
}

Restaurante parseLinha(const char* linha) {
    Restaurante r;
    char buf[1024];
    strncpy(buf, linha, sizeof(buf));
    char* saveptr;
    char* tok = strtok_r(buf, ",", &saveptr);
    r.id = atoi(tok);
    tok = strtok_r(NULL, ",", &saveptr); strncpy(r.nome, tok, MAX_STR);
    tok = strtok_r(NULL, ",", &saveptr); strncpy(r.cidade, tok, MAX_STR);
    tok = strtok_r(NULL, ",", &saveptr); r.capacidade = atoi(tok);
    tok = strtok_r(NULL, ",", &saveptr); r.avaliacao = atof(tok);
    tok = strtok_r(NULL, ",", &saveptr);
    r.numCozinhas = 0;
    char cozBuf[MAX_STR * MAX_COZINHAS];
    strncpy(cozBuf, tok, sizeof(cozBuf));
    char* saveCoz;
    char* ct = strtok_r(cozBuf, ";", &saveCoz);
    while (ct && r.numCozinhas < MAX_COZINHAS) {
        if (strlen(ct) > 0) {
            strncpy(r.tiposCozinha[r.numCozinhas], ct, MAX_STR);
            r.numCozinhas++;
        }
        ct = strtok_r(NULL, ";", &saveCoz);
    }
    tok = strtok_r(NULL, ",", &saveptr); r.faixaPreco = contarDollar(tok);
    char horBuf[MAX_STR];
    tok = strtok_r(NULL, ",", &saveptr); strncpy(horBuf, tok, MAX_STR);
    char h1[16], h2[16];
    sscanf(horBuf, "%[^-]-%s", h1, h2);
    r.horarioAbertura = parseHora(h1);
    r.horarioFechamento = parseHora(h2);
    tok = strtok_r(NULL, ",", &saveptr); r.dataAbertura = parseData(tok);
    tok = strtok_r(NULL, ",", &saveptr); r.aberto = (strncmp(tok, "true", 4) == 0) ? 1 : 0;
    return r;
}

void formatar(Restaurante* r, char* buf) {
    char cozStr[256] = "";
    for (int i = 0; i < r->numCozinhas; i++) {
        strcat(cozStr, r->tiposCozinha[i]);
        if (i < r->numCozinhas - 1) strcat(cozStr, ",");
    }
    char faixa[6] = "";
    for (int i = 0; i < r->faixaPreco; i++) faixa[i] = '$';
    faixa[r->faixaPreco] = '\0';
    char avalStr[16];
    if ((int)(r->avaliacao * 10) % 10 == 0) {
        sprintf(avalStr, "%.1f", r->avaliacao);
    } else {
        sprintf(avalStr, "%g", r->avaliacao);
    }
    sprintf(buf, "[%d ## %s ## %s ## %d ## %s ## [%s] ## %s ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]",
            r->id, r->nome, r->cidade, r->capacidade, avalStr, cozStr, faixa,
            r->horarioAbertura.hora, r->horarioAbertura.minuto,
            r->horarioFechamento.hora, r->horarioFechamento.minuto,
            r->dataAbertura.dia, r->dataAbertura.mes, r->dataAbertura.ano,
            r->aberto ? "true" : "false");
}

int getCor(NoRB* n) { return (n == NULL) ? PRETO : n->cor; }

void rotacaoEsq(NoRB* x) {
    NoRB* y = x->dir;
    x->dir = y->esq;
    if (y->esq != NULL) y->esq->pai = x;
    y->pai = x->pai;
    if (x->pai == NULL) raiz = y;
    else if (x == x->pai->esq) x->pai->esq = y;
    else x->pai->dir = y;
    y->esq = x;
    x->pai = y;
}

void rotacaoDir(NoRB* y) {
    NoRB* x = y->esq;
    y->esq = x->dir;
    if (x->dir != NULL) x->dir->pai = y;
    x->pai = y->pai;
    if (y->pai == NULL) raiz = x;
    else if (y == y->pai->esq) y->pai->esq = x;
    else y->pai->dir = x;
    x->dir = y;
    y->pai = x;
}

void inserirCorrigir(NoRB* z) {
    while (z->pai != NULL && z->pai->cor == VERMELHO) {
        if (z->pai == z->pai->pai->esq) {
            NoRB* y = z->pai->pai->dir;
            if (getCor(y) == VERMELHO) {
                z->pai->cor = PRETO;
                y->cor = PRETO;
                z->pai->pai->cor = VERMELHO;
                z = z->pai->pai;
            } else {
                if (z == z->pai->dir) {
                    z = z->pai;
                    rotacaoEsq(z);
                }
                z->pai->cor = PRETO;
                z->pai->pai->cor = VERMELHO;
                rotacaoDir(z->pai->pai);
            }
        } else {
            NoRB* y = z->pai->pai->esq;
            if (getCor(y) == VERMELHO) {
                z->pai->cor = PRETO;
                y->cor = PRETO;
                z->pai->pai->cor = VERMELHO;
                z = z->pai->pai;
            } else {
                if (z == z->pai->esq) {
                    z = z->pai;
                    rotacaoDir(z);
                }
                z->pai->cor = PRETO;
                z->pai->pai->cor = VERMELHO;
                rotacaoEsq(z->pai->pai);
            }
        }
    }
    raiz->cor = PRETO;
}

void inserir(Restaurante r) {
    NoRB* z = (NoRB*)malloc(sizeof(NoRB));
    z->elemento = r;
    z->cor = VERMELHO;
    z->esq = z->dir = z->pai = NULL;

    NoRB* y = NULL;
    NoRB* x = raiz;
    while (x != NULL) {
        y = x;
        int cmp = strcmp(r.nome, x->elemento.nome);
        if (cmp < 0) x = x->esq;
        else if (cmp > 0) x = x->dir;
        else { free(z); return; }
    }
    z->pai = y;
    if (y == NULL) raiz = z;
    else if (strcmp(r.nome, y->elemento.nome) < 0) y->esq = z;
    else y->dir = z;

    inserirCorrigir(z);
}

int pesquisar(const char* x, NoRB* i) {
    if (i == NULL) return 0;
    comparacoes++;
    int cmp = strcmp(i->elemento.nome, x);
    if (cmp == 0) return 1;
    if (cmp > 0) { printf("esq "); return pesquisar(x, i->esq); }
    printf("dir "); return pesquisar(x, i->dir);
}

void pesquisarInicio(const char* x) {
    printf("raiz ");
    if (pesquisar(x, raiz)) { printf("SIM\n"); }
    else { printf("NAO\n"); }
}

void caminhaCentral(NoRB* i) {
    if (i != NULL) {
        caminhaCentral(i->esq);
        char buf[1024];
        formatar(&i->elemento, buf);
        printf("%s\n", buf);
        caminhaCentral(i->dir);
    }
}

Restaurante restaurantes[512];
int numRestaurantes = 0;

Restaurante* buscarPorId(int id) {
    for (int i = 0; i < numRestaurantes; i++)
        if (restaurantes[i].id == id) return &restaurantes[i];
    return NULL;
}

void lerCsv() {
    FILE* f = fopen("/tmp/restaurantes.csv", "r");
    if (!f) return;
    char linha[1024];
    fgets(linha, sizeof(linha), f);
    while (fgets(linha, sizeof(linha), f)) {
        int len = strlen(linha);
        if (len > 0 && linha[len-1] == '\n') linha[len-1] = '\0';
        if (strlen(linha) > 0) {
            restaurantes[numRestaurantes++] = parseLinha(linha);
        }
    }
    fclose(f);
}

int main() {
    lerCsv();

    char linha[128];
    scanf("%s", linha);
    while (strcmp(linha, "-1") != 0) {
        int id = atoi(linha);
        Restaurante* r = buscarPorId(id);
        if (r != NULL) inserir(*r);
        scanf("%s", linha);
    }

    char consulta[MAX_STR];
    getchar();
    fgets(consulta, sizeof(consulta), stdin);
    if (strlen(consulta) > 0 && consulta[strlen(consulta)-1] == '\n')
        consulta[strlen(consulta)-1] = '\0';

    clock_t inicio = clock();

    while (strcmp(consulta, "FIM") != 0) {
        pesquisarInicio(consulta);
        fgets(consulta, sizeof(consulta), stdin);
        if (strlen(consulta) > 0 && consulta[strlen(consulta)-1] == '\n')
            consulta[strlen(consulta)-1] = '\0';
    }

    clock_t fim = clock();
    double tempoMs = (double)(fim - inicio) / CLOCKS_PER_SEC * 1000.0;

    caminhaCentral(raiz);

    FILE* arq = fopen("890309_arvore_bicolor.txt", "w");
    if (arq) {
        fprintf(arq, "890309\t%ld\t%.2f\n", comparacoes, tempoMs);
        fclose(arq);
    }
    return 0;
}
