package com.rosiek.plumber;

import com.google.common.collect.Lists;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

public class FileTaskItemRepository extends SimpleTaskItemRepository {

    @Override
    public List<TaskItem<?>> readItems(Task<?> task, int limit) {
        List<TaskItem<?>> taskItems = super.readItems(task, limit);

        try (LineNumberReader reader = new LineNumberReader(new FileReader((String)task.getPayload()))) {

            boolean first = true;
            for (TaskItem<?> taskItem : taskItems) {
                FileItem fileItem = (FileItem) taskItem.getPayload();
                if (first) {
                    for (int i = 0 ; i < fileItem.getBegin(); i++) {
                        reader.readLine();
                    }
                    first = false;
                }
                List<String> lines = Lists.newArrayList();
                for (int i = fileItem.getBegin() ; i < fileItem.getEnd(); i++) {
                    String line = reader.readLine();
                    if (line != null) {
                        lines.add(line);
                    }
                }
                fileItem.setContents(lines);
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error while reading file to process task: %s", task), e);
        }

        return taskItems;
    }

}
