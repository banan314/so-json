package org.example.stackoverflow;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;

import java.io.BufferedOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class Main {
    public static void main(String[] args) throws Exception {
//        var factory = new JsonFactory();
        var factory = new ChunkJsonFactory();
//        try (var parser = factory.createParser(new FileReader("src/main/resources/sample-json.json"))) {
        try (var parser = factory.createParser(new FileReader("src/main/resources/big-json.json"))) {
            while (parser.nextToken() != null) {
                if (parser.getCurrentToken().isScalarValue()) {
//                    readBinaryValue(parser);
//                    printToken(parser);
                    JsonWriter writer = new JsonWriter();
                    writer.setLetterToCount('a');
                    parser.getText(writer);
                    System.out.println(writer.getCount());
                    break;
                }
            }
        }
    }

    private static void printToken(JsonParser parser) throws Exception {
        Writer writer = new OutputStreamWriter(new BufferedOutputStream(System.out), "UTF-8");
        var writtenOut = parser.getText(writer);
        System.out.println("\n");
        System.out.println(writtenOut);
        writer.flush();
    }

    private static void readBinaryValue(JsonParser parser) throws Exception {
        System.out.println(parser.getText());
        var jsonOutputStream = new JsonOutputStream();
        jsonOutputStream.resetCount();
        parser.readBinaryValue(Base64Variants.MIME, jsonOutputStream);
        System.out.println(jsonOutputStream.getCount());
    }
}