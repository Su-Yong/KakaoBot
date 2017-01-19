package com.suyong.kakaobot.script;

public interface ScriptEngine {
    boolean execute();
    void setScriptSource(String source);
    void invokeFunction(String name, Object[] parameter);
}
