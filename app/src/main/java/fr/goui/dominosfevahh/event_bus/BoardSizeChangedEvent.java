package fr.goui.dominosfevahh.event_bus;

public class BoardSizeChangedEvent {

    private int width;

    private int height;

    public BoardSizeChangedEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
