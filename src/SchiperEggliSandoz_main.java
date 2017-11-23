import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;


public class SchiperEggliSandoz_main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int numProcesses = 3;
		Thread[] myThreads = new Thread[numProcesses];
		try{
			// Create Registry
			Registry registry = LocateRegistry.createRegistry(1099);

			int[][] destIDs = {{1,2}, {}, {1}};
			String[][] messages = {{"1", "2"}, {}, {"3"}};
			int[][] delays = {{5000, 0}, {}, {500}};

			for (int i = 0; i < numProcesses; i++)
			{
				SchiperEggliSandoz process = new SchiperEggliSandoz(i, numProcesses);
				MyProcess p = new MyProcess(process, destIDs[i], messages[i], delays[i]);
				myThreads[i] = new Thread(p);
			}
			for (int i = 0; i < numProcesses; i++)
			{
				myThreads[i].start();
			}
			
		} catch (Exception e) {
			System.err.println("Could not create registry exception: " + e.toString()); 
			e.printStackTrace(); 
		} 
		
	}

}

class MyProcess implements Runnable
{	
	int[] destIDs;
	String[] messages;
	SchiperEggliSandoz process;
	int[] delays;
	public MyProcess(SchiperEggliSandoz process, int[] destIDs, String[] messages, int[] delays) {
		this.messages = messages;
		this.destIDs = destIDs;
		this.process = process;
		this.delays = delays;
	}

	public void run() {
		for (int i = 0; i < destIDs.length; i++)
		{
			try
			{
			    if(process.pid == 2)
			        Thread.sleep(1000);
				process.send(destIDs[i], "SchiperEggliSandoz", messages[i], delays[i]);
			}
			catch (Exception e) {
				System.err.println("Client exception: " + e.toString()); 
				e.printStackTrace(); 
			} 
			
		}
	}
	
}
