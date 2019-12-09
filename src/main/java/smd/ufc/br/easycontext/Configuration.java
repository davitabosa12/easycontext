package smd.ufc.br.easycontext;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import smd.ufc.br.easycontext.fence.Fence;
import smd.ufc.br.easycontext.parser.version1_0.JSONParser1_0;

public class Configuration {
    public static final String TAG = "Configuration";
    private static final String CONFIGURATION_NAME = "configuration";
    private List<Fence> fenceList;

    public List<Fence> getFenceList() {
        return fenceList;
    }

    public void setFenceList(List<Fence> fenceList) {
        this.fenceList = fenceList;
    }

    public static Configuration readJSON(Context context) throws IOException {
        Gson g = new Gson();
        InputStream is;
        try{
            is = context.getResources().openRawResource(context.getResources().getIdentifier(CONFIGURATION_NAME,
                    "raw", context.getPackageName()));
        } catch (Resources.NotFoundException ex){
            //there are no json with name 'configuration' in res/raw.
            Log.e(TAG, "There are no configurations in res/raw. Please make sure you have a valid JSON file named '" + CONFIGURATION_NAME + "' in res/raw.", ex);
            return null;
        }

        Reader r = new InputStreamReader(is);


        JsonReader jsonReader = g.newJsonReader(r); //fazer um novo reader de json

        jsonReader.beginObject(); //espera um inicio de objeto
        String peekTagName = jsonReader.nextName(); //property 'version'
        if(peekTagName.equals("version")){
            String version = jsonReader.nextString();
            //todo Set up a version of ConfigurationReader here
            Log.d(TAG, "readJSON: Using version " + version);
        }
        Configuration config = new JSONParser1_0().parseJSON(jsonReader);

        return config;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Configuration: \n");
        sb.append(fenceList.toString());
        return sb.toString();
    }
}
