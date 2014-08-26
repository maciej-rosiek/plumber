package com.rosiek.plumber;

import java.util.List;

public interface TaskProcessor<TaskType extends Task<?>, TaskItemType extends TaskItem<?>> {

    void processItems(TaskType task, List<TaskItemType> taskItems);

    void completeTask(TaskType task);

}
