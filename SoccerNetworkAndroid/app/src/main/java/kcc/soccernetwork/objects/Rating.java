package kcc.soccernetwork.objects;

/**
 * Created by Administrator on 4/23/2016.
 */
public class Rating {
    private String value;
    private String rating_type;

    public String getRating_type() {
        return rating_type;
    }

    public void setRating_type(String rating_type) {
        this.rating_type = rating_type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
