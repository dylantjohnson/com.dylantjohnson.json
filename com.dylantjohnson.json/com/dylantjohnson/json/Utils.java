package com.dylantjohnson.json;

import java.io.*;

class Utils {
    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException("value must not be null");
        }
    }

    public static int peek(InputStream stream) throws IOException {
        stream.mark(1);
        var next = stream.read();
        stream.reset();
        return next;
    }

    public static int peekIgnoreWhitespace(InputStream stream) throws IOException {
        var next = peek(stream);
        while (Character.isWhitespace(next)) {
            stream.read();
            next = peek(stream);
        }
        return next;
    }

    public static boolean isDigit(int codePoint) {
        return Character.isDigit(codePoint) || codePoint == '.';
    }
}
