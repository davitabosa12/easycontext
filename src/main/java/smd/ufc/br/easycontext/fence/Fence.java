package smd.ufc.br.easycontext.fence;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;

public final class Fence implements Serializable {
    private String name;
    private Rule rule;
    private FenceAction action;

    public Fence(String name, Rule rule, FenceAction action){
		this.name = name;
		this.rule = rule;
		this.action = action;
	}

    public static Fence fromJson(String json) {

        JsonObject object = new Gson().fromJson(json, JsonObject.class);

        String name = object.getAsJsonPrimitive("name").getAsString();
        String klassName = object.getAsJsonPrimitive("action").getAsString();
        FenceAction action = null;
        try {

            action = (FenceAction) Class.forName(klassName).newInstance();
        } catch (IllegalAccessException e) {
            Log.e("Fence", "fromJson: class" + klassName + " is protected", e);
        } catch (InstantiationException e) {
            Log.e("Fence", "fromJson: class" + klassName + " cannot be instanced", e);
        } catch (ClassNotFoundException e) {
            Log.e("Fence", "fromJson: class" + klassName + " does not exist", e);
            e.printStackTrace();
        }
        Rule r = RuleJsonParser.fromJson(object.get("rule").toString());
        return new Fence(name, r, action);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public FenceAction getAction() {
        return action;
    }

    public void setAction(FenceAction action) {
        this.action = action;
    }


    @Override
    public String toString() {
        JsonObject fence = new JsonObject();
        fence.addProperty("name", name);
        fence.add("rule", new Gson().fromJson(rule.toString(), JsonObject.class));
        fence.addProperty("action", action.getClass().getCanonicalName());
        return fence.toString();
    }
}
