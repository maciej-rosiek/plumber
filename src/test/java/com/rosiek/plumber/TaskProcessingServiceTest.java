package com.rosiek.plumber;

import java.io.IOException;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TaskProcessingServiceTest {

    private TaskProcessingService taskProcessingService;

    private static final String FILE = "/data/file2.txt";

    @Before
    public void setUp() {
        final TaskRepository taskRepository = new SimpleTaskRepository();
        final TaskItemRepository taskItemRepository = new FileTaskItemRepository();

        final TaskProcessor taskProcessor = new TaskProcessor() {
            @Override
            public void processItems(final Task<?> task, final List<TaskItem<?>> taskItems) {
                System.out.println(String.format("Processing task: [%s], items: [%s]", task,
                        taskItems != null ? taskItems.size() : 0));
                for (final TaskItem<?> taskItem : taskItems) {
                    System.out.println("  - " + taskItem.getPayload());
                }
            }

            @Override
            public void completeTask(final Task<?> task) {
                System.out.println(String.format("Finished task: [%s]", task));
            }
        };
        taskProcessingService = new TaskProcessingService(taskRepository, taskItemRepository, taskProcessor);
    }

    @Test
    public void test() throws IOException {
        final Task<?> task = new FileTask(new FileTaskPayload(FILE));
        System.out.println("Importing file chunks into queue");

        System.out.println("Counting lines in file");

        taskProcessingService.add(task, null);

        System.out.println("Starting to process task");
        while (taskProcessingService.process(5)) { }
    }

}
