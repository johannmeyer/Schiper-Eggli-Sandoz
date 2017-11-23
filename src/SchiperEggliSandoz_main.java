import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;


public class SchiperEggliSandoz_main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// args[0] = numProcesses		
	
//		int numProcesses = Integer.parseInt(args[0]);
		int numProcesses = 3;
		Thread[] myThreads = new Thread[numProcesses];
		try{
			// Create Registry
			Registry registry = LocateRegistry.createRegistry(1099);


			int[][] destIDs = {{1,2}, {}, {1}};
			String[][] messages = {{"1", "2"}, {}, {"3"}};
			for (int i = 0; i < numProcesses; i++)
			{
				SchiperEggliSandoz process = new SchiperEggliSandoz(i, numProcesses);
				MyProcess p = new MyProcess(process, destIDs[i], messages[i]);
				myThreads[i] = new Thread(p);
			}
			for (int i = 0; i < numProcesses; i++)
			{
				myThreads[i].start();
//				Thread.sleep(3000);
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
	public MyProcess(SchiperEggliSandoz process, int[] destIDs, String[] messages) {
		this.messages = messages;
		this.destIDs = destIDs;
		this.process = process;
	}

	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < destIDs.length; i++)
		{
			try
			{
			    if(process.pid == 2)
			        Thread.sleep(1000);
				process.send(destIDs[i], "SchiperEggliSandoz", messages[i]);
			}
			catch (Exception e) {
				System.err.println("Client exception: " + e.toString()); 
				e.printStackTrace(); 
			} 
			
		}
	}
	
}
