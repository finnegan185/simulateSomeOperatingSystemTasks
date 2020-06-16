package operatingSystems;

public class Threading implements Runnable{
    String name;
    
	Threading(String name){
    	this.name = name;
    }
	
	public String getName() {
		return name;
	}
	public void run() {
        for(int i = 1; i < 6; i++) {
        	Thread.yield();
            System.out.println(getName() +
              " - iteration no. " + i);
            try {
            	Thread.currentThread().sleep(15);
            } catch(InterruptedException e) {}
            
        }
    }
}
