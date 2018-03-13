#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <time.h>
#include <sys/types.h>
#include <unistd.h>
#define MAX 10

extern unsigned int buffer[];
extern unsigned int count;
extern unsigned int fill;
extern unsigned int cons;
extern pthread_cond_t full;
extern pthread_cond_t empty;
extern pthread_mutex_t mutex;

void insert (int val) {
    buffer[fill] = val;
    fill = (fill + 1) % MAX;
    count++;
}

int get () {
    int tmp = buffer[cons];
    cons = (cons + 1) % MAX;
    count--;
    return tmp;
}

void *producer (void *args) {
    int loops = *(int *)args;
    for (int i = 0; i < loops; i++) {
        pthread_mutex_lock(&mutex);
        while (count == MAX){
	    printf("buffer is full.Producers stopped\n");
            pthread_cond_wait(&empty, &mutex);
	}
        insert(i);
        pthread_cond_signal(&full);
	printf("Producer No %ld has put %d @ position %d\n", pthread_self(), i, fill);
        pthread_mutex_unlock(&mutex);
    }
    pthread_exit(NULL);
}

void *consumer (void *args) {
    int loops = *(int *)args;
    for (int j = 0; j < loops; j++) {
        pthread_mutex_lock(&mutex);
        while (count == 0){
            printf("buffer is empty.Consumers stopped.\n");
	    pthread_cond_wait(&full, &mutex);
	}
	int t = get();
        pthread_cond_signal(&empty);
	printf("Consumer No %ld got %d @ position %d\n", pthread_self(), t, cons);
        pthread_mutex_unlock(&mutex);
    }
    pthread_exit(NULL);
}
