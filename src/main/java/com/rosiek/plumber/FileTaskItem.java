package com.rosiek.plumber;

public class FileTaskItem implements TaskItem<String> {

    private String line;

    public FileTaskItem(final String line) {
        this.line = line;
    }

    @Override
    public String getPayload() {
        return line;
    }

    @Override
    public String toString() {
        return "FileTaskItem{" + "line='" + line + '\'' + '}';
    }

}
