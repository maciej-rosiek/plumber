package com.rosiek.plumber;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import java.util.List;

import com.google.common.collect.Lists;

public class FileTaskItemRepository implements TaskItemRepository<FileTask, FileTaskItem> {

    private static final int LINE_SEPARATOR_SIZE = System.lineSeparator().length();

    @Override
    public List<FileTaskItem> readItems(final FileTask task, final int limit) {

        final FileTaskPayload payload = task.getPayload();
        final List<FileTaskItem> taskItems = Lists.newArrayList();
        try(final LineNumberReader reader = new LineNumberReader(new FileReader(payload.getFile()))) {
            long charsProcessed = 0;

            boolean first = true;
            for (int i = 0; i < limit; i++) {
                if (first) {
                    reader.skip(payload.getCharsProcessed());
                    first = false;
                }

                final String line = reader.readLine();
                if (line != null) {
                    charsProcessed += line.length();

                    final FileTaskItem taskItem = new FileTaskItem(line);
                    taskItems.add(taskItem);
                }
            }

            payload.setCharsProcessed(payload.getCharsProcessed() + charsProcessed
                    + taskItems.size() * LINE_SEPARATOR_SIZE);
            payload.setLinesProcessed(payload.getLinesProcessed() + taskItems.size());
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error while reading file to process task: %s", task), e);
        }

        return taskItems;
    }

    @Override
    public boolean hasMoreItems(final FileTask task) {
        final FileTaskPayload payload = task.getPayload();
        try(final LineNumberReader reader = new LineNumberReader(new FileReader(payload.getFile()))) {
            reader.skip(payload.getCharsProcessed());

            final String line = reader.readLine();
            return (line != null && !line.isEmpty()) || reader.read() >= 0;
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error while reading file to process task: %s", task), e);
        }
    }

    @Override
    public void removeItems(final FileTask task) {
        throw new UnsupportedOperationException("Removing items is not supported");
    }

    @Override
    public void add(final FileTask task, final List<FileTaskItem> taskItems) {
        throw new UnsupportedOperationException("Adding items is not supported");
    }

}
