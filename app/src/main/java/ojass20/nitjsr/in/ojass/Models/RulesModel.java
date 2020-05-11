package ojass20.nitjsr.in.ojass.Models;

/**
 * Created by Aditya on 04-02-2017.
 */

public class RulesModel {
    public String text;
    public int ruleNo;

    public RulesModel(int ruleNo, String text) {

        this.text = text;
        this.ruleNo = ruleNo;

    }
    public RulesModel()
    {

    }

    public int getRuleNo() {
        return ruleNo;
    }

    public String getText() {
        return text;
    }

}