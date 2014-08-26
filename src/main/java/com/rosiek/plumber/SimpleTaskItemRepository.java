package com.rosiek.plumber;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SimpleTaskItemRepository implements TaskItemRepository<Task<?>, TaskItem<?>> {

    private Map<Task<?>, List<TaskItem<?>>> items = Maps.newHashMap();

    @Override
    public List<TaskItem<?>> readItems(final Task<?> task, final int limit) {
        final List<TaskItem<?>> taskItems = items.get(task);
        Preconditions.checkArgument(!taskItems.isEmpty(), "This task doesn't have any items: [%s]", task);
        if (limit < taskItems.size()) {
            items.put(task, taskItems.subList(limit, taskItems.size()));
            return taskItems.subList(0, limit);
        } else {
            items.put(task, Lists.<TaskItem<?>>newArrayList());
            return taskItems;
        }
    }

    @Override
    public boolean hasMoreItems(final Task<?> task) {
        return !items.get(task).isEmpty();
    }

    @Override
    public void removeItems(final Task<?> task) {
        items.remove(task);
    }

    @Override
    public void add(final Task<?> task, final List<TaskItem<?>> taskItems) {
        items.put(task, taskItems);
    }

}
