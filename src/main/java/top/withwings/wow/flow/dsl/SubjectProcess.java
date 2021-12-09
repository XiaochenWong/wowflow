package top.withwings.wow.flow.dsl;

import top.withwings.wow.flow.model.Process;
import top.withwings.wow.flow.model.Step;

import java.util.HashMap;
import java.util.Map;

public class SubjectProcess<I, P> {
    private I id;
    private String startAt;
    private Class<P> payloadType;
    private Map<String, Step> steps = new HashMap<>();

    public <I2> SubjectProcess<I2, P> withId(I2 i2) {
        final SubjectProcess<I2, P> sp = new SubjectProcess<>();
        sp.payloadType = this.payloadType;
        sp.startAt = this.startAt;
        sp.steps = this.steps;
        sp.id = i2;
        return sp;
    }

    public <P2> SubjectProcess<I, P2> withPayloadType(Class<P2> p2) {
        final SubjectProcess<I, P2> sp = new SubjectProcess<>();
        sp.payloadType = p2;
        sp.startAt = this.startAt;
        sp.steps = this.steps;
        sp.id = this.id;
        return sp;
    }

    public SubjectProcess<I, P> startAt(String startAt) {
        this.startAt = startAt;
        return this;
    }

    public SubjectProcess<I, P> and() {
        return this;
    }

    public SubjectStep<I, P> step(String stepKey) {
        final SubjectStep<I, P> step = new SubjectStep<>();
        this.steps.put(stepKey, step.innerStep);
        step.root = this;
        return step;
    }

    public Process<I, P> build() {
        return Process.newInstance(id, steps, this.startAt);
    }

}
