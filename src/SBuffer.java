
public class SBuffer {
	int pid;
	int[] timeStamp;
	
	public SBuffer(int numProcesses)
	{
		timeStamp = new int[numProcesses];		
	}
	
	public int[] getTimeStamp()
	{
		return timeStamp;
	}
	
	public int getPid()
	{
		return pid;
	}

	public void insert(int pid, int[] timeStamp)
	{
		//TODO add insertion algorithm
	}
	
	public static int compare(SBuffer s1, SBuffer s2)
	{
		// Vector comparison
//		for (int i = 0; i < s1.length)
		return 0;
	}

}
