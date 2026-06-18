#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_STR 64
#define MAX_COZINHAS 10
#define TAM_HASH 53

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

struct NoTrie;

typedef struct NoFilho {
    unsigned char c;
    struct NoTrie* filho;
    struct NoFilho* prox;
} NoFilho;

typedef struct NoTrie {
    Restaurante* restaurante;
    NoFilho* filhos[TAM_HASH];
} NoTrie;

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

NoTrie* novoNoTrie() {
    NoTrie* n = (NoTrie*)malloc(sizeof(NoTrie));
    n->restaurante = NULL;
    for (int i = 0; i < TAM_HASH; i++) n->filhos[i] = NULL;
    return n;
}

NoTrie* buscarFilho(NoTrie* no, char c) {
    int h = (unsigned char)c % TAM_HASH;
    NoFilho* f = no->filhos[h];
    while (f != NULL) {
        if (f->c == (unsigned char)c) return f->filho;
        f = f->prox;
    }
    return NULL;
}

NoTrie* obterOuCriarFilho(NoTrie* no, char c) {
    int h = (unsigned char)c % TAM_HASH;
    NoFilho* f = no->filhos[h];
    while (f != NULL) {
        if (f->c == (unsigned char)c) return f->filho;
        f = f->prox;
    }
    NoFilho* novo = (NoFilho*)malloc(sizeof(NoFilho));
    novo->c = (unsigned char)c;
    novo->filho = novoNoTrie();
    novo->prox = no->filhos[h];
    no->filhos[h] = novo;
    return novo->filho;
}

void inserir(NoTrie* raiz, Restaurante* r) {
    NoTrie* atual = raiz;
    char* nome = r->nome;
    for (int i = 0; nome[i] != '\0'; i++) {
        atual = obterOuCriarFilho(atual, nome[i]);
    }
    atual->restaurante = r;
}

int pesquisar(NoTrie* raiz, const char* nome) {
    NoTrie* atual = raiz;
    for (int i = 0; nome[i] != '\0'; i++) {
        char c = nome[i];
        comparacoes++;
        NoTrie* filho = buscarFilho(atual, c);
        if (filho == NULL) {
            return 0;
        }
   printf("%c ", c);
        atual = filho;
    }
    if (atual->restaurante != NULL) {
        char buf[1024];
        formatar(atual->restaurante, buf);
        printf("SIM %s\n", buf);
        return 1;
    }
    return 0;
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
    NoTrie* raizTrie = novoNoTrie();

    char linha[128];
    scanf("%s", linha);
    while (strcmp(linha, "-1") != 0) {
        int id = atoi(linha);
        Restaurante* r = buscarPorId(id);
   if (r != NULL) inserir(raizTrie, r);
        scanf("%s", linha);
    }

    char consulta[MAX_STR];
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
    fgets(consulta, sizeof(consulta), stdin);
    tirarN(consulta);

    clock_t inicio = clock();

    while (strcmp(consulta, "FIM") != 0) {
        int achou = pesquisar(raizTrie, consulta);
        if (!achou) printf("NAO\n");
        fgets(consulta, sizeof(consulta), stdin);
        tirarN(consulta);
    }

    clock_t fim = clock();
    double tempoMs = (double)(fim - inicio) / CLOCKS_PER_SEC * 1000.0;

    FILE* arq = fopen("890309_arvore_trie_hash.txt", "w");
    if (arq) {
        fprintf(arq, "890309\t%ld\t%.2f\n", comparacoes, tempoMs);
        fclose(arq);
    }
    return 0;
}