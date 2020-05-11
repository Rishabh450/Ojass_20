package ojass20.nitjsr.in.ojass.Models;

public class NotificationModal {
    public String ques,ans,event;
    public boolean isExplandable=false;

    public NotificationModal(){}
    public NotificationModal(String ques, String ans, String event) {
        this.ques = ques;
        this.ans = ans;
        isExplandable = false;
        this.event = event;
    }

    public void setExplandable(boolean explandable) {
        isExplandable = explandable;
    }

    public String getEvent() {
        return event;
    }

    public String getQues() {
        return ques;
    }

    public String getAns() {
        return ans;
    }

    public boolean isExplandable() {
        return isExplandable;
    }
}
