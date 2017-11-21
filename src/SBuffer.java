import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class SBuffer{

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
				return VectorClock.lessThanEqualTo(bufferElementMesg.getTimeStamp(),
									currProcess.getTimeStamp());
			}
		}
		
		// The message contains no knowledge of what should have been received
		return true;
	}

	public static List<S> merge(List<S> ownBuffer, List<S> incomingBuffer) {
	    List<S> resultBuffer = new LinkedList<S>();
	    
       for(int i = 0; i < incomingBuffer.size(); i++) {
           boolean found = false;
           S incomingS = incomingBuffer.get(i);
           
           for(int j = 0; j < ownBuffer.size(); i++) {
               S ownS = ownBuffer.get(i);
               if(incomingS.getPid() == ownS.getPid()) {
                   found = true;
                   int[] maxTimeStamp = VectorClock.max(incomingS.getTimeStamp(), ownS.getTimeStamp());
                   resultBuffer.add(new S(ownS.getPid(), maxTimeStamp));
               }
           }
           
           if(!found) {
               resultBuffer.add(incomingBuffer.get(i));
           }
       }
       
       return resultBuffer;
	}
	
	public static String toString(List<S> sBuffer)
	{
		return Arrays.toString(sBuffer.toArray());
	}
}
