package test;

import top.withwings.wow.flow.dsl.Wow;
import top.withwings.wow.flow.model.Event;
import top.withwings.wow.flow.model.Process;

import java.time.LocalDateTime;

public class ProcessDefineTests {

    public static void main(String[] args) {

        final Process<Long, SRPayload> process =
                Wow.defineProcess()
                        .startAt("county-judge")
                .withId(100L).withPayloadType(SRPayload.class)
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

        final SRPayload payload = new SRPayload(LocalDateTime.now(), "Shawn Wong");
        process.handle(new Event<>("yes", payload));
        process.handle(new Event<>("yes", payload));
        process.handle(new Event<>("yes", payload));
        process.logs().forEach(System.out::println);
    }


    public static class SRPayload {
        private LocalDateTime time;
        private String issuer;

        public SRPayload(LocalDateTime time, String issuer) {
            this.time = time;
            this.issuer = issuer;
        }

        @Override
        public String toString() {
            return "SRPayload{" +
                    "time=" + time +
                    ", issuer='" + issuer + '\'' +
                    '}';
        }
    }

}
