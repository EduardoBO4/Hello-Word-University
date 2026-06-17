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
    Restaurante* restaurante;
    struct NoLista* prox;
} NoLista;

typedef struct {
    int tamTab;
    NoLista* tabela[];
} Hash;

long comparacoes = 0;
long mov = 0;

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

Hash* iniciarHash(int tam) {
    mov++;
    Hash* h = (Hash*)malloc(sizeof(Hash) + tam * sizeof(NoLista*));
    if (h == NULL) return NULL;
    h->tamTab = tam;
    for (int i = 0; i < tam; i++) h->tabela[i] = NULL;
    return h;
}

int hashFunc(char* chave, Hash* h) {
    int soma = 0;
    for (int i = 0; chave[i] != '\0'; i++) soma += (int)chave[i];
    return soma % h->tamTab;
}

NoLista* criarNo(Restaurante* r) {
    NoLista* novo = (NoLista*)malloc(sizeof(NoLista));
    novo->restaurante = r;
    novo->prox = NULL;
    return novo;
}

void inserir(Restaurante* r, Hash* h) {
    int pos = hashFunc(r->nome, h);
    NoLista* novo = criarNo(r);
    novo->prox = h->tabela[pos];
    h->tabela[pos] = novo;
}

NoLista* pesquisar(char* chave, Hash* h, int* posicao) {
    int pos = hashFunc(chave, h);
    *posicao = pos;
    NoLista* atual = h->tabela[pos];
    while (atual != NULL) {
        comparacoes++;
        if (strcmp(atual->restaurante->nome, chave) == 0) {
            return atual;
        }
        atual = atual->prox;
    }
    return NULL;
}

void liberarHash(Hash* h) {
    for (int i = 0; i < h->tamTab; i++) {
        NoLista* atual = h->tabela[i];
        while (atual != NULL) {
            NoLista* prox = atual->prox;
            free(atual);
            atual = prox;
        }
    }
    free(h);
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
    Hash* h = iniciarHash(31);

    char linha[128];
    scanf("%s", linha);
    while (strcmp(linha, "-1") != 0) {
        int id = atoi(linha);
        Restaurante* r = buscarPorId(id);
        if (r != NULL) inserir(r, h);
        scanf("%s", linha);
    }

    char consulta[MAX_STR];
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
    fgets(consulta, sizeof(consulta), stdin);
    tirarN(consulta);

    clock_t inicio = clock();

    while (strcmp(consulta, "FIM") != 0) {
        int posicao = 0;
        NoLista* achou = pesquisar(consulta, h, &posicao);
        if (achou != NULL) {
            char buf[1024];
            formatar(achou->restaurante, buf);
            printf("%d %s\n", posicao, buf);
        } else {
            printf("-1\n");
        }
        fgets(consulta, sizeof(consulta), stdin);
        tirarN(consulta);
    }

    clock_t fim = clock();
    double tempoMs = (double)(fim - inicio) / CLOCKS_PER_SEC * 1000.0;

    FILE* arq = fopen("890309_hash_indireta.txt", "w");
    if (arq) {
        fprintf(arq, "890309\t%ld\t%ld\t%.2f\n", comparacoes, mov, tempoMs);
        fclose(arq);
    }
    liberarHash(h);
    return 0;
}
