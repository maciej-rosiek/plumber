package com.rosiek.plumber;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleTaskRepository implements TaskRepository {

    private Queue<Task> tasks = new LinkedList<>();

    @Override
    public Task getTask() {
        return tasks.element();
    }

    @Override
    public void completeTask(Task task) {
        tasks.remove();
    }

    @Override
    public void failTask(Task task, Throwable ex) {
        tasks.remove();
    }

    @Override
    public void add(Task task) {
        tasks.add(task);
    }
}
