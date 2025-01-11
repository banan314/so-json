package org.example.stackoverflow;

import com.google.gson.stream.JsonReader;

import java.io.FileReader;

public class GsonMain {
    public static void main(String[] args) throws Exception {
        try (JsonReader reader = new JsonReader(new FileReader(Config.SAMPLE_JSON))) {
            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                // there's no way to use a writer stream with GSON
                if (reader.peek().name().equals("STRING")) {
                    String value = reader.nextString();
                    System.out.println(value);
                    break;
                }
            }
        }
    }
}
