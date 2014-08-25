package com.rosiek.plumber;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import java.util.List;

import com.google.common.collect.Lists;

public class FileTaskItemRepository implements TaskItemRepository {

    private static final int LINE_SEPARATOR_SIZE = System.lineSeparator().length();

    @Override
    public List<TaskItem<?>> readItems(final Task<?> task, final int limit) {

        final FileTaskPayload payload = (FileTaskPayload) task.getPayload();
        final List<TaskItem<?>> taskItems = Lists.newArrayList();
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

                    final TaskItem<?> taskItem = new FileTaskItem(line);
                    taskItems.add(taskItem);
                }
            }

            payload.setCharsProcessed(payload.getCharsProcessed() + charsProcessed
                    + taskItems.size() * LINE_SEPARATOR_SIZE);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error while reading file to process task: %s", task), e);
        }

        return taskItems;
    }

    @Override
    public boolean hasMoreItems(final Task<?> task) {
        final FileTaskPayload payload = (FileTaskPayload) task.getPayload();
        try(final LineNumberReader reader = new LineNumberReader(new FileReader(payload.getFile()))) {
            reader.skip(payload.getCharsProcessed());

            final String line = reader.readLine();
            return (line != null && !line.isEmpty()) || reader.read() >= 0;
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error while reading file to process task: %s", task), e);
        }
    }

    @Override
    public void removeItems(final Task<?> task) {
        throw new UnsupportedOperationException("Removing items is not yet implemented");
    }

    @Override
    public void add(final Task<?> task, final List<TaskItem<?>> taskItems) {
        throw new UnsupportedOperationException("Adding items is not yet implemented");
    }

}
