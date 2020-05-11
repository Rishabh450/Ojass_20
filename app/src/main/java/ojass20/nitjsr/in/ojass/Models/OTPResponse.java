package ojass20.nitjsr.in.ojass.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OTPResponse {
    @SerializedName("Status")
    @Expose
    public  String status;
    @SerializedName("Details")
    @Expose
    public  String details;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
