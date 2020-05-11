package ojass20.nitjsr.in.ojass.Models;

import java.util.ArrayList;

public class BranchModal {
    public String about;
    public ArrayList<BranchHeadModal> branchHead;

    public BranchModal(String about, ArrayList<BranchHeadModal> branchHead) {
        this.about = about;
        this.branchHead = branchHead;
    }

    public String getAbout() {
        return about;
    }

    public ArrayList<BranchHeadModal> getBranchHead() {
        return branchHead;
    }
}
