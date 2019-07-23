package com.taimeitech.plugin.FlywaySQL.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import static com.taimeitech.plugin.FlywaySQL.messages.MessageBundle.message;

/**
 * created by mickey.wang on 2019/1/4
 **/
public class SettingsConfigurable implements Configurable {
    private final Project project;
    private SettingsPanel panel;

    public SettingsConfigurable(Project project) {
        this.project = project;
    }

    private class SettingsPanel extends JPanel {
        private final JBList settingList;
        private final DefaultListModel<Setting> listModel;

        private boolean isModified = false;

        public SettingsPanel() {
            listModel = new DefaultListModel<>();
            initializeListModel();
            settingList = new JBList(listModel);
            settingList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setText(((Setting) value).getVersion());
                    return this;
                }
            });
            setLayout(new BorderLayout());
            add(ToolbarDecorator.createDecorator(settingList)
                    .setAddAction(anActionButton -> addNew())
                    .setRemoveAction(anActionButton -> removeSelected())
                    .setEditAction(anActionButton -> editSelected())
                    .disableUpAction()
                    .disableDownAction()
                    .createPanel());
        }

        private void apply() {
            List<Setting> newList = new ArrayList<Setting>(listModel.getSize());
            Enumeration<Setting> elements = listModel.elements();
            while (elements.hasMoreElements()) {
                newList.add(elements.nextElement());
            }
            PluginSettings.getInstance(project).setConfiguration(newList);
            isModified = false;
            if (null!=listModel&&listModel.getSize() == 1) {
                ToolbarDecorator.createDecorator(settingList).disableAddAction();
            }
        }

        private void reset() {
            listModel.removeAllElements();
            initializeListModel();
            isModified = false;
        }

        private void initializeListModel() {
            for (Setting setting : PluginSettings.getInstance(project).settings) {
                listModel.add(listModel.size(), setting);
            }
        }

        private void addNew() {
            SettingEditorDialog dlg = createSettingEditorDialog(message("dialog.add.title"));
            Setting setting = new Setting("");
            dlg.setData(setting);
            if (dlg.showAndGet()) {
                insertSetting(dlg.getData(), true);
            }
            settingList.requestFocus();
            isModified = true;
        }

        private void removeSelected() {
            int selectedIndex = settingList.getSelectedIndex();
            if (selectedIndex != -1) {
                listModel.remove(selectedIndex);
            }

            settingList.requestFocus();
            isModified = true;
        }

        private void editSelected() {
            int selectedIndex = settingList.getSelectedIndex();
            if (selectedIndex != -1) {
                Setting setting = listModel.getElementAt(selectedIndex);
                SettingEditorDialog dlg = createSettingEditorDialog(message("dialog.edit.title"));
                dlg.setData(setting);
                if (dlg.showAndGet()) {
                    listModel.set(selectedIndex, dlg.getData());
                }
            }

            settingList.requestFocus();
            isModified = true;
        }

        private SettingEditorDialog createSettingEditorDialog(String title) {
            return new SettingEditorDialog(panel, title);
        }

        private void insertSetting(@NotNull Setting filename, boolean setSelected) {
            listModel.add(listModel.size(), filename);
            if (setSelected) {
                settingList.setSelectedValue(filename, true);
            }
        }
    }

    @Nls
    @Override
    public String getDisplayName() {
        return message("settings.name");
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        // No help
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (panel == null) {
            panel = new SettingsPanel();
        }
        return panel;
    }

    @Override
    public boolean isModified() {
        return panel != null && panel.isModified;
    }

    @Override
    public void apply() {
        if (panel != null) {
            panel.apply();
        }
    }

    @Override
    public void reset() {
        if (panel != null) {
            panel.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        panel = null;
    }
}
