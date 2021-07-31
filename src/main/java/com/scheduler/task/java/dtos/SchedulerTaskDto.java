package com.scheduler.task.java.dtos;

import com.scheduler.task.java.enums.SchedulerTaskType;

public class SchedulerTaskDto {

    private long schedulerTaskId;
    private String SchedulerTaskName;
    private SchedulerTaskType schedulerTaskType;
    private String schedulerTaskDetails;
    private String schedulerTaskTime;
    private long schedulerTaskBasedOn;

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    private boolean isDone = false;

    public SchedulerTaskDto(long schedulerTaskId, String schedulerTaskName, SchedulerTaskType schedulerTaskType) {
        this.schedulerTaskId = schedulerTaskId;
        SchedulerTaskName = schedulerTaskName;
        this.schedulerTaskType = schedulerTaskType;
    }

    public long getSchedulerTaskId() {
        return schedulerTaskId;
    }

    public void setSchedulerTaskId(long schedulerTaskId) {
        this.schedulerTaskId = schedulerTaskId;
    }

    public String getSchedulerTaskName() {
        return SchedulerTaskName;
    }

    public void setSchedulerTaskName(String schedulerTaskName) {
        SchedulerTaskName = schedulerTaskName;
    }

    public SchedulerTaskType getSchedulerTaskType() {
        return schedulerTaskType;
    }

    public void setSchedulerTaskType(SchedulerTaskType schedulerTaskType) {
        this.schedulerTaskType = schedulerTaskType;
    }

    public String getSchedulerTaskDetails() {
        return schedulerTaskDetails;
    }

    public void setSchedulerTaskDetails(String schedulerTaskDetails) {
        this.schedulerTaskDetails = schedulerTaskDetails;
    }

    public String getSchedulerTaskTime() {
        return schedulerTaskTime;
    }

    public void setSchedulerTaskTime(String schedulerTaskTime) {
        this.schedulerTaskTime = schedulerTaskTime;
    }

    public long getSchedulerTaskBasedOn() {
        return schedulerTaskBasedOn;
    }

    public void setSchedulerTaskBasedOn(long schedulerTaskBasedOn) {
        this.schedulerTaskBasedOn = schedulerTaskBasedOn;
    }
}
