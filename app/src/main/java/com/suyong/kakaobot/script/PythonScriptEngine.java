package com.suyong.kakaobot.script;

import android.util.Log;

import org.python.antlr.PythonParser;
import org.python.core.PyInstance;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import py4j.GatewayServer;

public class PythonScriptEngine {
    private String script;
    private PythonInterpreter interpreter;
    private PyInstance instance;

    public PythonScriptEngine() {
        interpreter = new PythonInterpreter();
    }

    public void execute() throws Exception {
        try {
            PythonKakaoTalk kakaoTalk = new PythonKakaoTalk();
            GatewayServer server = new GatewayServer(kakaoTalk);
            server.start();
        } catch (Exception e) {
            throw new Exception("[-ScriptSplitTag-]" + e.toString());
        }
        try {
            interpreter.exec(this.script);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScriptSource(String source) {
        this.script = source;
    }

    public void invokeFunction(String name, PyObject[] parameter) {
        String params = "";
        for(int i = 0; i < parameter.length; i++) {
            params += parameter[i];
            if(i < parameter.length - 1) {
                params += ", ";
            }
        }

        Log.d("KakaoBot/PythonEngine", this.script + " ----- " + name + "(" + params + ")" + " ----- " + interpreter);
        interpreter.eval(name + "(" + parameter.toString() + ")");
    }
}
