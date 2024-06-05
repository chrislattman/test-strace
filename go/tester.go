package main

import (
	"fmt"
	"os"
	"os/exec"
	"sync"
	"time"
)

var mutex sync.Mutex

func threadFunc(channel chan bool) {
	mutex.Lock()
	fmt.Println("Inside goroutine! Sleeping for 3 seconds...")
	time.Sleep(3 * time.Second)
	mutex.Unlock()
	channel <- true
}

func main() {
	fmt.Println("Parent PID =", os.Getpid())

	cmd := exec.Command("date")
	res, _ := cmd.Output()
	fmt.Print(string(res))
	fmt.Println("First child PID =", cmd.ProcessState.Pid())

	cmd = exec.Command("date")
	res, _ = cmd.Output()
	fmt.Print(string(res))
	fmt.Println("Second child PID =", cmd.ProcessState.Pid())

	t1 := make(chan bool, 1)
	t2 := make(chan bool, 1)
	go threadFunc(t1)
	go threadFunc(t2)
	time.Sleep(time.Second)
	fmt.Println("Hello from main thread!")
	<-t1
	<-t2
}
