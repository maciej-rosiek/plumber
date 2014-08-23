package com.rosiek.plumber;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class TaskProcessingServiceTest {

    private TaskProcessingService taskProcessingService;

    private static final String FILE = "/data/file2.txt";

    @Before
    public void setUp() {
        TaskRepository taskRepository = new SimpleTaskRepository();
        TaskItemRepository taskItemRepository = new FileTaskItemRepository();

        TaskProcessor taskProcessor = new TaskProcessor() {
            @Override
            public void processItems(Task<?> task, List<TaskItem<?>> taskItems) {
                System.out.println(String.format("Processing task: [%s], items: [%s]", task, taskItems != null ? taskItems.size() : 0));
                if (taskItems != null) {
                    for (TaskItem<?> taskItem : taskItems) {
                        FileItem fileItem = (FileItem) taskItem.getPayload();
                        System.out.println("  - " + fileItem.getContents().size());
                    }
                }
            }

            @Override
            public void completeTask(Task<?> task) {
                System.out.println(String.format("Finished task: [%s]", task));
            }
        };
        taskProcessingService = new TaskProcessingService(taskRepository, taskItemRepository, taskProcessor);
    }

    @Test
    public void test() throws IOException {
        int chunks = 10;
        int linesPerChunk = 10000;
        Task<?> task = new FileTask(FILE);
        System.out.println("Importing file chunks into queue");

        System.out.println("Counting lines in file");
        int numberOfLines = 0;
        try (FileInputStream stream = new FileInputStream(FILE)) {
            byte[] buffer = new byte[8192];
            int n;
            while ((n = stream.read(buffer)) > 0) {
                for (int i = 0; i < n; i++) {
                    if (buffer[i] == '\n') numberOfLines++;
                }
            }
        }

        System.out.printf("Calculated number of lines: %d%n", numberOfLines);
        List<TaskItem<?>> items = Lists.newArrayList();
        for (int i = 0; i < numberOfLines; i += linesPerChunk) {
            items.add(new FileTaskItem(new FileItem(i, i + linesPerChunk)));
        }
        System.out.printf("Imported %d items%n", items.size());
        taskProcessingService.add(task, items);

        System.out.println("Starting to process task");
        while (taskProcessingService.process(chunks)) ;
    }

}
