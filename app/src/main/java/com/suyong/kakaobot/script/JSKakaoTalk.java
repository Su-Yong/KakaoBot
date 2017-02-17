package com.suyong.kakaobot.script;

import com.suyong.kakaobot.KakaoTalkListener;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

import java.util.ArrayList;

import static com.suyong.kakaobot.KakaoTalkListener.getSessions;

public class JSKakaoTalk extends ScriptableObject {

    @Override
    public String getClassName() {
        return "KakaoTalk";
    }

    @JSStaticFunction
    public static void send(String room, String message) {
        if (message == null) {
            KakaoTalkListener.Session[] sessions = KakaoTalkListener.getSessions().toArray(new KakaoTalkListener.Session[0]);
            KakaoTalkListener.send(sessions[sessions.length - 1].room, message);
        } else {
            KakaoTalkListener.send(room, message);
        }
    }
}
