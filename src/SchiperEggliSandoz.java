import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class SchiperEggliSandoz extends UnicastRemoteObject implements SchiperEggliSandoz_RMI{
    
    private List<Message> messageBuffer;
    
    public SchiperEggliSandoz() throws RemoteException {
        super();
        messageBuffer = new LinkedList<Message>();
    }

    public void receive(String m, SBuffer s, int[] v)
    {
    	// Perform Receive
    }
}
