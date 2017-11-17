
public class Message {

    private String message;
    private SBuffer sBuffer;
    private int[] timeStamp;
    
    public Message(String message, SBuffer sBuffer, int[] timeStamp) {
        this.message = message;
        this.sBuffer = sBuffer;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SBuffer getsBuffer() {
        return sBuffer;
    }

    public void setsBuffer(SBuffer sBuffer) {
        this.sBuffer = sBuffer;
    }

    public int[] getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int[] timeStamp) {
        this.timeStamp = timeStamp;
    }
    
}
