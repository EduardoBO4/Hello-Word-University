#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_STR 64
#define MAX_COZINHAS 10

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

typedef struct NoLista {
    Restaurante restaurante;
    struct NoLista* prox;
} NoLista;

typedef struct NoBST {
    char chave;
    NoLista* lista;
    struct NoBST* esq;
    struct NoBST* dir;
} NoBST;

NoBST* raizBST = NULL;
long comparacoes = 0;

Hora parseHora(const char* s) {
    Hora h; sscanf(s, "%d:%d", &h.hora, &h.minuto); return h;
}

Data parseData(const char* s) {
    Data d; sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia); return d;
}

int contarDollar(const char* s) {
    int c = 0; for (int i = 0; s[i]; i++) if (s[i] == '$') c++; return c;
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
        if (strlen(ct) > 0) { strncpy(r.tiposCozinha[r.numCozinhas], ct, MAX_STR); r.numCozinhas++; }
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
    if ((int)(r->avaliacao * 10) % 10 == 0)
        sprintf(avalStr, "%.1f", r->avaliacao);
    else
        sprintf(avalStr, "%g", r->avaliacao);
    sprintf(buf, "[%d ## %s ## %s ## %d ## %s ## [%s] ## %s ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]",
            r->id, r->nome, r->cidade, r->capacidade, avalStr, cozStr, faixa,
            r->horarioAbertura.hora, r->horarioAbertura.minuto,
            r->horarioFechamento.hora, r->horarioFechamento.minuto,
            r->dataAbertura.dia, r->dataAbertura.mes, r->dataAbertura.ano,
            r->aberto ? "true" : "false");
}

void tirarN(char* s) {
    for (int i = 0; s[i] != '\0'; i++) {
        if (s[i] == '\n' || s[i] == '\r') s[i] = '\0';
    }
}

void inserirNaLista(NoLista** lista, Restaurante r) {
    NoLista* novo = (NoLista*)malloc(sizeof(NoLista));
    novo->restaurante = r;
    novo->prox = NULL;
    if (*lista == NULL || strcmp(r.nome, (*lista)->restaurante.nome) < 0) {
        novo->prox = *lista;
        *lista = novo;
        return;
    }
    NoLista* atual = *lista;
    while (atual->prox != NULL && strcmp(r.nome, atual->prox->restaurante.nome) > 0)
        atual = atual->prox;
    novo->prox = atual->prox;
    atual->prox = novo;
}

NoBST* inserirBST(NoBST* no, Restaurante r) {
    char c = r.nome[0];
    if (no == NULL) {
        NoBST* novo = (NoBST*)malloc(sizeof(NoBST));
        novo->chave = c;
        novo->lista = NULL;
        novo->esq = novo->dir = NULL;
        inserirNaLista(&novo->lista, r);
        return novo;
    }
    if (c < no->chave) no->esq = inserirBST(no->esq, r);
    else if (c > no->chave) no->dir = inserirBST(no->dir, r);
    else inserirNaLista(&no->lista, r);
    return no;
}

void pesquisar(NoBST* no, char c_busca, const char* nome_busca, const char* dir) {
    printf("%s ", dir);
    if (no == NULL) {
        printf("NAO\n");
        return;
    }
    if (no->chave == c_busca) {
        NoLista* p = no->lista;
        while (p != NULL && strcmp(p->restaurante.nome, nome_busca) < 0) {
            comparacoes++;
   printf("%s ", p->restaurante.nome); 
            p = p->prox;
        }
        if (p != NULL && strcmp(p->restaurante.nome, nome_busca) == 0) {
            comparacoes++;
            char buf[1024];
            formatar(&p->restaurante, buf);
            printf("SIM %s\n", buf);
        } else {
            printf("NAO\n");
        }
    } else if (c_busca < no->chave) {
   pesquisar(no->esq, c_busca, nome_busca, "ESQ"); 
    } else {
        pesquisar(no->dir, c_busca, nome_busca, "DIR");
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
        if (strlen(linha) > 0)
            restaurantes[numRestaurantes++] = parseLinha(linha);
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
        if (r != NULL)  raizBST = inserirBST(raizBST, *r); 
        scanf("%s", linha);
    }

    char consulta[MAX_STR];
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
    fgets(consulta, sizeof(consulta), stdin);
    tirarN(consulta);

    clock_t inicio = clock();

    while (strcmp(consulta, "FIM") != 0) {
        pesquisar(raizBST, consulta[0], consulta, "RAIZ");
        fgets(consulta, sizeof(consulta), stdin);
        tirarN(consulta);
    }

    clock_t fim = clock();
    double tempoMs = (double)(fim - inicio) / CLOCKS_PER_SEC * 1000.0;

    FILE* arq = fopen("890309_hibrida_arvore_lista.txt", "w");
    if (arq) {
        fprintf(arq, "890309\t%ld\t%.2f\n", comparacoes, tempoMs);
        fclose(arq);
    }
    return 0;
}