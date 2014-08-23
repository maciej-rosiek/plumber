package com.rosiek.plumber;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class SimpleTaskItemRepository implements TaskItemRepository {

    private Map<Task<?>, List<TaskItem<?>>> items = Maps.newHashMap();

    @Override
    public List<TaskItem<?>> readItems(Task<?> task, int limit) {
        List<TaskItem<?>> taskItems = items.get(task);
        Preconditions.checkArgument(!taskItems.isEmpty(), "This task doesn't have any items: [%s]", task);
        if (limit < taskItems.size()) {
            items.put(task, taskItems.subList(limit, taskItems.size()));
            return taskItems.subList(0, limit);
        }
        else {
            items.put(task, Lists.newArrayList());
            return taskItems;
        }
    }

    @Override
    public boolean hasMoreItems(Task<?> task) {
        return !items.get(task).isEmpty();
    }

    @Override
    public void removeItems(Task<?> task) {
        items.remove(task);
    }

    @Override
    public void add(Task<?> task, List<TaskItem<?>> taskItems) {
        items.put(task, taskItems);
    }

}
