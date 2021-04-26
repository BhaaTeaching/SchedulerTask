package com.scheduler.task.java.dtos;

import java.util.List;

public class CatalogDto {
    public CatalogDto(List<SchedulerTaskDto> schedulerTaskDtos) {
        this.schedulerTaskDtos = schedulerTaskDtos;
    }

    public void setSchedulerTaskDtos(List<SchedulerTaskDto> schedulerTaskDtos) {
        this.schedulerTaskDtos = schedulerTaskDtos;
    }

    public List<SchedulerTaskDto> getSchedulerTaskDtos() {
        return schedulerTaskDtos;
    }

    List<SchedulerTaskDto> schedulerTaskDtos;
}
