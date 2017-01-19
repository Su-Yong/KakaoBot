package com.suyong.kakaobot.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.InvocationTargetException;

public class JSScriptEngine implements ScriptEngine {
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

    @Override
    public boolean execute() {
        try {
            Script script_real = jsContext.compileString(script, "", 0, null);
            scope = jsContext.initStandardObjects();

            ScriptableObject.defineClass(scope, JSKakaoTalk.class);
            script_real.exec(jsContext, scope);

            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public void setScriptSource(String source) {
        this.script = source;
    }

    @Override
    public void invokeFunction(String name, Object[] parameter) {
        Function func = (Function) scope.get(name, scope);
        func.call(jsContext, scope, scope, parameter);
    }
}
