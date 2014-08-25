package com.rosiek.plumber;

import java.util.List;

public interface TaskItemRepository {

    List<TaskItem<?>> readItems(Task<?> task, int limit);

    boolean hasMoreItems(Task<?> task);

    void removeItems(Task<?> task);

    void add(Task<?> task, List<TaskItem<?>> taskItems);

}
