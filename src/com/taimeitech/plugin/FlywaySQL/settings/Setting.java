package com.taimeitech.plugin.FlywaySQL.settings;

import com.taimeitech.plugin.FlywaySQL.messages.MessageBundle;

/**
 * created by mickey.wang on 2019/1/4
 **/
public class Setting {

    private String name;
    private String version;
    private String defaultExtension;
    private String template;

    public Setting() {
        this.version = "";

        this.name = MessageBundle.message("settings.name");
        this.defaultExtension = MessageBundle.message("settings.defaultExtension");
        this.template = MessageBundle.message("settings.template");
    }

    public Setting(String version) {
        this.version = version;

        this.name = MessageBundle.message("settings.name");
        this.defaultExtension = MessageBundle.message("settings.defaultExtension");
        this.template = MessageBundle.message("settings.template");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDefaultExtension() {
        return defaultExtension;
    }

    public void setDefaultExtension(String defaultExtension) {
        this.defaultExtension = defaultExtension;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
