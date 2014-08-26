package com.rosiek.plumber;

import java.util.LinkedList;

public class SimpleTaskRepository implements TaskRepository {

    private LinkedList<Task> tasks = new LinkedList<>();

    @Override
    public Task getTask() {
        return tasks.element();
    }

    @Override
    public boolean hasMoreTasks() {
        return !tasks.isEmpty();
    }

    @Override
    public void completeTask(final Task task) {
        tasks.remove();
    }

    @Override
    public void failTask(final Task task, final Throwable ex) {
        tasks.remove();
    }

    @Override
    public void add(final Task task) {
        tasks.add(task);
    }

    @Override
    public void saveProgress(final Task<?> task) {
        tasks.removeFirst();
        tasks.addFirst(task);
    }

}
