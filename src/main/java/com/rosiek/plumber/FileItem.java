package com.rosiek.plumber;

import java.util.List;

public class FileItem {

    private final int begin;
    private final int end;
    private List<String> contents;

    public FileItem(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public List<String> getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "FileItem{" +
                "begin=" + begin +
                ", end=" + end +
                ", contents='" + contents + '\'' +
                '}';
    }
}
