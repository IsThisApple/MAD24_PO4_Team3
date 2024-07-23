package sg.edu.np.mad.nearbuy;

public class Message {
    private String text;
    private String sender;

    public Message() {
        // default constructor required for firebase
    }

    public Message(String text, String sender) {
        this.text = text;
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }

}