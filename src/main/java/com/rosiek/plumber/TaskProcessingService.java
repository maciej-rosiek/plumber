package com.rosiek.plumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TaskProcessingService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskProcessingService.class);

    private final TaskRepository taskRepository;
    private final TaskItemRepository taskItemRepository;
    private final TaskProcessor taskProcessor;

    public TaskProcessingService(TaskRepository taskRepository, TaskItemRepository taskItemRepository, TaskProcessor taskProcessor) {
        this.taskRepository = taskRepository;
        this.taskItemRepository = taskItemRepository;
        this.taskProcessor = taskProcessor;
    }

    public void add(Task<?> task, List<TaskItem<?>> taskItems) {
        taskItemRepository.add(task, taskItems);
        taskRepository.add(task);
    }

    public boolean process(int limit) {
        Task<?> task = taskRepository.getTask();
        try {
            List<TaskItem<?>> taskItems = taskItemRepository.readItems(task, limit);
            taskProcessor.processItems(task, taskItems);
            if (!taskItemRepository.hasMoreItems(task)) {
                taskRepository.completeTask(task);
                taskProcessor.completeTask(task);
                return false;
            }
        } catch (Throwable ex) {
            LOG.error("Error while processing task: %s", task, ex);
            taskRepository.failTask(task, ex);
            taskItemRepository.removeItems(task);
        }
        return true;
    }
}
