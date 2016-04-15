package com.bestgood.commons.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

/**
 * Created by dengdingchun on 16/3/25.
 */
public class GsonFactory {

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
                .create();
    }

    public static Gson buildOnlyIncludeExposeAnnotationGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }
}
