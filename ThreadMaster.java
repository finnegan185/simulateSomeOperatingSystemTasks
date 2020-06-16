package operatingSystems;

public class ThreadMaster {
	public static void main(String[] args) {
        Thread firstThread = new Thread(new Threading("Thread 1"));
        Thread secondThread = new Thread(new Threading("Thread 2"));
        Thread thirdThread = new Thread(new Threading("Thread 3"));
        firstThread.start();
        try {Thread.currentThread().sleep(5);
        } catch(InterruptedException e) {}
        secondThread.start();
        try {Thread.currentThread().sleep(5);
        } catch(InterruptedException e) {}
        thirdThread.start();
	}
}
