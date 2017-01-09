package com.longge.thirdpartdemo.websocket.converter;

import java.io.IOException;

/**
 * Created by yunlong.su on 2017/1/9.
 */

public interface Converter<F, T> {
    T convert(F value) throws IOException;
}
