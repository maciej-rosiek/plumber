package com.rosiek.plumber;

public class FileTaskItem implements TaskItem<FileItem> {

    private FileItem payload;

    public FileTaskItem(FileItem payload) {
        this.payload = payload;
    }

    @Override
    public FileItem getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "FileTaskItem{" +
                "payload=" + payload +
                '}';
    }
}
