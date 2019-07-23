package com.taimeitech.plugin.FlywaySQL.messages;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import javax.annotation.Nullable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;

/**
 * created by mickey.wang on 2019/1/4
 **/
public class MessageBundle {
    @Nullable
    private static Reference<ResourceBundle> bundleRef;

    private static final String BUNDLE = "com.taimeitech.plugin.FlywaySQL.messages.MessageBundle";

    private MessageBundle() {
    }

    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    @NotNull
    private static ResourceBundle getBundle() {
        @Nullable ResourceBundle bundle = null;
        if (bundleRef != null) {
            bundle = bundleRef.get();
        }

        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE);
            bundleRef = new SoftReference<>(bundle);
        }

        return bundle;
    }
}
