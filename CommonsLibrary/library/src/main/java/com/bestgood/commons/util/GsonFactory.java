package com.bestgood.commons.util;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Created by dengdingchun on 16/3/25.
 */
public class GsonFactory {

    // Using Android's base64 libraries. This can be replaced with any base64 library.
    private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.decode(json.getAsString(), Base64.NO_WRAP);
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
        }
    }

    public static Gson buildNormalGson() {
        return new Gson();
    }

    public static Gson buildOnlyIncludePublicFieldsGson() {
        return new GsonBuilder()
                .excludeFieldsWithModifiers(
                        Modifier.PROTECTED,
                        Modifier.PRIVATE,
                        Modifier.STATIC,
                        Modifier.FINAL,
                        Modifier.TRANSIENT,
                        Modifier.VOLATILE)
                .registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
                .create();
    }

    public static Gson buildOnlyIncludeExposeAnnotationGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeHierarchyAdapter(byte[].class,
                        new ByteArrayToBase64TypeAdapter())
                .create();
    }
}
