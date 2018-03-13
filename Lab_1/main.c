#include "park.h"

// Monitor must be initialised to a constant value in global scope
monitor_t *valet = NULL;

void help(void) {
    printf("usage: park -n [nuber of cars] -k [number of attempts]\n");
    exit(1);
}

int main(int argc, char **argv) {

    // Initialise the car park valet (monitor)
    valet = (monitor_t *) malloc(sizeof(monitor_t));
    openPark(valet);
    
    // Check the input and parse it
    if (argc != 5) {
        help();
    }
    int n;
    for (int i = 1; i < argc - 1; i++) {
        if (!strcmp(argv[i], "-n")) {
            n = atoi(argv[++i]);
        } else if (!strcmp(argv[i], "-k")) {
            valet->k = atoi(argv[++i]);
        } else {
            help();
        }
    }

    int args[n];

    printf("Welcome to the Car Park!\nWe have %d slots available\n", valet->available);

    // Start the threads
    for (int kk = 0; kk < valet->k; ++kk) {
        printf("-------- ROUND %d  --------", kk);
        pthread_t cars[n];
        for (int j = 0; j < n; j++) {
            args[j] = j;
            srand(time(NULL) + j);
            sleep(rand() % 3);
            pthread_create(&cars[j], &valet->attr, car, &args[j]);
        }
        /* If threads are joinable join them, otherwise (i.e. detached) let them
           release resources automatically
        */
        int check_thread_state;
        int rc = pthread_attr_getdetachstate(&valet->attr, &check_thread_state);
        if (check_thread_state == PTHREAD_CREATE_JOINABLE) {
            for (int l = 0; l < n; l++) pthread_join(cars[l], NULL);
        }
    }


    closePark(valet);

    free(valet);
    return 0;
}
