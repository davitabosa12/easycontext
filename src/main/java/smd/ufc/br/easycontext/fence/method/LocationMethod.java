package smd.ufc.br.easycontext.fence.method;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by davitabosa on 13/08/2018.
 */

public enum LocationMethod implements FenceMethod{
    LOCATION_ENTERING("ENTERING"),
    LOCATION_EXITING("EXITING"),
    LOCATION_IN("IN");
    private String value;
    private static final Map<String, LocationMethod> map = new HashMap<>();
    static {
        for (LocationMethod en : values()) {
            map.put(en.value, en);
        }
    }

    public static LocationMethod valueFor(String name) {
        return map.get(name);
    }
    LocationMethod(String valueOf){
        this.value = valueOf;
    }
}
