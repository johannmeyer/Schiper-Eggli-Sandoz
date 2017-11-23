import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SchiperEggliSandoz extends UnicastRemoteObject implements SchiperEggliSandoz_RMI{

    private List<Message> messageBuffer;
    private List<S> sBuffer;
    private int[] timeStamp;
    public int pid;

    public SchiperEggliSandoz(int pid, int numProcesses) throws RemoteException {
        super();
        messageBuffer = new LinkedList<Message>();
        sBuffer = new ArrayList<S>();
        timeStamp = new int[numProcesses];
        this.pid = pid;

        try{
        	// Binding the remote object (stub) in the local registry
        	Registry registry = LocateRegistry.getRegistry();

        	registry.rebind("SchiperEggliSandoz-" + pid, this);
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
    		println("Buffering message: " + m.toString());
    	    messageBuffer.add(m);
    	}
    }

    public synchronized void send(int destinationID, String destination, String message) throws MalformedURLException, RemoteException, NotBoundException {

        SchiperEggliSandoz_RMI dest = (SchiperEggliSandoz_RMI) Naming.lookup(destination + "-" + destinationID);

        timeStamp[pid]++;
        List<S> copy = new ArrayList<S>();
        for(S element : sBuffer) {
            copy.add(element.clone());
        }
        
        Message messageObject = new Message(message, copy, Arrays.copyOf(timeStamp, timeStamp.length));

        println("Sending - " + messageObject.toString());
        int wait = (int) (Math.random()*10000);

        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            dest.receive(messageObject);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                },
                wait
        );
        
        SBuffer.insert(sBuffer, new S(destinationID, Arrays.copyOf(timeStamp, timeStamp.length)));
    }
    
    public synchronized void send(int destinationID, String destination, String message, int delay) throws MalformedURLException, RemoteException, NotBoundException {

        SchiperEggliSandoz_RMI dest = (SchiperEggliSandoz_RMI) Naming.lookup(destination + "-" + destinationID);

        // New operation
        timeStamp[pid]++;
        List<S> copy = new ArrayList<S>();
        for(S element : sBuffer) {
            copy.add(element.clone());
        }
        
        Message messageObject = new Message(message, copy, Arrays.copyOf(timeStamp, timeStamp.length));

        println("Sending - " + messageObject.toString());

        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            dest.receive(messageObject);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                },
                delay
        );
               
        SBuffer.insert(sBuffer, new S(destinationID, Arrays.copyOf(timeStamp, timeStamp.length)));
    }

    /**
     * Checks the buffer for messages that can be delivered.
     */
    private synchronized void checkBuffer() {
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

    private synchronized void deliver(Message m) {
    	// Update knowledge of what should have occurred
        sBuffer = SBuffer.merge(sBuffer, m.getsBuffer());
        println("Delivering - " + m.toString());
        // Merge Vector Clocks
        this.timeStamp = VectorClock.max(this.timeStamp, m.getTimeStamp());

        // Increment clock for current process
        this.timeStamp[pid]++;
    }

    private void println(String message)
    {
    	String pidStr = "(" + this.pid + ") ";
    	System.err.println(pidStr + message);
    }

}
