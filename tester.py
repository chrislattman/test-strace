import os
import shutil
import subprocess
from threading import Lock, Thread
from time import sleep

mutex: Lock

def thread_func():
    global mutex
    mutex.acquire()
    print("Inside thread! Sleeping for 3 seconds...")
    sleep(3)
    mutex.release()

print("Parent PID = " + str(os.getpid()))

proc = subprocess.Popen([shutil.which("date")])
proc.wait()
print("First child PID = " + str(proc.pid))

proc = subprocess.Popen([shutil.which("date")])
proc.wait()
print("Second child PID = " + str(proc.pid))

mutex = Lock()
t1 = Thread(target=thread_func)
t2 = Thread(target=thread_func)
t1.start()
t2.start()
sleep(1)
print("Hello from main thread!")
t1.join()
t2.join()
