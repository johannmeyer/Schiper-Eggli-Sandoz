import java.rmi.Remote;


public interface SchiperEggliSandoz_RMI extends Remote{
	public void receive(String m, SBuffer s, int[] v);
}
