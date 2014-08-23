package com.rosiek.plumber;

public class FileTask implements Task<String> {

    private final String file;

    public FileTask(String file) {
        this.file = file;
    }

    @Override
    public String getPayload() {
        return file;
    }

    @Override
    public String toString() {
        return "FileTask{" +
                "file='" + file + '\'' +
                '}';
    }
}
