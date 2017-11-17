
public class Message {

    private String message;
    private SBuffer S;
    private int[] timeStamp;
    
    public Message(String message, SBuffer S, int[] timeStamp) {
        this.message = message;
        this.S = S;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SBuffer getS() {
        return S;
    }

    public void setS(SBuffer s) {
        S = s;
    }

    public int[] getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int[] timeStamp) {
        this.timeStamp = timeStamp;
    }
    
}
