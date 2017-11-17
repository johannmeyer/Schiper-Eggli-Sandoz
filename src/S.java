
public class S {

	int pid;
	int[] timeStamp;
	
	public S(int pid, int[] timeStamp)
	{
		this.pid = pid;
		this.timeStamp = timeStamp;
	}
	
	public int[] getTimeStamp()
	{
		return timeStamp;
	}
	
	public int getPid()
	{
		return pid;
	}
	
	public void setPid(int pid)
	{
		this.pid = pid;
	}
	
	public void setTimeStamp(int[] timeStamp)
	{
		this.timeStamp = timeStamp;
	}
}
