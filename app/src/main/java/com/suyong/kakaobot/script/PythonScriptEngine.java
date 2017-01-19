package com.suyong.kakaobot.script;

import org.python.antlr.PythonParser;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import py4j.GatewayServer;

public class PythonScriptEngine implements ScriptEngine {
    private String script;
    private PythonInterpreter interpreter;

    public PythonScriptEngine() {
        interpreter = new PythonInterpreter();
    }

    @Override
    public boolean execute() {
        try {
            PythonKakaoTalk kakaoTalk = new PythonKakaoTalk();
            GatewayServer server = new GatewayServer(kakaoTalk);
            server.start();

            interpreter.exec(this.script);

            return true;
        } catch (Exception err) {
            return false;
        }
    }

    @Override
    public void setScriptSource(String source) {
        this.script = source;
    }

    @Override
    public void invokeFunction(String name, Object[] parameter) {
        PyObject func = interpreter.get(name);
        func.__call__((PyObject[]) parameter);
    }
}
