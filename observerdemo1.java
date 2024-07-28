import java.util.ArrayList;
import java.util.List;

// Observer interface
interface Observer {
    void update(String roomID, boolean isOccupied);
}

// Concrete Observers
class LightingSystem implements Observer {
    @Override
    public void update(String roomID, boolean isOccupied) {
        if (isOccupied) {
            System.out.println("Turning on lights in room: " + roomID);
        } else {
            System.out.println("Turning off lights in room: " + roomID);
        }
    }
}

class AirConditioningSystem implements Observer {
    @Override
    public void update(String roomID, boolean isOccupied) {
        if (isOccupied) {
            System.out.println("Turning on AC in room: " + roomID);
        } else {
            System.out.println("Turning off AC in room: " + roomID);
        }
    }
}

// Subject class
class Room {
    private String roomID;
    private boolean isOccupied;
    private List<Observer> observers;

    public Room(String roomID) {
        this.roomID = roomID;
        this.isOccupied = false;
        this.observers = new ArrayList<>();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
        notifyObservers();
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(roomID, isOccupied);
        }
    }
}

// Main class to demonstrate the Observer pattern
public class observerdemo1 {
    public static void main(String[] args) {
        Room room101 = new Room("101");

        LightingSystem lightingSystem = new LightingSystem();
        AirConditioningSystem airConditioningSystem = new AirConditioningSystem();

        room101.addObserver(lightingSystem);
        room101.addObserver(airConditioningSystem);

        // Changing room occupancy status
        System.out.println("Room 101 is now occupied:");
        room101.setOccupied(true);

        System.out.println("\nRoom 101 is now vacant:");
        room101.setOccupied(false);
    }
}
