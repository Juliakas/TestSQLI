package com.github.juliakas.restEsapi;

import lombok.Getter;
import lombok.Setter;
import org.owasp.esapi.codecs.OracleCodec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MyOracleCodec extends OracleCodec {

    @Getter @Setter
    private char escapeChar;

    public MyOracleCodec(char escapeChar) {
        this.escapeChar = escapeChar;
    }

    @Override
    public String encode(char[] immune, String input) {
        String encoded = input;
        List<Character> escapableChars = new ArrayList<>(List.of('%', '_', '['));
        for (Character c : immune) {
            escapableChars.remove(c);
        }
        escapableChars.add(0, escapeChar);
        for (Character c : escapableChars) {
            encoded = encoded.replace(c.toString(), escapeChar + c.toString());
        }
        return encoded;
    }
}
