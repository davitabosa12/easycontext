package smd.ufc.br.easycontext.parser;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

import smd.ufc.br.easycontext.Configuration;

public interface JSONParser {
    Configuration parseJSON(JsonReader jsonReader) throws IOException;
}
