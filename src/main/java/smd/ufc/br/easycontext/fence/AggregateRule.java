package smd.ufc.br.easycontext.fence;

import android.os.Parcel;

import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import smd.ufc.br.easycontext.fence.method.AggregateMethod;

public class AggregateRule implements Rule {

    private List<Rule> rules;
    private AggregateMethod method;

    public AggregateRule() {
    }

    public static AggregateRule and(Rule ...rules){
        AggregateRule aggregateFence = new AggregateRule();
        aggregateFence.method = AggregateMethod.AGGREGATE_AND;
        aggregateFence.rules = Arrays.asList(rules);
        return aggregateFence;
    }
    public static AggregateRule and(List<Rule> rules){
        AggregateRule aggregateFence = new AggregateRule();
        aggregateFence.method = AggregateMethod.AGGREGATE_AND;
        aggregateFence.rules = rules;
        return aggregateFence;
    }
    public static AggregateRule or(Rule ...rules){
        AggregateRule aggregateFence = new AggregateRule();
        aggregateFence.method = AggregateMethod.AGGREGATE_OR;
        aggregateFence.rules = Arrays.asList(rules);
        return aggregateFence;
    }
    public static AggregateRule or(List<Rule> rules){
        AggregateRule aggregateFence = new AggregateRule();
        aggregateFence.method = AggregateMethod.AGGREGATE_OR;
        aggregateFence.rules = rules;
        return aggregateFence;
    }
    public static AggregateRule not(Rule rule){
        AggregateRule aggregateFence = new AggregateRule();
        aggregateFence.method = AggregateMethod.AGGREGATE_NOT;
        aggregateFence.rules = Arrays.asList(rule);
        return aggregateFence;
    }
    @Override
    public AwarenessFence getAwarenessFence() {
        List<AwarenessFence> realFences = new ArrayList<>();
        for(Rule r : rules){
            realFences.add(r.getAwarenessFence());
        }
        switch (this.method){
            case AGGREGATE_OR:
                return AwarenessFence.or(realFences);

            case AGGREGATE_AND:
                return AwarenessFence.and(realFences);

            case AGGREGATE_NOT:
                return AwarenessFence.not(realFences.get(0));

            default:
                return null;
        }
    }

    @Override
    public String toString() {

        JsonObject rule = new JsonObject();
        rule.addProperty("type", "Aggregate");
        rule.addProperty("method", String.valueOf(method));

        JsonArray jsonRules = new JsonArray();
        for (Rule r :
                rules) {
            jsonRules.add(new Gson().fromJson(r.toString(), JsonObject.class));
        }

        rule.add("rules", jsonRules);
        return rule.toString();
    }
}
