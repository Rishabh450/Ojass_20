package ojass20.nitjsr.in.ojass.Models;

public class HomePage {
    public String mTitle;
    public  int mImageId;
    public String mCircleColor;
    public int mIndex;
    public  int mAngle;
    public  int mImageSpecificId;
    public  int mBackground;

    public HomePage(String mTitle, String mCircleColor, int mIndex, int mImageId, int angle, int mImageSpecificId, int mBackground) {
        this.mTitle = mTitle;
        this.mImageId = mImageId;
        this.mCircleColor = mCircleColor;
        this.mIndex = mIndex;
        mAngle = angle;
        this.mImageSpecificId = mImageSpecificId;
        this.mBackground = mBackground;
    }

    public String getmTitle() {
        return mTitle;
    }

    public int getmImageId() {
        return mImageId;
    }

    public String getmCircleColor() {
        return mCircleColor;
    }

    public int getmIndex() {
        return mIndex;
    }


    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmCircleColor(String mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    public void setmIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public int getmAngle() {
        return mAngle;
    }

    public int getmImageSpecificId() {
        return mImageSpecificId;
    }

    public int getmBackground() {
        return mBackground;
    }
}
