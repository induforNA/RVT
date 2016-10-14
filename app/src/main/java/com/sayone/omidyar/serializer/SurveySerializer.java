package com.sayone.omidyar.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sayone.omidyar.model.Survey;

import java.lang.reflect.Type;

/**
 * Created by sayone on 14/10/16.
 */

public class SurveySerializer implements JsonSerializer<Survey> {
    @Override
    public JsonElement serialize(Survey src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", src.getId());
        return jsonObject;
    }
}
