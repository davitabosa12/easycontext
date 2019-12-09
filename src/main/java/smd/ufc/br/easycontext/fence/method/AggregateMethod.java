package smd.ufc.br.easycontext.fence.method;

import java.util.HashMap;
import java.util.Map;

public enum AggregateMethod implements FenceMethod {
    AGGREGATE_AND("AND"),
    AGGREGATE_OR("OR"),
    AGGREGATE_NOT("NOT");
    private String value;
    private static final Map<String, AggregateMethod> map = new HashMap<>();
    static {
        for (AggregateMethod en : values()) {
            map.put(en.value, en);
        }
    }

    public static AggregateMethod valueFor(String name) {
        return map.get(name);
    }
    AggregateMethod(String valueOf){
        this.value = valueOf;
    }

}
