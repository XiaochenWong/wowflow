package test;


import top.withwings.wow.flow.dsl.Wow;
import top.withwings.wow.flow.model.Event;
import top.withwings.wow.flow.model.Process;
import top.withwings.wow.flow.service.ChangeListener;
import top.withwings.wow.flow.service.PersistenceProvider;
import top.withwings.wow.flow.service.ProcessMaster;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessMasterTests {

    public static void main(String[] args) {

        ProcessMaster<Long, Locale> master = new MyProcessMaster();
        Process<Long, Locale> process = master.register(buildProcess());

        process.handle(new Event<>("yes", Locale.CANADA));

        Process<Long, Locale> persisted = master.getById(100L);

        assert persisted.logs().size() == 2;

        persisted.handle("no", Locale.CANADA);

        Process<Long, Locale> persistedAnother = master.getById(100L);

        assert persisted.logs().equals(persistedAnother.logs());

        persisted.logs().forEach(System.out::println);

    }

    private static Process<Long, Locale> buildProcess() {
        final Process<Long, Locale> process =
                Wow.defineProcess()
                        .startAt("county-judge")
                        .withId(100L).withPayloadType(Locale.class)
                        .step("county-judge")
                        .when("yes").goesTo("city-judge")
                        .when("no").goesTo("fail")
                        .withTitle("乡领导审批")
                        .step("city-judge")
                        .withTitle("市领导审批")
                        .when("yes").goesTo("province-judge")
                        .when("no").goesTo("fail")
                        .step("province-judge")
                        .withTitle("省领导审批")
                        .when("yes").goesTo("success")
                        .when("no").goesTo("fail")
                        .step("fail").withTitle("未通过")
                        .step("success").withTitle("审批通过")
                        .build();
        return process;
    }


    private static class MyProcessMaster extends ProcessMaster<Long, Locale> {

        @Override
        public List<ChangeListener<Long>> getChangeListeners() {
            return List.of(new JustPrintChangeListener());
        }

        @Override
        public PersistenceProvider<Long, Locale> getPersistenceProvider() {
            return ConcurrentHashMapPersistenceProvider.INSTANCE;
        }
    }


    private static class ConcurrentHashMapPersistenceProvider implements PersistenceProvider<Long, Locale> {

        private ConcurrentHashMap<Long, Process<Long, Locale>> map = new ConcurrentHashMap<>();

        static ConcurrentHashMapPersistenceProvider INSTANCE = new ConcurrentHashMapPersistenceProvider();

        @Override
        public void persist(Process<Long, Locale> source) {
            map.put(source.getId(), source);
        }

        @Override
        public Process<Long, Locale> get(Long id) {
            return map.get(id);
        }
    }


    private static class JustPrintChangeListener implements ChangeListener<Long> {

        @Override
        public void onChange(Long id) {
            System.err.println(String.format("process with id: %s has changed. ", id));
        }
    }
}
