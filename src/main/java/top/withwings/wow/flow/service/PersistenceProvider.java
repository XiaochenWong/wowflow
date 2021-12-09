package top.withwings.wow.flow.service;

import top.withwings.wow.flow.model.Process;

public interface PersistenceProvider<I, P> {

    void persist(Process<I, P> process);

    Process<I,P> get(I id);
}
