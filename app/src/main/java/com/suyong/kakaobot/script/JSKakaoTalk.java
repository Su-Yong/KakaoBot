package com.suyong.kakaobot.script;

import com.suyong.kakaobot.KakaoTalkListener;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

public class JSKakaoTalk extends ScriptableObject {

    @Override
    public String getClassName() {
        return "KakaoTalk";
    }

    @JSStaticFunction
    public static void send(String room, String message) {
        KakaoTalkListener.send(room, message);
    }
}
