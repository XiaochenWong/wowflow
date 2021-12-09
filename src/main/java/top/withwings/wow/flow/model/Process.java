package top.withwings.wow.flow.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Process<I, P> {

    I getId();

    Map<String, Step> steps();

    Step current();

    boolean handle(Event<P> event);

    default boolean handle(String eventName, P payload) {
        return this.handle(new Event<>(eventName, payload));
    }

    List<Log<P>> logs();

    boolean finished();

    static <I, P> Process<I, P> newInstance(I id, Map<String, Step> steps, String startAt) {
        return new ProcessImpl<>(id, steps, startAt);
    }

    static <I, P> Process<I, P> historicalInstance(I id, Map<String, Step> steps, String currentStepKey, List<Log<P>> logs) {
        return new ProcessImpl<>(id, steps, currentStepKey, logs);
    }


    class ProcessImpl<I, P> implements Process<I, P> {

        private final I id;

        private String currentStepKey;

        private final Map<String, Step> steps;

        private final List<Log<P>> logs;

        public ProcessImpl(I id, Map<String, Step> steps, String startAt) {
            this.id = id;
            this.steps = steps;
            this.currentStepKey = startAt;
            this.logs = new ArrayList<>();
            Event<P> initialized = new Event<>(Event.START_EVENT_NAME, null);
            appendLog(initialized, startAt);
        }

        public ProcessImpl(I id, Map<String, Step> steps, String currentStepKey, List<Log<P>> logs) {
            this.id = id;
            this.steps = steps;
            this.currentStepKey = currentStepKey;
            this.logs = new ArrayList<>();
            this.logs.addAll(logs);
        }


        public I getId() {
            return this.id;
        }

        @Override
        public Map<String, Step> steps() {
            return this.steps;
        }

        public Step current() {
            return steps.get(currentStepKey);
        }

        public boolean handle(Event<P> event) {
            final boolean accepted = current().getOptions().keySet().contains(event.getName());
            if (!accepted) {
                return false;
            }
            this.currentStepKey = current().getOptions().get(event.getName());
            appendLog(event, this.currentStepKey);
            return true;
        }

        public List<Log<P>> logs() {
            return this.logs;
        }

        @Override
        public boolean finished() {
            return current().isFinal();
        }

        private void appendLog(Event<P> event, String stepKey) {
            Step to = this.steps.get(stepKey);
            this.logs.add(new Log(event, to));
        }

        public String toString() {
            return "to do my friend";
        }

    }
}
