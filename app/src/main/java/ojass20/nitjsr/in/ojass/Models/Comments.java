package ojass20.nitjsr.in.ojass.Models;

public class Comments {
    public String senderId;
    public String message;
    public String time;
    public String senderImageUrl;
    public String senderUserName;

    public Comments(){

    }
    public Comments(String senderId, String message,String time,String senderUserName, String senderImageUrl) {
        this.senderId = senderId;
        this.message = message;
        this.time=time;
        this.senderImageUrl=senderImageUrl;
        this.senderUserName=senderUserName;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSenderImageUrl(String senderImageUrl) {
        this.senderImageUrl = senderImageUrl;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getSenderImageUrl() {
        return senderImageUrl;
    }

    public String getSenderUserName() {
        return senderUserName;
    }
}
