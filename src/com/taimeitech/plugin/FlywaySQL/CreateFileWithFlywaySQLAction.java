package com.taimeitech.plugin.FlywaySQL;

import com.intellij.ide.actions.CreateFileAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.taimeitech.plugin.FlywaySQL.messages.MessageBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by mickey.wang on 2019/1/4
 **/
@SuppressWarnings("ComponentNotRegistered")
public class CreateFileWithFlywaySQLAction extends CreateFileAction {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    private final String type;
    private final String version;
    private final String defaultExtension;
    private final String template;

    public CreateFileWithFlywaySQLAction(String type, String version, String defaultExtension, String template) {
        super(type + " " + version + " file", MessageBundle.message("action.create.new.file.description", type), StdFileTypes.PLAIN_TEXT.getIcon());
        this.type = type;
        this.version = version;
        this.defaultExtension = defaultExtension;
        this.template = template;
    }

    @Override
    public boolean isDumbAware() {
        return CreateFileWithFlywaySQLAction.class.equals(getClass());
    }

    @NotNull
    @Override
    protected PsiElement[] invokeDialog(Project project, PsiDirectory directory) {
        MyInputValidator validator = new MyValidator(project, directory);
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            try {
                return validator.create("test");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            Messages.showInputDialog(project, "Description:", "Create New FlywaySQL File", (Icon)null, (String)null, validator);

//            CreateFileWithFlywaySqlEditorDialog createFileWithFlywaySqlEditorDialog = new CreateFileWithFlywaySqlEditorDialog(project, MessageBundle.message("title.new.file", type));
//            createFileWithFlywaySqlEditorDialog.showAndGet();
//
//            String typeText = createFileWithFlywaySqlEditorDialog.typeField.getText();
//            String descriptionText = createFileWithFlywaySqlEditorDialog.descriptionField.getText();
//
//            CreateFileAction.MkDirs mkdirs = new CreateFileAction.MkDirs(descriptionText, directory);
//
////            mkdirs.directory.createFile(generateFileName(typeText, getFileName(mkdirs.newName)));
//            String fileName = generateFileName(typeText, getFileName(mkdirs.newName));
//            System.out.println(fileName);
////            try {
//
//                directory.createFile("V1_2_20190723155648__12_Administrator_223.sql");
////                new File(mkdirs.directory.toString().substring(13) + "\\" + fileName.replace("\"", "")).createNewFile();
//                VirtualFileManager.getInstance().syncRefresh();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
            return validator.getCreatedElements();
        }
    }

    @Nullable
    @Override
    protected String getDefaultExtension() {
        return defaultExtension;
    }

    @NotNull
    @Override
    protected PsiElement[] create(String newName, PsiDirectory directory) throws Exception {
        CreateFileAction.MkDirs mkDirs = new CreateFileAction.MkDirs(newName, directory);
        return new PsiElement[]{mkDirs.directory.createFile(generateFileName(getFileName(mkDirs.newName)))};
    }

    @Override
    protected String getCommandName() {
        return MessageBundle.message("command.create.file", type);
    }

    @Override
    protected String getActionName(PsiDirectory directory, String newName) {
        return MessageBundle.message("progress.creating.file", type, directory.getVirtualFile().getPresentableUrl(), File.separator, newName);
    }

    private String generateFileName(String description) {
        String result = template;

        Matcher matcher = VARIABLE_PATTERN.matcher(result);
        Date timestamp = new Date();
        while (matcher.find()) {
            String variable = matcher.group(1);
            String replacement = "";

            if ("NOW".equals(variable)) {
                replacement = new SimpleDateFormat("yyyyMMddHHmmss").format(timestamp);
            } else if ("USER".equals(variable)) {
                replacement = System.getProperty("user.name");
            } else if ("NAME".equals(variable)) {
                replacement = description.replace(" ","_");
            } else if ("VERSION".equals(variable)) {
                replacement = version; // .replace(".","_")
            } else if (variable.startsWith("NOW")) {
                replacement = new SimpleDateFormat(variable.substring(4)).format(timestamp);
            }
            result = matcher.replaceFirst(replacement);
            matcher.reset(result);
        }
        result = result.replace("\"", "");
        return result;
    }
}
