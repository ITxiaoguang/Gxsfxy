package top.yokey.gxsfxy.share;

import top.yokey.gxsfxy.share.themes.classic.ClassicTheme;

@SuppressWarnings("all")
public enum OnekeyShareTheme {

    CLASSIC(0, new ClassicTheme());

    private final int value;
    private final OnekeyShareThemeImpl impl;

    OnekeyShareTheme(int value, OnekeyShareThemeImpl impl) {
        this.value = value;
        this.impl = impl;
    }

    public int getValue() {
        return value;
    }

    public OnekeyShareThemeImpl getImpl() {
        return impl;
    }

    public static OnekeyShareTheme fromValue(int value) {
        for (OnekeyShareTheme theme : OnekeyShareTheme.values()) {
            if (theme.value == value) {
                return theme;
            }
        }
        return CLASSIC;
    }

}