package com.suyong.kakaobot;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.text.SpannableString;

import com.suyong.kakaobot.script.JSScriptEngine;
import com.suyong.kakaobot.script.PythonScriptEngine;

import java.util.ArrayList;

public class KakaoTalkListener extends NotificationListenerService {
    private static final String KAKAOTALK_PACKAGE = "com.kakao.talk";
    private static ArrayList<Notification.Action> sessions = new ArrayList<>();
    private static JSScriptEngine jsEngine;
    private static PythonScriptEngine pythonEngine;
    private static Context context;

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) { // @author ManDongI
        super.onNotificationPosted(statusBarNotification);

        if(statusBarNotification.getPackageName().equals(KAKAOTALK_PACKAGE)) {
            Notification.WearableExtender extender = new Notification.WearableExtender(statusBarNotification.getNotification());
            for(Notification.Action act : extender.getActions()) {
                if(act.getRemoteInputs() != null && act.getRemoteInputs().length > 0) {
                    if(act.title.toString().toLowerCase().contains("reply") || act.title.toString().toLowerCase().contains("Reply") || act.title.toString().toLowerCase().contains("답장")) {
                        String title = statusBarNotification.getNotification().extras.getString("android.title");
                        Object index = statusBarNotification.getNotification().extras.get("android.text");

                        context = getApplicationContext();

                        sessions.add(act);

                        if(jsEngine == null) {
                            jsEngine = new JSScriptEngine();
                        }
                        if(pythonEngine == null) {
                            pythonEngine = new PythonScriptEngine();
                        }

                        Type.Message message = parsingMessage(title, index);
                        jsEngine.invokeFunction("", new Object[]{ message.room, message.sender, message.message, message.room == message.sender });
                        pythonEngine.invokeFunction("", new Object[]{ message.room, message.sender, message.message, message.room == message.sender });
                    }
                }
            }
        }
    }

    public static void send(String room, String message) throws IllegalArgumentException { // @author ManDongI
        Notification.Action session = null;

        for(Notification.Action i : sessions) {
            if(i.title == room) {
                session = i;

                break;
            }
        }

        if(session == null) {
            throw new IllegalArgumentException("Can't find the room");
        }

        Intent sendIntent = new Intent();
        Bundle msg = new Bundle();
        for (RemoteInput inputable : session.getRemoteInputs()) msg.putCharSequence(inputable.getResultKey(), message);
        RemoteInput.addResultsToIntent(session.getRemoteInputs(), sendIntent, msg);

        try {
            session.actionIntent.send(context, 0, sendIntent);
        } catch (PendingIntent.CanceledException e) {

        }
    }

    private Type.Message parsingMessage(String title, Object index) {
        Type.Message result = new Type.Message();
        result.room = title;

        if(index instanceof String) {
            result.sender = title;
            result.message = (String) index;
        } else {
            String html = Html.toHtml((SpannableString) index);
            result.sender = Html.fromHtml(html.split("<b>")[1].split("</b>")[0]).toString();
            result.message = Html.fromHtml(html.split("</b>")[1].split("</p>")[0].substring(1)).toString();
        }

        return result;
    }
}
