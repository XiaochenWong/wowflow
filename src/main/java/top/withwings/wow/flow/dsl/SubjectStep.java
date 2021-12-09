package top.withwings.wow.flow.dsl;

import top.withwings.wow.flow.model.Process;
import top.withwings.wow.flow.model.Step;


public class SubjectStep<I, P> {

    SubjectProcess<I, P> root;

    Step innerStep = new Step();

    public SubjectWhen<I, P> when(String eventKey){
        return new SubjectWhen<>(this, eventKey);
    }

    public SubjectStep<I, P> withTitle(String title){
        this.innerStep.setTitle(title);
        return this;
    }

    public static class SubjectWhen<I, P> {

        SubjectStep<I, P> root;
        String eventKey;

        public SubjectWhen(SubjectStep root, String eventKey) {
            this.root = root;
            this.eventKey = eventKey;
        }

        public SubjectStep<I, P> goesTo(String futureStepKey) {
            root.innerStep.getOptions().put(eventKey, futureStepKey);
            return this.root;
        }

    }

    public SubjectStep<I, P> step(String stepKey){
        return root.step(stepKey);
    }

    public SubjectProcess<I, P> and(){
        return this.root;
    }

    public Process<I, P> build(){
        return root.build();
    }



}
