package sg.edu.np.mad.nearbuy;

public class Message {
    private String text;
    private String sender;

    public Message() {
        // Default constructor required for Firebase
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