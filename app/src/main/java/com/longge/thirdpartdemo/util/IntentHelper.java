package com.longge.thirdpartdemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class IntentHelper {

    /**
     * 普通Act跳转
     *
     * @param context
     * @param clazz
     */
    public static void startAct(Context context, Class<? extends Activity> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

}