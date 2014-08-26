package com.rosiek.plumber;

import java.util.List;

public interface TaskItemRepository<TaskType extends Task<?>, TaskItemType extends TaskItem<?>> {

    List<TaskItemType> readItems(TaskType task, int limit);

    boolean hasMoreItems(TaskType task);

    void removeItems(TaskType task);

    void add(TaskType task, List<TaskItemType> taskItems);

}
