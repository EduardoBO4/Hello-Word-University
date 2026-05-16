#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <math.h>

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

typedef struct Celula {
    struct Celula* prox;
    Restaurante* restaurante;
} Fila;

Fila* cabeca;
Fila* cauda;

Data decodificar_data(char *str) {
    Data dt;
    sscanf(str, "%d-%d-%d", &dt.ano, &dt.mes, &dt.dia);
    return dt;
}

Hora decodificar_hora(char *str) {
    Hora hr;
    sscanf(str, "%d:%d", &hr.hora, &hr.minuto);
    return hr;
}

char* duplicar_texto(const char* original) {
    int comp = strlen(original);
    char* copia = (char*)malloc((comp + 1) * sizeof(char));
    strcpy(copia, original);
    return copia;
}

Restaurante* extrair_restaurante(char *linha) {
    Restaurante* rec = (Restaurante*)malloc(sizeof(Restaurante));
    char h_abertura[10], h_fechamento[10], d_abertura[15];
    char str_nome[150], str_cidade[150], str_preco[15], str_tipo[60], str_aberto[15];

    sscanf(linha, "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^-]-%[^,],%[^,],%[^\n\r]",
           &rec->id_restaurante, str_nome, str_cidade, &rec->capacidade,
           &rec->avaliacao, str_tipo, str_preco, h_abertura, h_fechamento,
           d_abertura, str_aberto);

    for (int k = 0; str_aberto[k] != '\0'; k++) {
        if (str_aberto[k] == ' ' || str_aberto[k] == '\r' || str_aberto[k] == '\n') {
            str_aberto[k] = '\0';
        }
    }

    rec->aberto = (strcmp(str_aberto, "true") == 0);
    rec->hora_abertura = decodificar_hora(h_abertura);
    rec->hora_fechamento = decodificar_hora(h_fechamento);
    rec->data_abertura = decodificar_data(d_abertura);

    rec->nome = duplicar_texto(str_nome);
    rec->cidade = duplicar_texto(str_cidade);

    rec->faixa_preco = strlen(str_preco);

    for (int k = 0; str_tipo[k] != '\0'; k++) {
        if (str_tipo[k] == ';') {
            str_tipo[k] = ',';
        }
    }

    rec->tipo_cozinha = (char**)malloc(sizeof(char*));
    rec->tipo_cozinha[0] = duplicar_texto(str_tipo);

    return rec;
}

void exportar_restaurante(Restaurante* r, char* saida) {
    char buffer_abertura[10];
    char buffer_fechamento[10];
    char buffer_data[15];
    char buffer_preco[10];

    sprintf(buffer_abertura, "%02d:%02d", r->hora_abertura.hora, r->hora_abertura.minuto);
    sprintf(buffer_fechamento, "%02d:%02d", r->hora_fechamento.hora, r->hora_fechamento.minuto);
    sprintf(buffer_data, "%02d/%02d/%04d", r->data_abertura.dia, r->data_abertura.mes, r->data_abertura.ano);

    int p;
    for (p = 0; p < r->faixa_preco; p++) {
        buffer_preco[p] = '$';
    }
    buffer_preco[p] = '\0';

    char status[10];
    strcpy(status, r->aberto ? "true" : "false");

    sprintf(saida, "[%d ## %s ## %s ## %d ## %.1lf ## [%s] ## %s ## %s-%s ## %s ## %s]",
            r->id_restaurante, r->nome, r->cidade, r->capacidade,
            r->avaliacao, r->tipo_cozinha[0], buffer_preco,
            buffer_abertura, buffer_fechamento, buffer_data, status);
}

Colecao_Restaurante* carregar_dados() {
    FILE *arq = fopen("/tmp/restaurantes.csv", "r");
    if (!arq) return NULL;

    int num_registros = 0;
    char temp_linha[400];

    while (fgets(temp_linha, sizeof(temp_linha), arq) != NULL) {
        num_registros++;
    }

    rewind(arq);

    Colecao_Restaurante* colecao = (Colecao_Restaurante*)malloc(sizeof(Colecao_Restaurante));
    colecao->tamanho = num_registros - 1;
    colecao->restaurante = (Restaurante* )malloc((num_registros - 1) * sizeof(Restaurante));

    fgets(temp_linha, sizeof(temp_linha), arq);

    for (int j = 0; j < colecao->tamanho; j++) {
        fgets(temp_linha, sizeof(temp_linha), arq);
        Restaurante* r_temp = extrair_restaurante(temp_linha);
        colecao->restaurante[j] = *r_temp;
        free(r_temp);
    }

    fclose(arq);
    return colecao;
}

int localizar_id(Colecao_Restaurante* col, int alvo) {
    int pos = 0;
    while (pos < col->tamanho) {
        if (col->restaurante[pos].id_restaurante == alvo) {
            return pos;
        }
        pos++;
    }
    return -1;
}

int converter_str_int(char *texto) {
    int valor = 0;
    for (int j = 0; texto[j] != '\0' && texto[j] != '\n' && texto[j] != '\r'; j++) {
        valor = (valor * 10) + (texto[j] - '0');
    }
    return valor;
}

void inicializar_lista() {
    cabeca = (Fila*)malloc(sizeof(Fila));
    cabeca->restaurante = NULL;
    cabeca->prox = NULL;
    cauda = cabeca;
}

void adicionar_fim(Restaurante* item) {
    Fila* novo_no = (Fila*)malloc(sizeof(Fila));
    novo_no->restaurante = item;
    novo_no->prox = NULL;
    cauda->prox = novo_no;
    cauda = novo_no;
}

Restaurante* retirar_inicio() {
    if (cabeca == cauda) return NULL;
    Fila* rem = cabeca;
    cabeca = cabeca->prox;
    Restaurante* info = cabeca->restaurante;
    free(rem);
    return info;
}


void processar_registro(Restaurante* r) {
    adicionar_fim(r);
}

int main() {
    Colecao_Restaurante* base = carregar_dados();
    inicializar_lista();

    char instrucao[50];
    scanf("%s", instrucao);

    while (strcmp(instrucao, "-1") != 0) {
        int chave = converter_str_int(instrucao);
        int idx = localizar_id(base, chave);
        if (idx != -1) {
            processar_registro(&base->restaurante[idx]);
        }
        scanf("%s", instrucao);
    }

    scanf("%s", instrucao);
    int operacoes = converter_str_int(instrucao);

    for (int w = 0; w < operacoes; w++) {
        char comando[10];
        scanf("%s", comando);

        if (comando[0] == 'I') {
            int chave_nova;
            scanf("%d", &chave_nova);
            int ind = localizar_id(base, chave_nova);
            if (ind != -1) {
                processar_registro(&base->restaurante[ind]);
            }
        } else if (comando[0] == 'R') {
            Restaurante* apagado = retirar_inicio();
            if (apagado) {
               
                printf("(R)%s\n", apagado->nome);
            }
        }
    }

    Fila* iterador = cabeca->prox;
    while (iterador != NULL) {
        char txt_saida[400];
        exportar_restaurante(iterador->restaurante, txt_saida);
        printf("%s\n", txt_saida);
        iterador = iterador->prox;
    }

    while (cabeca != cauda) {
        retirar_inicio();
    }
    free(cabeca);

   
    free(base->restaurante);
    free(base);

    return 0;
}