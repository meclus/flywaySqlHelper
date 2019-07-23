package com.taimeitech.plugin.FlywaySQL.settings;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.taimeitech.plugin.FlywaySQL.CreateFileWithFlywaySQLAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * created by mickey.wang on 2019/1/4  , scheme = StorageScheme.DIRECTORY_BASED
 **/
@State(name= "FlywaySQL", storages = {
        @Storage(StoragePathMacros.WORKSPACE_FILE),
        @Storage(StoragePathMacros.MODULE_FILE + "/flyway-sql.xml")
})
public class PluginSettings implements PersistentStateComponent<PluginSettings>, ProjectComponent {

    public List<Setting> settings = new ArrayList<>();

    @Nullable
    @Override
    public PluginSettings getState() {
        return this;
    }

    @Override
    public void loadState(PluginSettings settings) {
        XmlSerializerUtil.copyBean(settings, this);
    }

    public static PluginSettings getInstance(Project project) {
        return project.getComponent(PluginSettings.class);
    }

    private void addActions() {
        if (settings != null && !settings.isEmpty()) {
            ActionManager am = ActionManager.getInstance();
            DefaultActionGroup newGroup = (DefaultActionGroup) am.getAction("NewGroup");
            String anchorId = "NewFile";
            for (Setting setting : settings) {
                CreateFileWithFlywaySQLAction action =
                        new CreateFileWithFlywaySQLAction(setting.getName(), setting.getVersion(), setting.getDefaultExtension(), setting.getTemplate());
                String actionId = "FlywaySQL." + setting.getName();
                am.registerAction(actionId, action);
                newGroup.addAction(action, new Constraints(Anchor.AFTER, anchorId));
                anchorId = actionId;
            }
        }
    }

    public void setConfiguration(List<Setting> settings) {
        removeActions();
        this.settings = settings;
        addActions();
    }

    public void removeActions() {
        ActionManager am = ActionManager.getInstance();
        DefaultActionGroup newGroup = (DefaultActionGroup) am.getAction("NewGroup");
        for (Setting setting : settings) {
            AnAction action = am.getAction("FlywaySQL." + setting.getName());
            am.unregisterAction("FlywaySQL." + setting.getName());
            newGroup.remove(action);
        }
    }

    @Override
    public void projectOpened() {
        addActions();
    }

    @Override
    public void projectClosed() {
//        removeActions();
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return  "FlywaySQLSettings";
    }
}
