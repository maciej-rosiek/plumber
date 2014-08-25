package com.rosiek.plumber;

public class FileTaskPayload {

    private String file;
    private long charsProcessed;
    private int linesProcessed;

    public FileTaskPayload(final String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setFile(final String file) {
        this.file = file;
    }

    public long getCharsProcessed() {
        return charsProcessed;
    }

    public void setCharsProcessed(final long charsProcessed) {
        this.charsProcessed = charsProcessed;
    }

    public int getLinesProcessed() {
        return linesProcessed;
    }

    public void setLinesProcessed(final int linesProcessed) {
        this.linesProcessed = linesProcessed;
    }

    @Override
    public String toString() {
        return "FileTaskPayload{" + "file='" + file + '\'' + ", charsProcessed=" + charsProcessed + ", linesProcessed="
                + linesProcessed + '}';
    }
}
