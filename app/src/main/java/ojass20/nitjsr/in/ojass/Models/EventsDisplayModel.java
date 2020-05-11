package ojass20.nitjsr.in.ojass.Models;

public class EventsDisplayModel {
    public int evImg;
    public String evName;
    public float alphaVal;
    public EventsDisplayModel(int evImg, String evName,float alphaVal) {
        this.evImg = evImg;
        this.evName = evName;
        this.alphaVal = alphaVal;
    }

    public float getAlphaVal() {
        return alphaVal;
    }

    public int getEvImg() {
        return evImg;
    }

    public String getEvName() {
        return evName;
    }

    public void setEvImg(int evImg) {
        this.evImg = evImg;
    }

    public void setEvName(String evName) {
        this.evName = evName;
    }

    public void setAlphaVal(float alphaVal) {
        this.alphaVal = alphaVal;
    }
}
