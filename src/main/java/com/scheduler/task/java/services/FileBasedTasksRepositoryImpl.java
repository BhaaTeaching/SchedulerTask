package com.scheduler.task.java.services;

import com.scheduler.task.java.dtos.CatalogDto;
import com.scheduler.task.java.dtos.SchedulerTaskDto;

import java.util.*;


public class FileBasedTasksRepositoryImpl implements FileBasedTasksRepository, Runnable {
    SchedulerTaskDto schedulerTask;
    CatalogDto catalogDto;
    Map<Long, Long> idToBasedOnId;

    public FileBasedTasksRepositoryImpl() {

    }

    public FileBasedTasksRepositoryImpl(CatalogDto catalogDto, SchedulerTaskDto schedulerTask, Map<Long, Long> idToBasedOnId) {
        super();
        this.schedulerTask = schedulerTask;
        this.catalogDto = catalogDto;
        this.idToBasedOnId = idToBasedOnId;
    }

    @Override
    public List<SchedulerTaskDto> searchAndRunPassedTasks(CatalogDto catalogDto) {
        System.out.println("Execute passed tasks ..");
        Calendar taskExecutionHour = Calendar.getInstance();
        Calendar currentHour = Calendar.getInstance();
        List<SchedulerTaskDto> passedSchedulerTaskDtos = new LinkedList<>();

        catalogDto.getSchedulerTaskDtos().forEach(schedulerTask -> {
            if (schedulerTask.getSchedulerTaskTime() != null) {
                String[] parts = schedulerTask.getSchedulerTaskTime().split(":");
                taskExecutionHour.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                taskExecutionHour.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                taskExecutionHour.set(Calendar.SECOND, 0);
                if (currentHour.after(taskExecutionHour)) {
                    passedSchedulerTaskDtos.add(schedulerTask);
                    isOnBased(catalogDto, schedulerTask);
                }
            }
        });
        System.out.println("Passed tasks has been executed successfully ..");
        return passedSchedulerTaskDtos;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " schedulerTask Start = " + schedulerTask.getSchedulerTaskName());
        isOnBased(catalogDto, schedulerTask);
        System.out.println(Thread.currentThread().getName() + " schedulerTask End = " + schedulerTask.getSchedulerTaskName());
    }

    private void applyRunningTask(SchedulerTaskDto schedulerTask) {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        Calendar date = Calendar.getInstance();
        Timer timer = new Timer();
        Executor executor = new Executor(schedulerTask);

        date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(schedulerTask.getSchedulerTaskTime().split(":")[0]));
        date.set(Calendar.MINUTE, Integer.parseInt(schedulerTask.getSchedulerTaskTime().split(":")[1]));
        date.set(Calendar.SECOND, 0);
        if (Arrays.stream(stackTraceElement).noneMatch(x -> x.getMethodName().equals("searchAndRunPassedTasks"))) {
            timer.schedule(executor, date.getTime());
            System.out.println("The task has been scheduled");
        } else {
            executor.run();
        }
    }

    private void isOnBased(CatalogDto catalogDto, SchedulerTaskDto schedulerTask) {
        if (schedulerTask.getSchedulerTaskBasedOn() == 0) {
            applyRunningTask(schedulerTask);
        } else {
            catalogDto.getSchedulerTaskDtos().forEach(schedulerTask1 -> {
                if (schedulerTask1.getSchedulerTaskId() == idToBasedOnId.get(schedulerTask.getSchedulerTaskId())) {
                    if (schedulerTask1.isDone()) {
                        applyRunningTask(schedulerTask);
                    }
                }
            });
        }
    }
}
