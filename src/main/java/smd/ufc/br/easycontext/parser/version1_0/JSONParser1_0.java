package smd.ufc.br.easycontext.parser.version1_0;

import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import smd.ufc.br.easycontext.Configuration;
import smd.ufc.br.easycontext.fence.AggregateRule;
import smd.ufc.br.easycontext.fence.Fence;
import smd.ufc.br.easycontext.fence.FenceAction;
import smd.ufc.br.easycontext.fence.Rule;
import smd.ufc.br.easycontext.fence.action.NotificationAction;
import smd.ufc.br.easycontext.fence.action.VibrateAction;
import smd.ufc.br.easycontext.parser.JSONParser;

public class JSONParser1_0 implements JSONParser {
    public static final String TAG = "JSONParser1_0";
    @Override
    public Configuration parseJSON(JsonReader jsonReader) throws IOException {
        String token = jsonReader.nextName();
        List<Fence> fences = new ArrayList<>();
        if(token.equals("fences")){

            fences = parseFenceList(jsonReader);
        }
        Configuration configuration = new Configuration();
        configuration.setFenceList(fences);
        return configuration;
    }

    private List<Fence> parseFenceList(JsonReader jsonReader) throws IOException {
        List<Fence> fences = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()){
            fences.add(parseFence(jsonReader));
        }
        jsonReader.endArray();
        return fences;
    }

    private Fence parseFence(JsonReader jsonReader) throws IOException {
        String name;
        Rule rule;
        FenceAction action;
        jsonReader.beginObject();
        jsonReader.nextName(); // property 'name'
        name = jsonReader.nextString();
        jsonReader.nextName(); // property 'rule'
        rule = parseRule(jsonReader);
        jsonReader.nextName(); // property 'action'
        action = parseActions(jsonReader);
        jsonReader.endObject();
        return new Fence(name, rule, action);
    }

    private FenceAction parseActions(JsonReader jsonReader) throws IOException {
        //action is a JSON array.
        List<FenceAction> actions = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()){
            actions.add(parseAction(jsonReader));
        }
        jsonReader.endArray();
        return actions.get(0);
    }

    private FenceAction parseAction(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        jsonReader.nextName(); //property 'actionName'
        String actionName = jsonReader.nextString();
        FenceAction action = null;
        switch (actionName) {
            case "NotificationAction": {
                String title, channel, text, importanceString;
                jsonReader.nextName(); //property 'title'

                title = jsonReader.nextString();
                jsonReader.nextName(); //property 'channel'

                channel = jsonReader.nextString();
                jsonReader.nextName(); //property 'text'

                text = jsonReader.nextString();
                jsonReader.nextName(); //property 'importance'

                importanceString = jsonReader.nextString();
                int importance = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    switch (importanceString) {
                        case "Notification.HIGH":

                                importance = NotificationManager.IMPORTANCE_HIGH;

                            break;
                        case "Notification.LOW":
                            importance = NotificationManager.IMPORTANCE_LOW;
                            break;
                        case "Notification.NONE":
                            importance = NotificationManager.IMPORTANCE_NONE;
                            break;
                        default:
                            //default
                            importance = NotificationManager.IMPORTANCE_DEFAULT;
                            break;
                    }
                }
                action = new NotificationAction(title, channel, text, importance);
                break;
            }
            case "VibrateAction": {
                jsonReader.nextName(); // property 'ms'
                long ms = jsonReader.nextLong();
                action = new VibrateAction(ms);
                break;
            }
            case "CustomAction": {
                jsonReader.nextName(); // property 'klassname';
                String klassName = jsonReader.nextString();
                try {
                    action = (FenceAction) Class.forName(klassName).newInstance();
                } catch (InstantiationException e) {
                    Log.e(TAG, "parseAction: Class supplied cannot be instantiated.", e);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "parseAction: Class supplied is not public.", e);
                } catch (ClassNotFoundException e) {
                    Log.e(TAG, "parseAction: Class supplied cannot be found.", e);
                } catch (ClassCastException e) {
                    Log.e(TAG, "parseAction: Class supplied does not implement FenceAction.", e);
                }
                break;
            }
            default: {
                //error
                Log.e(TAG, "parseAction: actionName not found.");
                break;
            }
        }
        jsonReader.endObject();
        return action;
    }

    private Rule parseRule(JsonReader jsonReader) throws IOException {
        String ruleName;
        jsonReader.beginObject();
        jsonReader.nextName(); // property ruleName
        ruleName = jsonReader.nextString();
        Rule rule = null;
        //check rule types
        if(ruleName.equals("Headphone")){
            //parse headphone rule...
            HeadphoneParser parser = new HeadphoneParser();
            rule = parser.parseHeadphoneRule(jsonReader);
        } else if(ruleName.equals("Location")){
            //parse location rule...
            LocationParser parser = new LocationParser();
            rule = parser.parseLocationRule(jsonReader);
        } else if(ruleName.equals("DetectedActivity")){
            //parse activity rule...
            DetectedActivityParser parser = new DetectedActivityParser();
            rule = parser.parseDetectedActivityRule(jsonReader);
        } else if(ruleName.equals("TimeInterval")){
            //parse TimeInterval rule...
            TimeIntervalParser parser = new TimeIntervalParser();
            rule = parser.parseTimeIntervalRule(jsonReader);
        } else if(ruleName.equals("Aggregate")){
            //parse aggregate rule...
            rule = parseAggregate(jsonReader);

        } else {
            //unknown rule
            Log.e(TAG, "parseRule: unknown rule type '" + ruleName + "'.");
            rule = null;
        }
        jsonReader.endObject();
        return rule;
    }
    private AggregateRule parseAggregate(JsonReader jsonReader) throws IOException {
        jsonReader.nextName(); //property 'method'
        String method = jsonReader.nextString();
        List<Rule> rules = new ArrayList<>();
        jsonReader.nextName(); //property 'rules'
        jsonReader.beginArray(); // rules array
        while (jsonReader.hasNext()){
            rules.add(parseRule(jsonReader));
        }
        jsonReader.endArray();
        if (method.equals(AggregateMethods.AND)){
            return AggregateRule.and(rules);
        } else if(method.equals(AggregateMethods.OR)){
            return AggregateRule.or(rules);
        } else if(method.equals(AggregateMethods.NOT)){
            return AggregateRule.not(rules.get(0));
        } else {
            //error
            Log.e(TAG, "parseAggregate: no method found");
            return null;
        }
    }

    static private class AggregateMethods{
        public static final String AND = "AGGREGATE.AND";
        public static final String NOT = "AGGREGATE.NOT";
        public static final String OR = "AGGREGATE.OR";
    }
}
