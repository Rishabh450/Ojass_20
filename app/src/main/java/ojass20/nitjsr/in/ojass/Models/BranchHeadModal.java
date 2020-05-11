package ojass20.nitjsr.in.ojass.Models;

public class BranchHeadModal {

    public String cn, name, img, wn;

    public BranchHeadModal(String cn, String name, String img, String wn) {
        this.cn = cn;
        this.name = name;
        this.img = img;
        this.wn = wn;
    }

    public String getCn() {
        return cn;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public String getWn() {
        return wn;
    }
}
