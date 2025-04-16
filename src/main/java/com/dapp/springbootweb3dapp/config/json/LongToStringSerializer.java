package com.dapp.springbootweb3dapp.config.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.lang.reflect.Type;

public class LongToStringSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        if (object instanceof Long) {
//            Long value = (Long) object;
//
//            if (value > 9007199254740991)
//                9007199254740991
//            1754751175128801281
//            // 大于15位数
            if (String.valueOf(object).length() >= 15) {
                serializer.out.writeString(object.toString());
            } else {
                serializer.out.writeLong((Long) object);
            }
            return;
        }

        serializer.out.writeString(object.toString());
    }
}
