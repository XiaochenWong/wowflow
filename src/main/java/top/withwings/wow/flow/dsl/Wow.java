package top.withwings.wow.flow.dsl;

public class Wow {

    private Wow(){
        // forbid instancing
    }

    public static  <I,P> SubjectProcess<I,P> defineProcess(){
        return new SubjectProcess<>();
    }


}
