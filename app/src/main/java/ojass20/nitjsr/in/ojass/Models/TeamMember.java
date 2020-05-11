package ojass20.nitjsr.in.ojass.Models;

import androidx.annotation.Nullable;

public class TeamMember {
    public String name,desig,whatsapp,facebook,github,call,img,insta,linkdin,email;
    public int team;

    public TeamMember(){}
    public TeamMember(String name, String desig, String whatsapp, String facebook,String insta,String linkdin, String github, String call,String img, int team,String email) {
        this.name = name;
        this.desig = desig;
        this.whatsapp = whatsapp;
        this.facebook = facebook;
        this.github = github;
        this.call = call;
        this.team = team;
        this.img=img;
        this.insta=insta;
        this.linkdin=linkdin;
        this.email = email;
    }

}
