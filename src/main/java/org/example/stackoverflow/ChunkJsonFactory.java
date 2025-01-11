package org.example.stackoverflow;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;

import java.io.IOException;
import java.io.Reader;

public class ChunkJsonFactory extends JsonFactory {

    public ChunkJsonFactory() {
        super();
    }

    public ChunkJsonFactory(ObjectCodec oc) {
        super(oc);
    }

    public ChunkJsonFactory(JsonFactory src, ObjectCodec codec) {
        super(src, codec);
    }

    public ChunkJsonFactory(JsonFactoryBuilder b) {
        super(b);
    }

    public ChunkJsonFactory(TSFBuilder<?, ?> b, boolean bogus) {
        super(b, bogus);
    }

    @Override
    protected JsonParser _createParser(Reader r, IOContext ctxt) throws IOException {
        return new ChunkReaderBasedJsonParser(ctxt, _parserFeatures, r, _objectCodec,
                _rootCharSymbols.makeChild());
    }
}
