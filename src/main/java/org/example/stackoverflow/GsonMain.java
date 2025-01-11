package org.example.stackoverflow;

import com.google.gson.stream.JsonReader;

import java.io.FileReader;

public class GsonMain {
    public static void main(String[] args) throws Exception {
        try (JsonReader reader = new JsonReader(new FileReader(Config.SAMPLE_JSON))) {
            while (reader.hasNext()) {
                reader.nextString();
            }
        }
    }
}
