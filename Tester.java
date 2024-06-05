import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.locks.ReentrantLock;

public class Tester {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Parent PID = " + ProcessHandle.current().pid());

        Process process = Runtime.getRuntime().exec("date");
        BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
        System.out.println(output.readLine());
        output.close();
        process.waitFor();
        System.out.println("First child PID = " + process.pid());

        process = Runtime.getRuntime().exec("date");
        output = new BufferedReader(new InputStreamReader(process.getInputStream()));
        System.out.println(output.readLine());
        output.close();
        process.waitFor();
        System.out.println("Second child PID = " + process.pid());

        ReentrantLock mutex = new ReentrantLock();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mutex.lock();
                System.out.println("Inside thread! Sleeping for 3 seconds...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mutex.unlock();
            }
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        Thread.sleep(1000);
        System.out.println("Hello from main thread!");
        t1.join();
        t2.join();
    }
}
