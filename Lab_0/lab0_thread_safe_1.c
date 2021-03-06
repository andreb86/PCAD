#include <assert.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

static volatile int flag1 = 0;
static volatile int flag2 = 0;
static volatile int turn = 1;
static volatile int gSharedCounter = 0, counter1 = 0, counter2 = 0;

/*

The keyword volatile prevents an optimizing compiler from optimizing away subsequent reads or writes and thus incorrectly reusing a stale value or omitting writes. Volatile values primarily arise in hardware access (memory-mapped I/O), where reading from or writing to memory is used to communicate with peripheral devices, and in threading, where a different thread may have modified a value.

Despite being a common keyword, the behavior of volatile differs significantly between programming languages, and is easily misunderstood. In C and C++, it is a type qualifier, like const, and is a property of the type. Furthermore, in C and C++ it does not work in most threading scenarios, and that use is discouraged.

*/

int gloopCount;

void proc1() {
    // Critical section -> acquire mutex lock
    counter1++;
    // Let the other task run -> release mutex lock
}  

void proc2() {
    // Critical section
    counter2++;
    // Let the other task run
}

//
// Tasks, as a level of indirection
//
void *task1(void *arg) {
    int i;
    printf("Starting task1\n");
    for(i=0;i<gloopCount;i++) proc1();
    pthread_exit(NULL);
}

void *task2(void *arg) {
    int i;
    printf("Starting task2\n");
    for(i=0;i<gloopCount;i++) proc2();
    pthread_exit(NULL);
}

int main(int argc, char ** argv)
{
    int loopCount = 0;
    pthread_t my_thread_1;
    pthread_t my_thread_2;
    void * returnCode;
    int result;
    int expected_sum;

    /* Check arguments to program*/
    if(argc != 2)  {
        fprintf(stderr, "USAGE: %s <loopcount>\n", argv[0]); 
        exit(1);
    }

    /* Parse argument */
    loopCount = atoi(argv[1]);
    gloopCount = loopCount;
    expected_sum = 2*gloopCount;

    /* Start the threads */
    result = pthread_create(&my_thread_1, NULL, task1, NULL);
    result = pthread_create(&my_thread_2, NULL, task2, NULL);

    /* Wait for the threads to end */
    result = pthread_join(my_thread_1,&returnCode);
    result = pthread_join(my_thread_2,&returnCode); 
    printf("Both threads terminated\n");

    gSharedCounter = counter1 + counter2;

    /* Check result */
    if( gSharedCounter != expected_sum ) {
        printf("[-] Sum %d rather than %d.\n", gSharedCounter, expected_sum);
        return 1;
    } else {
        printf("[+] Ok\n");
        return 0;
    }
}
