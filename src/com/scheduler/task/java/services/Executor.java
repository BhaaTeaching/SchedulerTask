package com.scheduler.task.java.services;

import com.scheduler.task.java.dtos.SchedulerTaskDto;

import java.util.TimerTask;

public class Executor extends TimerTask {

    SchedulerTaskDto schedulerTaskDto;

    public Executor(SchedulerTaskDto schedulerTaskDto) {
        this.schedulerTaskDto = schedulerTaskDto;
    }

    public void run() {
        System.out.println("*eat all the people*");
        schedulerTaskDto.getSchedulerTaskType();
    }
}
