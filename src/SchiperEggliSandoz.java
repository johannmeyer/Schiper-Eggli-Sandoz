import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SchiperEggliSandoz extends UnicastRemoteObject implements SchiperEggliSandoz_RMI{

    private List<Message> messageBuffer;
    private List<S> sBuffer;
    private int[] timeStamp;
    public int pid; //TODO make private and use getter

    public SchiperEggliSandoz(int pid, int numProcesses) throws RemoteException {
        super();
        messageBuffer = new LinkedList<Message>();
        sBuffer = new LinkedList<S>();
        timeStamp = new int[numProcesses];
        this.pid = pid;

        try{
        	// Binding the remote object (stub) in the local registry
        	Registry registry = LocateRegistry.getRegistry();

        	registry.bind("SchiperEggliSandoz-" + pid, this);
//        	for(String str : registry.list())
//        		System.err.println(str);
        	System.err.println("Process " + pid + " ready");
        } catch (Exception e) {
        	System.err.println("Server exception: " + e.toString());
        	e.printStackTrace();
     }
    }

    /**
     * Receives a messages. If deliver requirement is met, message is delivered.
     * Otherwise, it is added to the buffer.
     */
    public synchronized void receive(Message m)
    {
    	if (SBuffer.deliveryCondition(m.getsBuffer(), new S(pid, timeStamp))) {
    	    deliver(m);
    	    checkBuffer();
    	} else {
    		println("Current Vector Clock: " + VectorClock.toString(this.timeStamp));
    		println("Buffering message: " + SBuffer.toString(m.getsBuffer()));
    	    messageBuffer.add(m);
    	}
    }

    public void send(int destinationID, String destination, String message) throws MalformedURLException, RemoteException, NotBoundException {

        SchiperEggliSandoz_RMI dest = (SchiperEggliSandoz_RMI) Naming.lookup(destination + "-" + destinationID);
        // Make a copy for inserting into the SBuffer
        // int[] oldTimeStamp = Arrays.copyOf(timeStamp, timeStamp.length);

        // New operation
        timeStamp[pid]++;

        Message messageObject = new Message(message, sBuffer, timeStamp);

        println("Sending - " + messageObject.toString());
        dest.receive(messageObject);
//        println("send sBuffer before: " + SBuffer.toString(sBuffer));
        SBuffer.insert(sBuffer, new S(destinationID, Arrays.copyOf(timeStamp, timeStamp.length)));
//        println("send sBuffer after: " + SBuffer.toString(sBuffer));
    }

    /**
     * Checks the buffer for messages that can be delivered.
     */
    private void checkBuffer() {
//    	println("Checking buffer");
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
    	// Update knowledge of what should have occurred
        sBuffer = SBuffer.merge(sBuffer, m.getsBuffer());
        println("Delivering - " + m.toString());
        println("Old Timestamp: " + VectorClock.toString(this.timeStamp));
        // Merge Vector Clocks
        this.timeStamp = VectorClock.max(this.timeStamp, m.getTimeStamp());

        // Increment clock for current process
        this.timeStamp[pid]++;
        println("New Timestamp: " + VectorClock.toString(this.timeStamp));
    }

    private void println(String message)
    {
    	String pidStr = "(" + this.pid + ") ";
    	System.err.println(pidStr + message);
    }

}
