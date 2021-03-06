#include <assert.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>
#include <signal.h>
#include <sched.h>
#define MAXP 2

// Define the shared data structure for the car park
typedef struct __monitor_t {
    unsigned int available;
    unsigned int queueP;
    unsigned int queueS;
    unsigned int k; //number of rounds
    pthread_mutex_t gate;
    pthread_attr_t attr;
    pthread_cond_t greenLightPrimary;
    pthread_cond_t greenLightSecondary;
    pthread_cond_t closed;
} monitor_t;

/*
The shared global variable valet must be defined as a monitor type in the main
source
 */
extern monitor_t *valet;

int choseEntrance (void) {
    srand(time(NULL)); // initialise the random number generator
    return rand() % 2; // return 0 (primary entrance) or 1 (secondary entrance)
}

void primaryEntrance (int threadID) {
    pthread_mutex_lock(&valet->gate);
    valet->queueP++;
    while (!valet->available) {
        printf("\n\nCar No. %d is queueing at P access\nP access queue: %d\n",
               threadID, valet->queueP);
        pthread_cond_wait(&valet->greenLightPrimary, &valet->gate);
    }
    valet->queueP--;
    printf("\n\nCar No. %d is entering the park from P access\n%d slots left available\n",
           threadID, --valet->available);
    pthread_mutex_unlock(&valet->gate);
}

void secondaryEntrance (int threadID) {
    pthread_mutex_lock(&valet->gate);
    valet->queueS++;
	while(!valet->available) {
        printf("\n\nCar No. %d is queueing at S access\nS access queue: %d\n",
               threadID, valet->queueS);
        pthread_cond_wait(&valet->greenLightSecondary, &valet->gate);
    }
    valet->queueS--;
    printf("\n\nCar No. %d is entering the park from S access\n%d slots left available\n",
           threadID, --valet->available);
    pthread_mutex_unlock(&valet->gate);
}

void leaveThePark (int threadID) {
    pthread_mutex_lock(&valet->gate);
    printf("\n\nCar No. %d is leaving the car park\nThere are %d slots left avaialble\n",
	   threadID, ++valet->available);
    pthread_cond_signal(&valet->greenLightPrimary);
    srand(time(NULL) + threadID);
    sleep(rand() % 3);
    pthread_cond_signal(&valet->greenLightSecondary);
    pthread_mutex_unlock(&valet->gate);
}

void *car (void *t) {
    int threadID = *(int *)t;
    void (*enterThePark)(int);
    int ent = choseEntrance();
	  if (ent) {
	      enterThePark = secondaryEntrance;
	  } else {
	      enterThePark = primaryEntrance;
	  }
	  (enterThePark)(threadID);
	  sleep(rand() % 10);
	  leaveThePark(threadID);
    pthread_exit(NULL);
}

void openPark (monitor_t *v) {
    pthread_mutex_init(&v->gate, NULL);
    pthread_attr_init(&v->attr);
    pthread_attr_setdetachstate(&v->attr, PTHREAD_CREATE_JOINABLE);
    pthread_cond_init(&v->greenLightPrimary, NULL);
    pthread_cond_init(&v->greenLightSecondary, NULL);
    pthread_cond_init(&v->closed, NULL);
    v->available = MAXP;
    v->queueP = 0;
    v->queueS = 0;
}

void closePark (monitor_t *v) {
    pthread_cond_destroy(&v->greenLightPrimary);
    pthread_cond_destroy(&v->greenLightSecondary);
    pthread_cond_destroy(&v->closed);
    pthread_attr_destroy(&v->attr);
    pthread_mutex_destroy(&v->gate);
}
