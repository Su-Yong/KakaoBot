package com.suyong.kakaobot.script;

import android.content.Context;
import android.content.SharedPreferences;

import com.suyong.kakaobot.KakaoTalkListener;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

public class JSUtil extends ScriptableObject {
    private static Context context;

    public static void setContext(Context ctx) {
        context = ctx;
    }

    public JSUtil() {
        super();
    }

    @Override
    public String getClassName() {
        return "Util";
    }

    @JSStaticFunction
    public static void saveData(String key, ScriptableObject data) {
        SharedPreferences preference = context.getSharedPreferences("script_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, data.toString());
        editor.commit();
    }

    @JSStaticFunction
    public static ScriptableObject readData(String key) {
        JSScriptEngine engine = KakaoTalkListener.getJsEngines()[0];

        SharedPreferences preference = context.getSharedPreferences("script_data", Context.MODE_PRIVATE);
        return (ScriptableObject) engine.getContext().javaToJS(preference.getString(key, ""), engine.getScope());
    }
}
