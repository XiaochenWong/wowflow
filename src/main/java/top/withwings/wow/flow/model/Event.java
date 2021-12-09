package top.withwings.wow.flow.model;

public class Event<P> {

    private String name;
    private P payload;

    public final static String START_EVENT_NAME = "Initialized";

    public Event(String name, P payload) {
        this.name = name;
        this.payload = payload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public P getPayload() {
        return payload;
    }

    public void setPayload(P payload) {
        this.payload = payload;
    }
}
