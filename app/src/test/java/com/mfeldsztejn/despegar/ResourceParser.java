package com.mfeldsztejn.despegar;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ResourceParser {

    private static final String TEST_RESOURCES_LOCATION = "./src/test/res/";

    @SuppressWarnings("unchecked")
    public static <I> I getTestResourceObject(String fileName, Class<I> clazz) throws IOException {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
                .fromJson(getTestResourceContent(fileName), clazz);
    }

    public static String getTestResourceContent(String fileName) throws IOException {
        final StringBuilder contents = new StringBuilder();

        final InputStream is = new FileInputStream(TEST_RESOURCES_LOCATION + fileName);
        final BufferedReader input = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")), 1024 * 8);
        String line;
        while ((line = input.readLine()) != null) {
            contents.append(line);
        }
        input.close();

        return contents.toString();
    }
}
