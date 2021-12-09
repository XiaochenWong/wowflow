package top.withwings.wow.flow.model;

import java.time.LocalDateTime;

public class Log<P> {

    private Event<P> event;
    private Step to;
    private LocalDateTime time;

    public Log(Event<P> event, Step to) {
        this.event = event;
        this.to = to;
        this.time = LocalDateTime.now();
    }


    public Event getEvent() {
        return event;
    }

    public Step getTo() {
        return to;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String toString(){

        StringBuilder builder = new StringBuilder();

        if(event.getName().equals(Event.START_EVENT_NAME)){
            builder.append(String.format("Flow started at step \"%s\" --- %s", to.getTitle(), time.toString()));
        }else{
            builder.append(String.format("Flow went to step \"%s\" due to event \"%s\" ---%s",
                    to.getTitle(), event.getName(), time.toString()));
        }

        if(to.isFinal()){
            builder.append("(End)");
        }
        return builder.toString();
    }
}
