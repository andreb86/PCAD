#include "prod_cons.h"

unsigned int buffer[MAX];
unsigned int count = 0;
unsigned int fill = 0;
unsigned int cons = 0;
unsigned int loops;
pthread_cond_t full = PTHREAD_COND_INITIALIZER;
pthread_attr_t attr;
pthread_cond_t empty = PTHREAD_COND_INITIALIZER;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void help(void) {
    printf("usage: ProdCons -p [nuber of producers] -c [number of consumers] -l [loops]\n");
    exit(1);
}

int main (int argc, char **argv) {
    pthread_attr_init(&attr);
    pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);
    if (argc != 7) {
	help();
    }
    int p, c, loops;
    
    for (int i = 1; i < argc - 1; i++) {
	    if (!strcmp(argv[i], "-p")) {
	        p = atoi(argv[++i]);
	    } else if (!strcmp(argv[i], "-c")) {
	        c = atoi(argv[++i]);
	    } else if (!strcmp(argv[i], "-l")) {
	        loops = atoi(argv[++i]);
	    } else {
	        help();
	    }
    }
    
    pthread_t producers[p];
    pthread_t consumers[c];
    for (int pp = 0; pp < p; pp++) 
        pthread_create(&producers[pp], &attr, producer, &loops);
    
    for (int cc = 0; cc < c; cc++) 
        pthread_create(&consumers[cc], &attr, consumer, &loops);
    
    for (int pp = 0; pp < p; pp++) 
        pthread_join(producers[pp], NULL);
    
    for (int cc = 0; cc < c; cc++) 
    pthread_join(consumers[cc], NULL);

    pthread_mutex_destroy(&mutex);
    pthread_cond_destroy(&full);
    pthread_cond_destroy(&empty);
    return 0;
}
