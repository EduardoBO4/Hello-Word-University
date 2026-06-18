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
    int dia, mes, ano;
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

typedef struct {
    Restaurante** tabela;
    int m;
    int r;
    int nr;
    int tamTab;
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
    tok = strtok_r(NULL, ",", &saveptr); 
    
    if (strncmp(tok, "true", 4) == 0) {
        r.aberto = 1;
    } else {
        r.aberto = 0;
    }
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
        
    // ternario aqui n 
    char strAberto[6];
    if (r->aberto != 0) {
        strcpy(strAberto, "true");
    } else {
        strcpy(strAberto, "false");
    }
    
    sprintf(buf, "[%d ## %s ## %s ## %d ## %s ## [%s] ## %s ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]",
            r->id, r->nome, r->cidade, r->capacidade, avalStr, cozStr, faixa,
            r->horarioAbertura.hora, r->horarioAbertura.minuto,
            r->horarioFechamento.hora, r->horarioFechamento.minuto,
            r->dataAbertura.dia, r->dataAbertura.mes, r->dataAbertura.ano,
            strAberto);
}

void tirarN(char* s) {
    for (int i = 0; s[i] != '\0'; i++) {
        if (s[i] == '\n' || s[i] == '\r') s[i] = '\0';
    }
}

Hash* iniciarHash(int m, int r) {
    mov++;
    Hash* h = (Hash*)malloc(sizeof(Hash));
    h->m = m;
    h->r = r;
    h->nr = 0;
    h->tamTab = m + r;
    h->tabela = (Restaurante**)malloc((m + r) * sizeof(Restaurante*));
    for (int i = 0; i < m + r; i++) h->tabela[i] = NULL;
    return h;
}

// somando os caracteres pra tirar o modulo, padrao
int hashFunc(char* chave, Hash* h) {
    int soma = 0;
    for (int i = 0; chave[i] != '\0'; i++) soma += (int)chave[i];
    return soma % h->m;
}

void inserir(Restaurante* r, Hash* h) {
    int pos = hashFunc(r->nome, h);
    
    // o if com virgula contando a comparacao e a insercao mesmo assim
    if (comparacoes++, h->tabela[pos] == NULL) {
        h->tabela[pos] = r;
    } else if (comparacoes++, h->nr < h->r) {
        // jogando pra area de reserva no fim do array
        h->tabela[h->m + h->nr] = r;
        h->nr++;
    } else {
        printf("%s\n", r->nome);
    }
}

int pesquisar(char* chave, Hash* h) {
    int pos = hashFunc(chave, h);
    int resp = -1;
    
    if (comparacoes++, h->tabela[pos] != NULL && strcmp(h->tabela[pos]->nome, chave) == 0) {
        resp = pos;
    }
    
    // se nao achou na principal, caça na area de reserva
    for (int i = h->m; i < h->m + h->nr; i++) {
        if (comparacoes++, h->tabela[i] != NULL && strcmp(h->tabela[i]->nome, chave) == 0) {
            resp = i;
            i = h->m + h->nr; // quebra o loop Max gosta eu acho
        }
    }
    return resp;
}

void liberarHash(Hash* h) {
    free(h->tabela);
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
    Hash* h = iniciarHash(31, 19);

    char linha[128];
    scanf("%s", linha);
    
    // insere ate vir o -1
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
        int pos = pesquisar(consulta, h);
        if (pos == -1) {
            printf("-1\n");
        } else {
            char buf[1024];
            formatar(h->tabela[pos], buf);
            printf("%d %s\n", pos, buf);
        }
        fgets(consulta, sizeof(consulta), stdin);
        tirarN(consulta);
    }

    clock_t fim = clock();
    double tempoMs = (double)(fim - inicio) / CLOCKS_PER_SEC * 1000.0;

    // log base
    FILE* arq = fopen("890309_hash_reserva.txt", "w");
    if (arq) {
        fprintf(arq, "890309\t%ld\t%ld\t%.2f\n", comparacoes, mov, tempoMs);
        fclose(arq);
    }
    liberarHash(h);
    return 0;
}