import java.util.List;


public class SBuffer {

	public static void insert(List<S> sBuffer, S newS)
	{
		boolean flag = false;
		
		// If pid is in list update its timestamp
		for (S bufferElement : sBuffer)
		{
			if(bufferElement.getPid() == newS.getPid())
			{
				bufferElement.setTimeStamp(newS.getTimeStamp());
				flag = true;
				break;
			}
		}
		
		// If pid not found in list add it to the list
		if (!flag)
		{
			sBuffer.add(newS);
		}
		
	}
	
	public static boolean deliveryCondition(List<S> sBufferMesg, S currProcess)
	{
		
		// There exists an (i,V) in Sm and V <= Vi
		for (S bufferElementMesg : sBufferMesg)
		{
			if (bufferElementMesg.getPid() == currProcess.getPid())
			{
				return VectorClock.strictlyLessThan(bufferElementMesg.getTimeStamp(),
									currProcess.getTimeStamp());
			}
		}
		
		// The message contains no knowledge of what should have been received
		return true;
	}

}
