package com.suyong.kakaobot.script;

import com.suyong.kakaobot.KakaoTalkListener;

public class PythonKakaoTalk {
    public static void send(String room, String message) {
        KakaoTalkListener.send(room, message);
    }
}
