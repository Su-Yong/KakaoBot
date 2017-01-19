package com.suyong.kakaobot;

public class Type {
    public static class Message {
        public String room;
        public String sender;
        public String message;
    }

    public static class Project {
        public IconType icon;
        public String title;
        public String subtitle;
        public boolean disabled;
    }

    public enum IconType {
        JS,
        PYTHON,
    }
}
