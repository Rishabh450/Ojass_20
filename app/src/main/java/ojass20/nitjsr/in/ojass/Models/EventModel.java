package ojass20.nitjsr.in.ojass.Models;

import java.util.ArrayList;

/**
 * Created by Aditya on 02-02-2017.
 */

public class EventModel {

    public String about;
    public String branch;
    public String details;
    public String name;
    public String link;



    public PrizeModel1 p1;
    public PrizeModel2 p2;
    ArrayList<CoordinatorsModel> coordinatorsModelArrayList=new ArrayList<>();
    ArrayList<RulesModel> rulesModels=new ArrayList<>();

    public EventModel(String about, String branch, String details, String name, PrizeModel1 p1,PrizeModel2 p2, ArrayList<CoordinatorsModel> coordinatorsModelArrayList, ArrayList<RulesModel> rulesModels, String link) {
        this.about=about;
        this.name = name;
        this.details=details;
        this.branch=branch;
        this.p1 = p1;
        this.p2=p2;
        this.coordinatorsModelArrayList=coordinatorsModelArrayList;
        this.rulesModels=rulesModels;
        this.link = link;
    }
    public EventModel()
    {

    }

    public String getAbout() {
        return about;
    }

    public String getBranch() {
        return branch;
    }

    public String getDetails() {
        return details;
    }

    public String getName() {
        return name;
    }

    public ArrayList<CoordinatorsModel> getCoordinatorsModelArrayList() {
        return coordinatorsModelArrayList;
    }
    public PrizeModel1 getP1() {
        return p1;
    }

    public PrizeModel2 getP2() {
        return p2;
    }
    public ArrayList<RulesModel> getRulesModels() {
        return rulesModels;
    }

    public String getLink() {
        return link;
    }
}
