package com.rosiek.plumber;

import java.util.List;

public interface TaskProcessor {

    void processItems(Task<?> task, List<TaskItem<?>> taskItems);

    void completeTask(Task<?> task);

}
