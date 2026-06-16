#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>

typedef struct Data{
    int dia;
    int mes;
    int ano;
}Data;

typedef struct Hora{
    int hora;
    int minuto;
}Hora;

typedef struct Restaurante{
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
}Restaurante;

typedef struct ColecaoRestaurante{
    int tamanho;
    Restaurante* restaurante;
}Colecao_Restaurante;

typedef struct Celula {
    Restaurante* restaurante;
    struct Celula* prox;
} Celula;

Celula* primeiro = NULL;
Celula* ultimo = NULL;

void inicializar_lista() {
    primeiro = (Celula*)malloc(sizeof(Celula));
    primeiro->restaurante = NULL;
    primeiro->prox = NULL;
    ultimo = primeiro;
}

void inserir_fim(Restaurante* r) {
    Celula* nova = (Celula*)malloc(sizeof(Celula));
    nova->restaurante = r;
    nova->prox = NULL;
    ultimo->prox = nova;
    ultimo = nova;
}

void liberar_lista() {
    Celula* atual = primeiro;
    while (atual != NULL) {
        Celula* prox = atual->prox;
        free(atual);
        atual = prox;
    }
}

Data parse_data(char *s){
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}

void formatar_data(Data* data, char* buffer){
    sprintf(buffer,"%02d/%02d/%04d", data->dia, data->mes, data->ano);
}

Hora parse_hora(char *s){
    Hora h;
    sscanf(s,"%d:%d", &h.hora, &h.minuto);
    return h;
}

void formatar_hora(Hora* hora, char* buffer){
    sprintf(buffer, "%02d:%02d", hora->hora, hora->minuto);
}

void liberar_restaurante(Restaurante* r) {
    free(r->nome);
    free(r->cidade);
    free(r->tipo_cozinha[0]);
    free(r->tipo_cozinha);
}

Restaurante* parse_restaurante(char *s){
    Restaurante* r = (Restaurante*)malloc(sizeof(Restaurante));
    if(r == NULL){
        printf("Nao foi possivel criar restaurante!");
        return NULL;
    }
    char hora_a[6], hora_f[6], data_a[11], nome[100], cidade[100], preco[10], tipo[40], aberto[10];

    sscanf(s, "%d,%[^,],%[^,],%d,%lf,%[^,],%[^,],%[^-]-%[^,],%[^,],%[^\n]",
           &r->id_restaurante, nome, cidade, &r->capacidade,
           &r->avaliacao, tipo, preco, hora_a, hora_f,
           data_a, aberto);
    
    for(int i = 0; aberto[i] != '\0'; i++){
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

void formatar_restaurante(Restaurante* restaurante, char* buffer){
    char hora_fechamento[7], hora_abertura[7], data_abertura[12], str_aberto[6];

    formatar_hora(&restaurante->hora_abertura, hora_abertura);
    formatar_hora(&restaurante->hora_fechamento, hora_fechamento);
    formatar_data(&restaurante->data_abertura, data_abertura);
  
    char f_preco[5];
    int i;
    for(i = 0; i < restaurante->faixa_preco; i++){
        f_preco[i] = '$';
    }
    f_preco[i] = '\0';

    if(restaurante->aberto == true) {
        sprintf(str_aberto, "true");
    }else{
        sprintf(str_aberto, "false");
    }
    sprintf(buffer,"[%d ## %s ## %s ## %d ## %.1lf ## [%s] ## %s ## %s-%s ## %s ## %s]",
        restaurante->id_restaurante, restaurante->nome, restaurante->cidade,
        restaurante->capacidade, restaurante->avaliacao, restaurante->tipo_cozinha[0],
        f_preco, hora_abertura, hora_fechamento, data_abertura, str_aberto);
}

void ler_csv_colecao(Colecao_Restaurante* colecao, char* path){
    FILE *arq = fopen(path, "r");
    if(arq == NULL){
        printf("Erro ao abrir arquivo!");
        return;
    }
    char linha[200];
    fgets(linha, sizeof(linha), arq);
    
    int i = 0;
    while(fgets(linha, sizeof(linha), arq) != NULL){
        Restaurante* aux = parse_restaurante(linha);
        colecao->restaurante[i] = *aux; 
        i++;
        free(aux);
    }
    fclose(arq);
}

Colecao_Restaurante* ler_csv(){
    FILE *arq = fopen("/tmp/restaurantes.csv", "r");
    if(arq == NULL){
        printf("Erro ao abrir arquivo!");
        return NULL;
    }

    int tam = 0;
    char linha[200];
    while(fgets(linha, sizeof(linha), arq) != NULL){
        tam++;
    }
    fclose(arq);

    Colecao_Restaurante* novaCole = (Colecao_Restaurante*) malloc(sizeof(Colecao_Restaurante));
    if(novaCole == NULL){
        printf("Erro ao alocar Colecao!");
        return NULL;
    }
    novaCole->tamanho = tam - 1;
    novaCole->restaurante = (Restaurante*)malloc((tam - 1) * sizeof(Restaurante));
    if(novaCole->restaurante == NULL){
        printf("Erro ao alocar restaurante!");
        return NULL;
    }
    ler_csv_colecao(novaCole,"/tmp/restaurantes.csv");

    return novaCole;
}

int buscar_id(Colecao_Restaurante* colecao, int id_buscado) {
    for (int i = 0; i < colecao->tamanho; i++) {
        if (colecao->restaurante[i].id_restaurante == id_buscado) {
            return i; 
        }
    }
    return -1;
}

int transformarInt(char *s){
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

void swap_celula(Celula* i, Celula* j, int* mov) {
    Restaurante* temp = i->restaurante;
    i->restaurante = j->restaurante;
    j->restaurante = temp;
    (*mov) += 3;
}

void SelecaoLista(Celula* cabeca, int* comp, int* mov) {
    for (Celula* i = cabeca->prox; i != NULL && i->prox != NULL; i = i->prox) {
        Celula* menor = i;
        for (Celula* j = i->prox; j != NULL; j = j->prox) {
            (*comp)++;
            if (strcmp(j->restaurante->nome, menor->restaurante->nome) < 0) {
                menor = j;
            }
        }
        if (menor != i) {
            swap_celula(i, menor, mov);
        }
    }
}

int main(){
    clock_t inicio, fim;
    double total_tempo;

    Colecao_Restaurante* cr = ler_csv();
    inicializar_lista();

    int comp = 0, mov = 0;
    char linha[5];
    scanf("%s", linha);
    
    while(strcmp(linha, "-1") != 0){
        int id = transformarInt(linha);
        int id_buscado = buscar_id(cr, id);
        if(id_buscado != -1){
            inserir_fim(&cr->restaurante[id_buscado]);  
        }
        scanf("%s", linha);
    }
    
    inicio = clock();
    SelecaoLista(primeiro, &comp, &mov);
    fim = clock();
    total_tempo = ((fim - inicio) / (double)CLOCKS_PER_SEC) * 1000.0; 

    for(Celula* i = primeiro->prox; i != NULL; i = i->prox) {
        char leitura[300];
        formatar_restaurante(i->restaurante, leitura);
        printf("%s\n", leitura);
    }

    FILE* arq_log = fopen("890309_selecao.txt", "w");
    if(arq_log != NULL){
        fprintf(arq_log, "890309\t Comparacoes: %d\t Movimentos: %d\t Tempo: %.4lf\n", comp, mov, total_tempo);
        fclose(arq_log);
    }

    liberar_lista();

    for (int i = 0; i < cr->tamanho; i++) {
        liberar_restaurante(&cr->restaurante[i]);
    }
    free(cr->restaurante);
    free(cr);
    
    return 0;
}