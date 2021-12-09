package top.withwings.wow.flow.dsl;

public class Wow {

    public static  <I,P> SubjectProcess<I,P> defineProcess(){
        return new SubjectProcess<>();
    }


}
