package com.taimeitech.plugin.FlywaySQL.settings;

import com.intellij.openapi.ui.DialogWrapper;
import com.taimeitech.plugin.FlywaySQL.messages.MessageBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * created by mickey.wang on 2019/1/4
 **/
public class SettingEditorDialog extends DialogWrapper {

    private final JTextField versionField = new JTextField(5);

    public SettingEditorDialog(JComponent parent, String title) {
        super(parent, true);
        setTitle(title);
        init();
    }

    @Override
    public void show() {
        super.show();
        versionField.requestFocus();
    }

    public Setting getData() {
        Setting setting = new Setting("");
        setting.setVersion(versionField.getText());
        return setting;
    }

    public void setData(Setting setting) {
        versionField.setText(setting.getVersion());
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constr;

        // version
        constr = new GridBagConstraints();
        constr.gridx = 0;
        constr.gridy = 1;
        constr.anchor = GridBagConstraints.WEST;
        constr.insets = new Insets(5, 0, 0, 0);
        panel.add(new JLabel(MessageBundle.message("dialog.version.label")), constr);

        constr = new GridBagConstraints();
        constr.gridx = 1;
        constr.gridy = 1;
        constr.weightx = 1;
        constr.insets = new Insets(5, 10, 0, 0);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.anchor = GridBagConstraints.WEST;
        panel.add(versionField, constr);

        return panel;
    }
}
