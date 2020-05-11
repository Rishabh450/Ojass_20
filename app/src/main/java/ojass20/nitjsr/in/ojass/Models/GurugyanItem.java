package ojass20.nitjsr.in.ojass.Models;

public class GurugyanItem implements Comparable{

    public String url, title, description;
    public int day;

    public GurugyanItem(){

    }

    public GurugyanItem(String url, String title, String description, int day) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.day = day;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDay() {
        return day;
    }

    @Override
    public int compareTo(Object o) {
        return this.getDay() - ((GurugyanItem) o).getDay();
    }

}
