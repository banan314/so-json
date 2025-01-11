package org.example.stackoverflow;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Writer;

/**
 * Writer that counts the number of characters matching a given letter written
 */
public class JsonWriter extends Writer {

    @Getter
    @Setter
    private int count;

    @Setter
    private char letterToCount = 'a';

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            if (cbuf[i] == letterToCount) {
                count++;
            }
        }
    }

    @Override
    public void flush() throws IOException {
        // no need to do anything, there's no destination
    }

    @Override
    public void close() throws IOException {
        // no need to do anything, there's no destination
    }
}
