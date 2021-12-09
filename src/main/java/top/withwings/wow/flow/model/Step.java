package top.withwings.wow.flow.model;

import java.util.LinkedHashMap;


public class Step {

    private String title;

    private LinkedHashMap<String, String> options = new LinkedHashMap<>();

    public boolean isFinal(){
        return options.isEmpty();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public LinkedHashMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(LinkedHashMap<String, String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Step{ title='" + title + '}';
    }
}
