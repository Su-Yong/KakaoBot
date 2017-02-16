package com.suyong.kakaobot.script;

import android.util.Log;
/*
import org.python.antlr.PythonParser;
import org.python.core.PyInstance;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;*/

public class PythonScriptEngine {/*
    private String script;
    private PythonInterpreter interpreter;
    private PyInstance instance;

    public PythonScriptEngine() {
        interpreter = new PythonInterpreter();
    }

    public void execute() throws Exception {
        try {
            interpreter.set("KakaoTalk", new PythonKakaoTalk());
            interpreter.exec(this.script);
        } catch (Exception e) {
            throw new Exception("SCRIPTSPLITTAG" + e.toString());
        }
    }

    public void setScriptSource(String source) {
        this.script = source;
    }

    public void invokeFunction(String name, PyObject[] parameter) {
        PyObject function = interpreter.get(name);
        function.__call__(parameter);
    }*/
}
