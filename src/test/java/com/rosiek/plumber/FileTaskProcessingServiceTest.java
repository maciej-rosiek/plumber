package com.rosiek.plumber;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.TemporaryFolder;

public class FileTaskProcessingServiceTest {

    private static final int NUMBER_OF_LINES = 100_000;
    private static final String FILE_LINE = "This is a temporary line for testing";

    private TaskProcessingService taskProcessingService;

    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();

    private String file;
    private boolean done;
    private int linesProcessed;

    @Before
    public void setUp() throws IOException {
        final TaskRepository taskRepository = new SimpleTaskRepository();
        final TaskItemRepository taskItemRepository = new FileTaskItemRepository();
        final TaskProcessor taskProcessor = new TaskProcessor<FileTask, FileTaskItem>() {
            @Override
            public void processItems(final FileTask task, final List<FileTaskItem> taskItems) {
                linesProcessed += taskItems.size();
            }

            @Override
            public void completeTask(final FileTask task) {
                done = true;
            }
        };

        taskProcessingService = new TaskProcessingService(taskRepository, taskItemRepository, taskProcessor);

        file = createTestFile();
    }

    @Test
    public void testProcessingService() throws IOException {
        taskProcessingService.add(new FileTask(new FileTaskPayload(file)), null);

        FileTask task = null;
        for (int i = 0; i < 10; i++) {
            task = (FileTask) taskProcessingService.process(NUMBER_OF_LINES / 10);
        }

        Assert.assertNotNull(task);
        Assert.assertTrue(done);
        Assert.assertEquals((FILE_LINE.length() + System.lineSeparator().length()) * NUMBER_OF_LINES,
            task.getPayload().getCharsProcessed());
        Assert.assertEquals(NUMBER_OF_LINES, linesProcessed);
    }

    private String createTestFile() throws IOException {
        final String file = tempDir.newFile().getAbsolutePath();

        try(final BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < NUMBER_OF_LINES; i++) {
                bw.write(FILE_LINE);
                bw.newLine();
            }
        }

        return file;
    }

}
