#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>

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

Data parse_data(char *str_data) {
    Data dt;
    sscanf(str_data, "%d-%d-%d", &dt.ano, &dt.mes, &dt.dia);
    return dt;
}

void formatar_data(Data* dt_ptr, char* buf_data) {
    sprintf(buf_data, "%02d/%02d/%04d", dt_ptr->dia, dt_ptr->mes, dt_ptr->ano);
}

Hora parse_hora(char *str_hora) {
    Hora hr;
    sscanf(str_hora, "%d:%d", &hr.hora, &hr.minuto);
    return hr;
}

void formatar_hora(Hora* hr_ptr, char* buf_hora) {
    sprintf(buf_hora, "%02d:%02d", hr_ptr->hora, hr_ptr->minuto);
}

void liberar_restaurante(Restaurante* rest_ptr) {
    free(rest_ptr->nome);
    free(rest_ptr->cidade);
    free(rest_ptr->tipo_cozinha[0]);
    free(rest_ptr->tipo_cozinha);
}

Restaurante* parse_restaurante(char *linha_csv) {
    Restaurante* novo_rest = (Restaurante*)malloc(sizeof(Restaurante));
    if (novo_rest == NULL) {
        printf("Nao foi possivel criar restaurante!");
        return NULL;
    }
    char h_abrir[6], h_fechar[6], d_abrir[11], nm[100], cid[100], prc[10], tp[40], st_aberto[10];

    sscanf(linha_csv, "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^-]-%[^,],%[^,],%[^\n]",
           &novo_rest->id_restaurante, nm, cid, &novo_rest->capacidade,
           &novo_rest->avaliacao, tp, prc, h_abrir, h_fechar,
           d_abrir, st_aberto);
    
    for (int idx = 0; st_aberto[idx] != '\0'; idx++) {
        if (st_aberto[idx] == '\r' || st_aberto[idx] == '\n' || st_aberto[idx] == ' ')
            st_aberto[idx] = '\0';
    }
    novo_rest->aberto = (strcmp(st_aberto, "true") == 0);
    novo_rest->hora_abertura = parse_hora(h_abrir);
    novo_rest->hora_fechamento = parse_hora(h_fechar);
    novo_rest->data_abertura = parse_data(d_abrir);
    
    int len_nome = 0;
    while (nm[len_nome] != '\0') len_nome++;
    novo_rest->nome = (char*)malloc((len_nome + 1) * sizeof(char));
    sprintf(novo_rest->nome, "%s", nm);
    
    int len_cid = 0;
    while (cid[len_cid] != '\0') len_cid++;
    novo_rest->cidade = (char*)malloc((len_cid + 1) * sizeof(char));
    sprintf(novo_rest->cidade, "%s", cid);

    int len_prc = 0;
    while (prc[len_prc] != '\0') len_prc++;
    novo_rest->faixa_preco = len_prc;

    int len_tp = 0;
    while (tp[len_tp] != '\0') len_tp++;
    for (int idx = 0; tp[idx] != '\0'; idx++)
        if (tp[idx] == ';') tp[idx] = ',';

    novo_rest->tipo_cozinha = (char**)malloc(1 * sizeof(char*));
    novo_rest->tipo_cozinha[0] = (char*)malloc((len_tp + 1) * sizeof(char));
    sprintf(novo_rest->tipo_cozinha[0], "%s", tp);

    return novo_rest;
}

void formatar_restaurante(Restaurante* rest_ptr, char* buf_rest) {
    char h_fechamento[7], h_abertura[7], d_abertura[12], txt_aberto[6];

    formatar_hora(&rest_ptr->hora_abertura, h_abertura);
    formatar_hora(&rest_ptr->hora_fechamento, h_fechamento);
    formatar_data(&rest_ptr->data_abertura, d_abertura);
  
    char str_preco[5];
    int idx;
    for (idx = 0; idx < rest_ptr->faixa_preco; idx++) {
        str_preco[idx] = '$';
    }
    str_preco[idx] = '\0';

    if (rest_ptr->aberto == true) {
        sprintf(txt_aberto, "true");
    } else {
        sprintf(txt_aberto, "false");
    }
    sprintf(buf_rest, "[%d ## %s ## %s ## %d ## %.1lf ## [%s] ## %s ## %s-%s ## %s ## %s]",
        rest_ptr->id_restaurante, rest_ptr->nome, rest_ptr->cidade,
        rest_ptr->capacidade, rest_ptr->avaliacao, rest_ptr->tipo_cozinha[0],
        str_preco, h_abertura, h_fechamento, d_abertura, txt_aberto);
}

void ler_csv_colecao(Colecao_Restaurante* col_ptr, char* caminho) {
    FILE *arquivo = fopen(caminho, "r");
    if (arquivo == NULL) {
        printf("Erro ao abrir arquivo!");
        return;
    }
    char str_linha[200];
    fgets(str_linha, sizeof(str_linha), arquivo);
    
    int idx = 0;
    while (fgets(str_linha, sizeof(str_linha), arquivo) != NULL) {
        Restaurante* temp = parse_restaurante(str_linha);
        col_ptr->restaurante[idx] = *temp;
        idx++;
        free(temp);
    }
    fclose(arquivo);
}

Colecao_Restaurante* ler_csv() {
    FILE *arquivo = fopen("/tmp/restaurantes.csv", "r");
    if (arquivo == NULL) {
        printf("Erro ao abrir arquivo!");
        return NULL;
    }

    int total_linhas = 0;
    char str_linha[200];
    while (fgets(str_linha, sizeof(str_linha), arquivo) != NULL) {
        total_linhas++;
    }
    fclose(arquivo);

    Colecao_Restaurante* col_nova = (Colecao_Restaurante*)malloc(sizeof(Colecao_Restaurante));
    if (col_nova == NULL) {
        printf("Erro ao alocar Colecao!");
        return NULL;
    }
    col_nova->tamanho = total_linhas - 1;
    col_nova->restaurante = (Restaurante*)malloc((total_linhas - 1) * sizeof(Restaurante));
    if (col_nova->restaurante == NULL) {
        printf("Erro ao alocar restaurante!");
        return NULL;
    }
    ler_csv_colecao(col_nova, "/tmp/restaurantes.csv");

    return col_nova;
}

int buscar_id(Colecao_Restaurante* col_ptr, int id_procurado) {
    for (int idx = 0; idx < col_ptr->tamanho; idx++) {
        if (col_ptr->restaurante[idx].id_restaurante == id_procurado) {
            return idx;
        }
    }
    return -1;
}

int transformarInt(char *str_num) {
    
    int total_chars;
    for (total_chars = 0; str_num[total_chars] != '\0' && str_num[total_chars] != '\n'; total_chars++);

    int mult = 1;
    int num_final = 0;
    for (int idx = total_chars - 1; idx >= 0; idx--) {
        num_final += (str_num[idx] - '0') * mult;
        mult *= 10;
    }
    return num_final;
}

void swap(Restaurante *pos_i, Restaurante *pos_j, int* qtd_mov) {
   Restaurante temp = *pos_i;
   *pos_i = *pos_j;
   *pos_j = temp;
   (*qtd_mov) += 3;
}

void Insercao(Restaurante vetor_r[], int total, int limite_k, int* count_comp, int* count_mov) {
    for (int idx_i = 0; idx_i < total; idx_i++) {
        Restaurante elemento_aux = vetor_r[idx_i];

        (*count_mov)++;
        int idx_j = (idx_i < limite_k) ? idx_i - 1 : limite_k - 1;
        (*count_comp)++;
        while ((idx_j >= 0 && strcmp(elemento_aux.cidade, vetor_r[idx_j].cidade) < 0)) {
            (*count_mov)++;
            vetor_r[idx_j + 1] = vetor_r[idx_j];
            idx_j--;
            (*count_comp)++;
        }

        (*count_mov)++;
        vetor_r[idx_j + 1] = elemento_aux;
    }
}

int main() {
    clock_t tempo_ini, tempo_fim;
    double milissegundos;

    Colecao_Restaurante* colecao_dados = ler_csv();
    
    Restaurante lista_ordenada[1000];
    int qtd_ordenados = 0;
    int total_comp = 0, total_mov = 0;
    char buffer_entrada[5];
    
    scanf("%s", buffer_entrada);
    while (strcmp(buffer_entrada, "-1") != 0) {
        int id_convertido = transformarInt(buffer_entrada);

        int pos_encontrada = buscar_id(colecao_dados, id_convertido);
        if (pos_encontrada != -1) {
           lista_ordenada[qtd_ordenados] = colecao_dados->restaurante[pos_encontrada];  
           qtd_ordenados++;
        }
        scanf("%s", buffer_entrada);
    }
    
    tempo_ini = clock();
    Insercao(lista_ordenada, qtd_ordenados, 10, &total_comp, &total_mov);
    tempo_fim = clock();
    milissegundos = ((tempo_fim - tempo_ini) / (double)CLOCKS_PER_SEC) * 1000.0; 

    for (int idx = 0; idx < qtd_ordenados; idx++) {
        char saida_formatada[300];
        formatar_restaurante(&lista_ordenada[idx], saida_formatada);
        printf("%s\n", saida_formatada);
    }

    FILE* arq_registro = fopen("890309_insercao_parcial.txt", "w");
    if (arq_registro != NULL) {
        fprintf(arq_registro, "890309\t Comparacoes: %d\t Movimentos: %d\t Tempo: %.4lf\n", total_comp, total_mov, milissegundos);
        fclose(arq_registro);
    }

    for (int idx = 0; idx < colecao_dados->tamanho; idx++) {
        liberar_restaurante(&colecao_dados->restaurante[idx]);
    }
    free(colecao_dados->restaurante);
    free(colecao_dados);
    
    return 0;
}