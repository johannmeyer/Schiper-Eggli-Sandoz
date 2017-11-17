import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class SchiperEggliSandoz extends UnicastRemoteObject implements SchiperEggliSandoz_RMI{
    
    private List<Message> messageBuffer;
    private List<S> sBuffer;
    private int[] timeStamp;
    private int pid;
    
    public SchiperEggliSandoz(int pid, int numProcesses) throws RemoteException {
        super();
        messageBuffer = new LinkedList<Message>();
        sBuffer = new LinkedList<S>();
        timeStamp = new int[numProcesses];
        this.pid = pid;
    }

    /**
     * Receives a messages. If deliver requirement is met, message is delivered.
     * Otherwise, it is added to the buffer.
     */
    public void receive(Message m)
    {
    	if (SBuffer.deliveryCondition(m.getsBuffer(), new S(pid, timeStamp))) {
    	    deliver(m);
    	    checkBuffer();
    	} else {
    	    messageBuffer.add(m);
    	}
    }
    
    /**
     * Checks the buffer for messages that can be delivered.
     */
    private void checkBuffer() {
        for(int i = 0; i < messageBuffer.size(); i++) {
            Message m = messageBuffer.get(i);
            if (SBuffer.deliveryCondition(m.getsBuffer(), new S(pid, timeStamp))) {
                deliver(m);
                messageBuffer.remove(i);
                checkBuffer();
                break;
            }
        }
    }
    
    private void deliver(Message m) {
        sBuffer = SBuffer.merge(sBuffer, m.getsBuffer());
    }
}
