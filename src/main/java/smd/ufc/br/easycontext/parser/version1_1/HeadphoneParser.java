package smd.ufc.br.easycontext.parser.version1_1;


import android.util.Log;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

import smd.ufc.br.easycontext.fence.HeadphoneRule;

public class HeadphoneParser {

    public HeadphoneRule parseHeadphoneRule(JsonReader jsonReader) throws IOException {
        jsonReader.nextName(); // property 'method'
        String method = jsonReader.nextString();
        HeadphoneRule rule = null;
        if(method.equals(HeadphoneMethods.PLUGGING_IN)){
            rule = HeadphoneRule.pluggingIn();
        } else if (method.equals(HeadphoneMethods.UNPLUGGING)){
            rule = HeadphoneRule.unplugging();
        } else if (method.equals(HeadphoneMethods.DURING)){
            //not yet implemented
            rule = null;
        } else {
            //error
            Log.e("HeadphoneParser", "parseHeadphoneRule: method " + method + " does not exist.");
        }
        return rule;
    }

    static class HeadphoneMethods{
        public static final String PLUGGING_IN= "HEADPHONE.PLUGGING_IN";
        public static final String UNPLUGGING= "HEADPHONE.UNPLUGGING";
        public static final String DURING= "HEADPHONE.DURING";
    }
}
