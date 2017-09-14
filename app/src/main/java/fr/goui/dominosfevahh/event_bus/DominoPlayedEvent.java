package fr.goui.dominosfevahh.event_bus;

import fr.goui.dominosfevahh.model.Domino;

public class DominoPlayedEvent {

    private Domino domino;

    public DominoPlayedEvent(Domino domino) {
        this.domino = domino;
    }

    public Domino getDomino() {
        return domino;
    }
}
