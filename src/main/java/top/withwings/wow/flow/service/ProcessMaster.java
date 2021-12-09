package top.withwings.wow.flow.service;

import top.withwings.wow.flow.model.Event;
import top.withwings.wow.flow.model.Log;
import top.withwings.wow.flow.model.Process;
import top.withwings.wow.flow.model.Step;

import java.util.List;
import java.util.Map;

public abstract class ProcessMaster<I, P> {

    public abstract List<ChangeListener<I>> getChangeListeners();

    public abstract PersistenceProvider<I, P> getPersistenceProvider();


    public Process<I, P> register(Process<I, P> process) {
        getPersistenceProvider().persist(process);
        return new ProcessProxy<>(process, getChangeListeners(), getPersistenceProvider());
    }

    public Process<I, P> getById(I id) {
        Process<I, P> source = getPersistenceProvider().get(id);
        return new ProcessProxy<>(source, getChangeListeners(), getPersistenceProvider());
    }


    private static class ProcessProxy<I, P> implements Process<I, P> {
        private Process<I, P> source;
        private List<ChangeListener<I>> changeListeners;
        private PersistenceProvider<I, P> persistenceProvider;

        public ProcessProxy(Process<I, P> source,
                            List<ChangeListener<I>> changeListeners,
                            PersistenceProvider<I, P> persistenceProvider) {
            this.source = source;
            this.changeListeners = changeListeners;
            this.persistenceProvider = persistenceProvider;
        }


        @Override
        public I getId() {
            return source.getId();
        }

        @Override
        public Map<String, Step> steps() {
            return source.steps();
        }

        @Override
        public Step current() {
            return source.current();
        }

        @Override
        public boolean handle(Event<P> event) throws IllegalStateException{

            externalChangeDetect();

            boolean accepted = source.handle(event);
            if (accepted) {
                persistenceProvider.persist(source);
                for (ChangeListener<I> changeListener : changeListeners) {
                    changeListener.onChange(source.getId());
                }
            }
            return accepted;
        }

        private void externalChangeDetect() {
            Process<I,P> persisted = persistenceProvider.get(this.getId());
            if(persisted.logs().size() != this.logs().size()){
                throw new IllegalStateException("The process has changed, Please fetch the latest process");
            }
        }

        @Override
        public List<Log<P>> logs() {
            return source.logs();
        }

        @Override
        public boolean finished() {
            return source.finished();
        }
    }

}
