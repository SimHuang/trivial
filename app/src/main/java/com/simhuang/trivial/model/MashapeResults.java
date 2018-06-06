package com.simhuang.trivial.model;

import java.util.List;

public class MashapeResults {

    private Meta meta;
    private List<MashapeQuestion> result;

    public MashapeResults(Meta meta, List<MashapeQuestion> result) {
        this.meta = meta;
        this.result = result;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<MashapeQuestion> getResult() {
        return result;
    }

    public void setResults(List<MashapeQuestion> result) {
        this.result = result;
    }
}
