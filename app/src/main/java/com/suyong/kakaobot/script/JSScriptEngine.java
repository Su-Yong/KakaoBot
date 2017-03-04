package com.suyong.kakaobot.script;

import android.util.Log;

import com.suyong.kakaobot.FileManager;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.UniqueTag;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;

public class JSScriptEngine {
    private ScriptThread thread;
    private String source;
    private String name;

    public JSScriptEngine() {
        thread = new ScriptThread();
    }

    public void setScript(String str) {
        this.source = str;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void run() {
        this.thread = new ScriptThread();
        this.thread.start();
    }

    public void stop() {
        this.thread.exit();
    }

    public Context getContext() {
        return this.thread.getContext();
    }

    public ScriptableObject getScope() {
        return this.thread.getScope();
    }

    public void invoke(String func, Object... parameter) {
        this.thread.invoke(func, parameter);
    }

    private class ScriptThread extends Thread {
        private Context context;
        private ScriptableObject scope;

        @Override
        public void run() {
            context = Context.enter();
            context.setOptimizationLevel(-1);

            Script script = context.compileString(source, "", 0, null);
            scope = context.initStandardObjects();

            try {
                ScriptableObject.defineClass(scope, JSKakaoTalk.class);
                script.exec(context, scope);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public Context getContext() {
            return context;
        }

        public ScriptableObject getScope() {
            return scope;
        }

        public void invoke(String func, Object... parameter) {
            Function function = (Function) scope.get(func);
            function.call(context, scope, scope, parameter);
        }

        public void exit() {
            context.exit();
        }
    }
}
