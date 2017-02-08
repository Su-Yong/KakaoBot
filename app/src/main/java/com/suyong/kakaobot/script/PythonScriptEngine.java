package com.suyong.kakaobot.script;

import org.python.antlr.PythonParser;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import py4j.GatewayServer;

public class PythonScriptEngine {
    private String script;
    private PythonInterpreter interpreter;

    public PythonScriptEngine() {
        interpreter = new PythonInterpreter();
    }

    public void execute() {
        try {
            PythonKakaoTalk kakaoTalk = new PythonKakaoTalk();
            GatewayServer server = new GatewayServer(kakaoTalk);
            server.start();

            interpreter.exec(this.script);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void setScriptSource(String source) {
        this.script = source;
    }

    public void invokeFunction(String name, PyObject[] parameter) {
        PyObject func = interpreter.get(name);
        func.__call__(parameter);
    }
}
