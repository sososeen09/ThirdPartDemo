package com.longge.thirdpartdemo.websocket.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

/**
 * Created by yunlong.su on 2017/1/9.
 */

public class GsonResponseBodyConverter<T> implements Converter<String, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(String value) throws IOException {
        return adapter.fromJson(value);
    }

}
