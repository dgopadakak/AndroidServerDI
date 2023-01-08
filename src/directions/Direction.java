package directions;

import java.util.ArrayList;

public class Direction
{
    String name;
    ArrayList<Tour> listOfTours;

    public Direction(String name, ArrayList<Tour> listOfTours)
    {
        this.name = name;
        this.listOfTours = listOfTours;
    }
}
