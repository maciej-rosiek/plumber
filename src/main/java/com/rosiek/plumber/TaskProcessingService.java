package com.rosiek.plumber;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskProcessingService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskProcessingService.class);

    private final TaskRepository taskRepository;
    private final TaskItemRepository taskItemRepository;
    private final TaskProcessor taskProcessor;

    public TaskProcessingService(final TaskRepository taskRepository, final TaskItemRepository taskItemRepository,
            final TaskProcessor taskProcessor) {
        this.taskRepository = taskRepository;
        this.taskItemRepository = taskItemRepository;
        this.taskProcessor = taskProcessor;
    }

    public void add(final Task<?> task, final List<TaskItem<?>> taskItems) {
        if (taskItems != null && !taskItems.isEmpty()) {
            taskItemRepository.add(task, taskItems);
        }

        taskRepository.add(task);
    }

    public boolean process(final int limit) {
        final Task<?> task = taskRepository.getTask();
        try {
            final List<TaskItem<?>> taskItems = fetch(limit, task);
            process(task, taskItems);
            return write(task);
        } catch (Throwable ex) {
            LOG.error("Error while processing task: %s", task, ex);
            taskRepository.failTask(task, ex);
            taskItemRepository.removeItems(task);
            return true;
        }
    }

    private void process(final Task<?> task, final List<TaskItem<?>> taskItems) {
        taskProcessor.processItems(task, taskItems);
    }

    private List<TaskItem<?>> fetch(final int limit, final Task<?> task) {
        return taskItemRepository.readItems(task, limit);
    }

    private boolean write(final Task<?> task) {
        taskRepository.saveProgress(task);
        if (!taskItemRepository.hasMoreItems(task)) {
            taskRepository.completeTask(task);
            taskProcessor.completeTask(task);
            return false;
        }

        return true;
    }
}
