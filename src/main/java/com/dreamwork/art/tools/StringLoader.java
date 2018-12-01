package com.dreamwork.art.tools;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StringLoader {
    public static String load(String classPath) throws IOException {
        return StreamUtils.copyToString(new ClassPathResource(classPath).getInputStream(), StandardCharsets.UTF_8);
    }
}
