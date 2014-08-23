package com.rosiek.plumber;

public class SimpleTask implements Task<String> {

    private final String message;

    public SimpleTask(String message) {
        this.message = message;
    }

    @Override
    public String getPayload() {
        return message;
    }

    @Override
    public String toString() {
        return "SimpleTask{" +
                "message='" + message + '\'' +
                '}';
    }
}
