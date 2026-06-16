#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>

int comp = 0, mov = 0, k = 10;

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

Data parse_data(char *s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

void formatar_data(Data* data, char* buffer) {
    sprintf(buffer,"%02d/%02d/%04d", data->dia, data->mes, data->ano);
}

Hora parse_hora(char *s) {
    Hora h;
    sscanf(s,"%d:%d", &h.hora, &h.minuto);
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
    if(r == NULL) return NULL;
    
    char hora_a[6], hora_f[6], data_a[11], nome[100], cidade[100], preco[10], tipo[40], aberto[10];
    
    sscanf(s, "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^-]-%[^,],%[^,],%[^\n]",
           &r->id_restaurante, nome, cidade, &r->capacidade,
           &r->avaliacao, tipo, preco, hora_a, hora_f,
           data_a, aberto);
           
    for(int i = 0; aberto[i] != '\0'; i++) {
        if(aberto[i] == '\r' || aberto[i] == '\n' || aberto[i] == ' ')
            aberto[i] = '\0';
    }
    
    r->aberto = (strcmp(aberto, "true") == 0);
    r->hora_abertura = parse_hora(hora_a);
    r->hora_fechamento = parse_hora(hora_f);
    r->data_abertura = parse_data(data_a);
    
    int tam = 0;
    while(nome[tam] != '\0') tam++;
    r->nome = (char*)malloc((tam + 1) * sizeof(char));
    sprintf(r->nome,"%s", nome);
    
    tam = 0;
    while(cidade[tam] != '\0') tam++;
    r->cidade = (char*)malloc((tam + 1) * sizeof(char));
    sprintf(r->cidade,"%s", cidade);
    
    tam = 0;
    while(preco[tam] != '\0') tam++;
    r->faixa_preco = tam;
    
    tam = 0;
    while(tipo[tam] != '\0') tam++;
    for(int i = 0; tipo[i] != '\0'; i++)
        if(tipo[i] == ';') tipo[i] = ',';
        
    r->tipo_cozinha = (char**)malloc(1 * sizeof(char*));
    r->tipo_cozinha[0] = (char*)malloc((tam + 1) * sizeof(char));
    sprintf(r->tipo_cozinha[0],"%s",tipo);
    
    return r;
}

void formatar_restaurante(Restaurante* restaurante, char* buffer) {
    char hora_fechamento[7], hora_abertura[7], data_abertura[12], str_aberto[6];
    formatar_hora(&restaurante->hora_abertura, hora_abertura);
    formatar_hora(&restaurante->hora_fechamento, hora_fechamento);
    formatar_data(&restaurante->data_abertura, data_abertura);
    
    char f_preco[5];
    int i;
    for(i = 0; i < restaurante->faixa_preco; i++) f_preco[i] = '$';
    f_preco[i] = '\0';
    
    if(restaurante->aberto == true) sprintf(str_aberto, "true");
    else sprintf(str_aberto, "false");
    
    sprintf(buffer,"[%d ## %s ## %s ## %d ## %.1lf ## [%s] ## %s ## %s-%s ## %s ## %s]",
        restaurante->id_restaurante, restaurante->nome, restaurante->cidade,
        restaurante->capacidade, restaurante->avaliacao, restaurante->tipo_cozinha[0],
        f_preco, hora_abertura, hora_fechamento, data_abertura, str_aberto);
}

void ler_csv_colecao(Colecao_Restaurante* colecao, char* path) {
    FILE *arq = fopen(path, "r");
    if(arq == NULL) return;
    
    char linha[200];
    fgets(linha, sizeof(linha), arq);
    
    int i = 0;
    while(fgets(linha, sizeof(linha), arq) != NULL) {
        Restaurante* aux = parse_restaurante(linha);
        colecao->restaurante[i] = *aux;
        i++;
        free(aux);
    }
    fclose(arq);
}

Colecao_Restaurante* ler_csv() {
    FILE *arq = fopen("/tmp/restaurantes.csv", "r");
    if(arq == NULL) return NULL;
    
    int tam = 0;
    char linha[200];
    while(fgets(linha, sizeof(linha), arq) != NULL) tam++;
    fclose(arq);
    
    Colecao_Restaurante* novaCole = (Colecao_Restaurante*) malloc(sizeof(Colecao_Restaurante));
    novaCole->tamanho = tam - 1;
    novaCole->restaurante = (Restaurante*)malloc((tam - 1) * sizeof(Restaurante));
    
    ler_csv_colecao(novaCole,"/tmp/restaurantes.csv");
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
    for(qtdCaracteres=0; s[qtdCaracteres] != '\0' && s[qtdCaracteres] != '\n'; qtdCaracteres++);

    int contador = 1;
    int resposta = 0;
    for(int i=qtdCaracteres-1; i>=0; i--) {
        resposta += (s[i] - '0') * contador;
        contador *= 10;
    }
    return resposta;
}

void swap(Restaurante *i, Restaurante *j) {
   Restaurante temp = *i;
   *i = *j;
   *j = temp;
   mov += 3;
}

int maior_data(Restaurante* r1, Restaurante* r2) {
    Data d1 = r1->data_abertura;
    Data d2 = r2->data_abertura;

    if (d1.ano != d2.ano) return d1.ano > d2.ano;
    if (d1.mes != d2.mes) return d1.mes > d2.mes;
    if (d1.dia != d2.dia) return d1.dia > d2.dia;

    return strcmp(r1->nome, r2->nome) > 0; 
}

int get_maior_filho(Restaurante* rest, int i, int tamHeap) {
    comp++;
    if (2*i == tamHeap || maior_data(&rest[2*i], &rest[2*i+1]))
        return 2 * i;
    return 2 * i + 1;
}

void reconstruir(Restaurante* rest, int tamHeap) {
    int i = 1;
    while (i <= tamHeap / 2) {
        int filho = get_maior_filho(rest, i, tamHeap);
        comp++;
        if (maior_data(&rest[filho], &rest[i])) {
            swap(&rest[i], &rest[filho]);
            i = filho;
        } else {
            break;
        }
    }
}

void construir(Restaurante* rest, int tamHeap) {
    for (int i = tamHeap; i > 1 &&(comp++, maior_data(&rest[i], &rest[i/2])); i /= 2) {
        swap(&rest[i], &rest[i/2]);
    }
}

void heapsort(Restaurante* rest, int n) {
    Restaurante* tmp = (Restaurante*)malloc((n + 1) * sizeof(Restaurante));
    for (int i = 0; i < n; i++)
        tmp[i + 1] = rest[i];

    for (int tamHeap = 2; tamHeap <= k; tamHeap++)
        construir(tmp, tamHeap);
        
    for(int i = k + 1; i <= n; i++) {
        comp++;
        if(maior_data(&tmp[1], &tmp[i])) {
            swap(&tmp[i],&tmp[1]);
            reconstruir(tmp, k);
        }
    }

    int tamHeap = k;
    while (tamHeap > 1) {
        swap(&tmp[1], &tmp[tamHeap--]);
        reconstruir(tmp, tamHeap);
    }

    for (int i = 0; i < n; i++)
        rest[i] = tmp[i + 1];

    free(tmp);
}

int main() {
    clock_t inicio, fim;
    double total_tempo;

    Colecao_Restaurante* cr = ler_csv();
    if (cr == NULL) return 1;
    
    Restaurante r_ordenados[1000];
    int ordenados = 0;
    
    char linha[32];
    scanf("%s", linha);
    
    while(strcmp(linha, "-1") != 0) {
        int id = transformarInt(linha);
        int pos = buscarId(cr, id);
        
        if(pos != -1) {
           r_ordenados[ordenados] = cr->restaurante[pos];  
           ordenados++;
        }
        scanf("%s", linha);
    }
    
    inicio = clock();
    if(ordenados > 0) heapsort(r_ordenados, ordenados);
    fim = clock();
    total_tempo = ((fim - inicio) / (double)CLOCKS_PER_SEC) * 1000.0; 

    for(int i = 0; i < ordenados; i++) {
        char leitura[300];
        formatar_restaurante(&r_ordenados[i], leitura);
        printf("%s\n", leitura);
    }

    FILE* arq_log = fopen("890309_heapsort_parcial.txt", "w");
    if(arq_log != NULL) {
        fprintf(arq_log, "890309\t Comparacoes: %d\t Movimentos: %d\t Tempo: %.4lf\n", comp, mov, total_tempo);
        fclose(arq_log);
    }

    for (int i = 0; i < cr->tamanho; i++) {
        liberar_restaurante(&cr->restaurante[i]);
    }
    free(cr->restaurante);
    free(cr);

    return 0;
}