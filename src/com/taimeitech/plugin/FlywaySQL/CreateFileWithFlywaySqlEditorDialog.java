package com.taimeitech.plugin.FlywaySQL;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.taimeitech.plugin.FlywaySQL.messages.MessageBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * created by mickey.wang on 2019/1/4
 **/
public class CreateFileWithFlywaySqlEditorDialog extends DialogWrapper {

    public final JTextField typeField = new JTextField(3);
    public final JTextField descriptionField = new JTextField(50);

    public CreateFileWithFlywaySqlEditorDialog(Project project, String title) {
        super(project, true);
        setTitle(title);
        init();
    }

    @Override
    public void show() {
        super.show();
        typeField.requestFocus();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constr;

        // type
        constr = new GridBagConstraints();
        constr.gridx = 0;
        constr.gridy = 0;
        constr.anchor = GridBagConstraints.WEST;
        constr.insets = new Insets(5, 0, 0, 0);
        panel.add(new JLabel(MessageBundle.message("dialog.type.label")), constr);

        constr = new GridBagConstraints();
        constr.gridx = 1;
        constr.gridy = 0;
        constr.weightx = 1;
        constr.insets = new Insets(5, 10, 0, 0);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.anchor = GridBagConstraints.WEST;
        panel.add(typeField, constr);

        // description
        constr = new GridBagConstraints();
        constr.gridx = 0;
        constr.gridy = 1;
        constr.anchor = GridBagConstraints.WEST;
        constr.insets = new Insets(5, 0, 0, 0);
        panel.add(new JLabel(MessageBundle.message("dialog.description.label")), constr);

        constr = new GridBagConstraints();
        constr.gridx = 1;
        constr.gridy = 1;
        constr.weightx = 1;
        constr.insets = new Insets(5, 10, 0, 0);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.anchor = GridBagConstraints.WEST;
        panel.add(descriptionField, constr);

        return panel;
    }
}
