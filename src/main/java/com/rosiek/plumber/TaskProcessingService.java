package com.rosiek.plumber;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskProcessingService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskProcessingService.class);

    private final TaskRepository taskRepository;
    private final TaskItemRepository<Task<?>, TaskItem<?>> taskItemRepository;
    private final TaskProcessor<Task<?>, TaskItem<?>> taskProcessor;

    @SuppressWarnings("unchecked")
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

    public Task<?> process(final int limit) {
        if (!taskRepository.hasMoreTasks()) {
            LOG.info("No tasks available for processing.");
            return null;
        } else {
            final Task<?> task = taskRepository.getTask();
            try {
                final List<TaskItem<?>> taskItems = fetch(limit, task);
                process(task, taskItems);
                write(task);
            } catch (Throwable ex) {
                LOG.error("Error while processing task: %s", task, ex);
                taskRepository.failTask(task, ex);
                taskItemRepository.removeItems(task);
            }

            return task;
        }

    }

    private void process(final Task<?> task, final List<TaskItem<?>> taskItems) {
        taskProcessor.processItems(task, taskItems);
    }

    private List<TaskItem<?>> fetch(final int limit, final Task<?> task) {
        return taskItemRepository.readItems(task, limit);
    }

    private void write(final Task<?> task) {
        taskRepository.saveProgress(task);
        if (!taskItemRepository.hasMoreItems(task)) {
            taskRepository.completeTask(task);
            taskProcessor.completeTask(task);
        }
    }
}
