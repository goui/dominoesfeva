package fr.goui.dominosfevahh.model;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.goui.dominosfevahh.event_bus.PlayerNumberChangedEvent;

public class Model {

    private static Model instance;

    private List<Player> players = new ArrayList<>();

    private int numberOfRounds;

    private int pointsByRound;

    private Model() {
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    /* PLAYER DETAILS */

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer() {
        players.add(new Player());
        EventBus.getDefault().post(new PlayerNumberChangedEvent());
    }

    public void removePlayer() {
        players.remove(players.size() - 1);
        EventBus.getDefault().post(new PlayerNumberChangedEvent());
    }

    public Player getCurrentRoundLeader() {
        Player leader = players.get(0);
        for (Player player : players) {
            if (player.getNumberOfPoints() < leader.getNumberOfPoints()) {
                leader = player;
            }
        }
        return leader;
    }

    public Player getWinner() {
        Player leader = null;
        for (Player player : players) {
            if (player.getNumberOfRounds() == numberOfRounds) {
                leader = player;
            }
        }
        return leader;
    }

    public void addPointsToPlayerAtPosition(int position, int points) {
        Player player = players.get(position);
        player.setNumberOfPoints(player.getNumberOfPoints() + points);
    }

    /* GAME DETAILS */

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public int getPointsByRound() {
        return pointsByRound;
    }

    public void setPointsByRound(int pointsByRound) {
        this.pointsByRound = pointsByRound;
    }

    public boolean isRoundFinished() {
        boolean ret = false;
        for (Player player : players) {
            if (player.getNumberOfPoints() >= pointsByRound) {
                ret = true;
            }
        }
        return ret;
    }

    public boolean isGameFinished() {
        return getWinner() != null;
    }
}
