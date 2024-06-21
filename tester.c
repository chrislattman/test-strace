#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <pthread.h>

pthread_mutex_t mutex;

void *thread_func(void *args)
{
    pthread_mutex_lock(&mutex);
    printf("Inside thread! Sleeping for 3 seconds...\n");
    sleep(3);
    pthread_mutex_unlock(&mutex);
    return NULL;
}

int main(void)
{
    printf("Parent PID = %d\n", getpid());

    switch (fork()) {
    case 0:
        execlp("date", "date", (char *) 0);
    default:
        pid_t child = wait(NULL);
        printf("First child PID = %d\n", child);
    }

    switch (fork()) {
    case 0:
        execlp("date", "date", (char *) 0);
    default:
        pid_t child = wait(NULL);
        printf("Second child PID = %d\n", child);
    }

    pthread_t t1, t2;
    pthread_mutex_init(&mutex, NULL);
    pthread_create(&t1, NULL, thread_func, NULL);
    pthread_create(&t2, NULL, thread_func, NULL);
    sleep(1);
    printf("Hello from main thread!\n");
    pthread_join(t1, NULL);
    pthread_join(t2, NULL);

    return 0;
}
