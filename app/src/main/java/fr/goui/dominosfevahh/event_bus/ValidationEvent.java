package fr.goui.dominosfevahh.event_bus;

public class ValidationEvent {

    private boolean isValid;

    public ValidationEvent(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }
}
