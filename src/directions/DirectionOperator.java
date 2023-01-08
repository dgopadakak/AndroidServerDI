package directions;

import java.util.ArrayList;
import java.util.Objects;

public class DirectionOperator
{
    private final int id = 1;
    private ArrayList<Direction> directions = new ArrayList<>();

    public void addTour(String directionName, Tour tour)
    {
        boolean isNewDirectionNeeded = true;
        for (Direction direction : directions)
        {
            if (Objects.equals(direction.name, directionName))
            {
                isNewDirectionNeeded = false;
                direction.listOfTours.add(tour);
                break;
            }
        }
        if (isNewDirectionNeeded)
        {
            ArrayList<Tour> tempArrayList = new ArrayList<>();
            tempArrayList.add(tour);
            directions.add(new Direction(directionName, tempArrayList));
        }
    }

    public void delTour(int airlineId, int ticketId)
    {
        directions.get(airlineId).listOfTours.remove(ticketId);
    }

    public void editTour(int airlineId, int ticketId, Tour newTour)
    {
        directions.get(airlineId).listOfTours.set(ticketId, newTour);
    }

    public ArrayList<Direction> getDirections()
    {
        return directions;
    }

    public void setDirections(ArrayList<Direction> directions)
    {
        this.directions = directions;
    }
}
