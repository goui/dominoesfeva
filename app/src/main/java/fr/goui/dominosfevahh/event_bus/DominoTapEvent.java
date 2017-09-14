package fr.goui.dominosfevahh.event_bus;

import fr.goui.dominosfevahh.model.Domino;

public class DominoTapEvent {

    private Domino domino;

    public DominoTapEvent(Domino domino) {
        this.domino = domino;
    }

    public Domino getDomino() {
        return domino;
    }
}
