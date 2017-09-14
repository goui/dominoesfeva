package fr.goui.dominosfevahh.event_bus;

public class PointSetEvent {

    private int playerPosition;

    private int playerPoints;

    public PointSetEvent(int playerPosition, int playerPoints) {
        this.playerPosition = playerPosition;
        this.playerPoints = playerPoints;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public int getPlayerPoints() {
        return playerPoints;
    }
}
