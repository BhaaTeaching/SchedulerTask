package com.scheduler.task.java.services;

import com.scheduler.task.java.dtos.CatalogDto;
import com.scheduler.task.java.dtos.SchedulerTaskDto;

import java.util.List;

public interface FileBasedTasksRepository {
    List<SchedulerTaskDto> searchAndRunPassedTasks(CatalogDto catalogDto);
}
