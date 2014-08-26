package com.rosiek.plumber;

public interface  TaskRepository {

    Task getTask();

    boolean hasMoreTasks();

    void completeTask(Task task);

    void failTask(Task task, Throwable ex);

    void add(Task task);

    void saveProgress(Task<?> task);
}
