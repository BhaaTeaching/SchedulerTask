package com.scheduler.task.java.services;

import com.scheduler.task.java.dtos.SchedulerTaskDto;
import java.io.IOException;
import java.util.TimerTask;


public class Executor extends TimerTask {

    SchedulerTaskDto schedulerTaskDto;

    public Executor(SchedulerTaskDto schedulerTaskDto) {
        this.schedulerTaskDto = schedulerTaskDto;
    }

    @Override
    public void run() {
        System.out.println("Task " + schedulerTaskDto.getSchedulerTaskId() + " Started");
        try {
            Runtime.getRuntime().exec(schedulerTaskDto.getSchedulerTaskDetails());
            schedulerTaskDto.setDone(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
