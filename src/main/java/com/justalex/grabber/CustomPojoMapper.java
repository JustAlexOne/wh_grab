package com.justalex.grabber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.mashape.unirest.http.ObjectMapper;

import java.io.IOException;

public class CustomPojoMapper implements ObjectMapper {


    private static CustomPojoMapper instance;
    private com.fasterxml.jackson.databind.ObjectMapper mapper;

    private CustomPojoMapper() {
        mapper = new com.fasterxml.jackson.databind.ObjectMapper();
    }

    public static synchronized CustomPojoMapper getInstance() {
        if (instance == null) {
            instance = new CustomPojoMapper();
        }
        return instance;
    }

    public com.fasterxml.jackson.databind.ObjectMapper getEnclosingMapper() {
        return mapper;
    }

    @Override
    public <T> T readValue(String value, Class<T> valueType) {
        T result = null;
        try {
            result = mapper.readValue(value, valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String writeValue(Object value) {
        String result = null;
        try {
            result = mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public <T> T readValue(String content, JavaType valueType) throws IOException {
        return mapper.readValue(content, valueType);
    }

    public TypeFactory getTypeFactory() {
        return mapper.getTypeFactory();
    }

}
