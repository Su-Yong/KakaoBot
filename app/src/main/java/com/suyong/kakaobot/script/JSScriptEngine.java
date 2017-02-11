package com.suyong.kakaobot.script;

import android.util.Log;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.InvocationTargetException;

public class JSScriptEngine {
    private static String script;
    private static Context jsContext;
    private static ScriptableObject scope;

    public JSScriptEngine() {
        jsContext = Context.enter();
        jsContext.setOptimizationLevel(-1);
    }

    public Context getJsContext() {
        return this.jsContext;
    }

    public void execute() throws Exception {
        try {
            Script script_real = jsContext.compileString(script, "", 0, null);
            scope = jsContext.initStandardObjects();

            ScriptableObject.defineClass(scope, JSKakaoTalk.class);
            script_real.exec(jsContext, scope);

            Log.d("KakaoBot/JSEngine", "Execute succeed");
        } catch (Exception e) {
            throw new Exception("[-ScriptSplitTag-]" + e.toString());
        }
    }

    public void setScriptSource(String source) {
        this.script = source;
    }

    public void invokeFunction(String name, Object[] parameter) {
        Function func = (Function) scope.get(name, scope);
        func.call(jsContext, scope, scope, parameter);
        Log.d("KakaoBot/JSEngine", "invoke function: " + name);
    }
}
