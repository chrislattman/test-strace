use std::{process, sync, thread, time};

static MUTEX: sync::Mutex<u64> = sync::Mutex::new(0);

fn thread_func() {
    {
        let _counter = MUTEX.lock().unwrap();
        println!("Inside thread! Sleeping for 3 seconds...");
        thread::sleep(time::Duration::from_secs(3));
    }
}

fn main() {
    println!("Parent PID: {}", process::id());

    let mut cmd = process::Command::new("date");
    let mut child = cmd.spawn().unwrap();
    let pid = child.id();
    child.wait().unwrap();
    println!("First child PID = {}", pid);

    let mut cmd = process::Command::new("date");
    let mut child = cmd.spawn().unwrap();
    let pid = child.id();
    child.wait().unwrap();
    println!("Second child PID = {}", pid);

    let t1 = thread::spawn(|| {
        thread_func();
    });
    let t2 = thread::spawn(|| {
        thread_func();
    });
    thread::sleep(time::Duration::from_secs(1));
    println!("Hello from main thread!");
    t1.join().unwrap();
    t2.join().unwrap();
}
