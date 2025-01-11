package org.example.stackoverflow;

import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

public class JsonOutputStream extends OutputStream {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream(4);
    private final Base64.Encoder encoder = Base64.getMimeEncoder();

    @Getter
    private int count = 0;

    @Override
    public void write(int b) throws IOException {
        buffer.write(b);
        if (buffer.size() % 4 == 0) { // Process in chunks of 4 Base64 characters
            flushBuffer();
        }
    }

    public void resetCount() {
        count = 0;
    }

    private void flushBuffer() {
        if (buffer.size() < 4) return; // Only decode full Base64 chunks
        byte[] encoded = encoder.encode(buffer.toByteArray());
        buffer.reset();
    }
}
