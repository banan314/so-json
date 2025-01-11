package org.example.stackoverflow;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class ChunkReaderBasedJsonParser extends ReaderBasedJsonParser {
    private boolean finishedStringBuffer = false;

    public ChunkReaderBasedJsonParser(IOContext ctxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st, char[] inputBuffer, int start, int end, boolean bufferRecyclable) {
        super(ctxt, features, r, codec, st, inputBuffer, start, end, bufferRecyclable);
    }

    public ChunkReaderBasedJsonParser(IOContext ctxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st) {
        super(ctxt, features, r, codec, st);
    }

    @Override
    public int getText(Writer writer) throws IOException {
        JsonToken t = _currToken;
        if (t == JsonToken.VALUE_STRING) {
            int total = 0;
            while (_tokenIncomplete) {
                _tokenIncomplete = getNextStringChunk();
                total += _textBuffer.contentsToWriter(writer);
                _textBuffer.resetWithEmpty();
            }
            return total;
        }
        if (t == JsonToken.FIELD_NAME) {
            String n = _parsingContext.getCurrentName();
            writer.write(n);
            return n.length();
        }
        if (t != null) {
            if (t.isNumeric()) {
                return _textBuffer.contentsToWriter(writer);
            }
            char[] ch = t.asCharArray();
            writer.write(ch);
            return ch.length;
        }
        return 0;
    }

    /**
     *
     * @return true if the token is still incomplete, false if it is complete
     */
    private boolean getNextStringChunk() throws IOException {
        finishedStringBuffer = false;
        _finishString();
        return finishedStringBuffer;
    }

    @Override
    protected void _finishString2() throws IOException {
        char[] outBuf = _textBuffer.getCurrentSegment();
        int outPtr = _textBuffer.getCurrentSegmentSize();
        final int[] codes = INPUT_CODES_LATIN1;
        final int maxCode = codes.length;

        while (true) {
            // we ran out of buffer?
            if (outPtr >= outBuf.length) {
                finishedStringBuffer = true;
                return;
            }

            if (_inputPtr >= _inputEnd) {
                if (!_loadMore()) {
                    _reportInvalidEOF(": was expecting closing quote for a string value",
                            JsonToken.VALUE_STRING);
                }
            }
            char c = _inputBuffer[_inputPtr++];
            int i = c;
            if (i < maxCode && codes[i] != 0) {
                if (i == INT_QUOTE) {
                    break;
                } else if (i == INT_BACKSLASH) {
                    /* Although chars outside of BMP are to be escaped as
                     * an UTF-16 surrogate pair, does that affect decoding?
                     * For now let's assume it does not.
                     */
                    c = _decodeEscaped();
                } else if (i < INT_SPACE) {
                    _throwUnquotedSpace(i, "string value");
                } // anything else?
            }

            // Ok, let's add char to output:
            outBuf[outPtr++] = c;
        }
        _textBuffer.setCurrentLength(outPtr);
    }
}
