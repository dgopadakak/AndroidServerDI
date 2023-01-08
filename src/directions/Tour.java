package directions;

public class Tour
{
    String country;
    String duration;
    int rate;
    String startDate;
    String endDate;
    int cost;
    int isAvailable;
    String comment;

    public Tour(String country, String duration, int rate, String startDate, String endDate, int cost,
                int isAvailable, String comment)
    {
        this.country = country;
        this.duration = duration;
        this.rate = rate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cost = cost;
        this.isAvailable = isAvailable;
        this.comment = comment;
    }
}
