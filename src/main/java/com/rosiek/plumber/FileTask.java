package com.rosiek.plumber;

public class FileTask implements Task<FileTaskPayload> {

    private final FileTaskPayload payload;

    public FileTask(final FileTaskPayload payload) {
        this.payload = payload;
    }

    @Override
    public FileTaskPayload getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "FileTask{" + "payload='" + payload + '\'' + '}';
    }

}
