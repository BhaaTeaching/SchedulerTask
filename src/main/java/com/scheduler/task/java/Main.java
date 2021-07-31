package com.scheduler.task.java;

import com.scheduler.task.java.dtos.CatalogDto;
import com.scheduler.task.java.dtos.SchedulerTaskDto;
import com.scheduler.task.java.services.FileBasedTasksRepository;
import com.scheduler.task.java.services.FileBasedTasksRepositoryImpl;
import com.scheduler.task.java.services.ReadTasks;
import com.scheduler.task.java.services.ReadTasksImpl;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ReadTasks readTasks = new ReadTasksImpl();
        CatalogDto catalogDto = null;
        try {
            catalogDto = readTasks.readSchedulerTasksFile();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        execute(catalogDto);

    }

    private static void execute(CatalogDto catalogDto) {
        ExecutorService executorService;

        if (catalogDto != null) {
            Map<Long, Long> idToBasedOnId = mapIdToBasedOnId(catalogDto);
            FileBasedTasksRepository fileBasedTasksRepository = new FileBasedTasksRepositoryImpl(catalogDto, null, idToBasedOnId);
            List<SchedulerTaskDto> passedSchedulerTaskDtos = fileBasedTasksRepository.searchAndRunPassedTasks(catalogDto);
            executorService = Executors.newFixedThreadPool(catalogDto.getSchedulerTaskDtos().size());

            ExecutorService finalExecutorService = executorService;
            getNotExecutedTasks(catalogDto.getSchedulerTaskDtos(), passedSchedulerTaskDtos).forEach(schedulerTask -> {
                Runnable fileBasedTasksRepositoryThread = new FileBasedTasksRepositoryImpl(catalogDto, schedulerTask, idToBasedOnId);
                finalExecutorService.execute(fileBasedTasksRepositoryThread);
            });
            executorService.shutdown();
            while (!executorService.isTerminated()) {
            }
            System.out.println("Finish Execution ");
        }
    }

    private static List<SchedulerTaskDto> getNotExecutedTasks(List<SchedulerTaskDto> allTasks, List<SchedulerTaskDto> passedTasks) {
        return allTasks.stream().filter(task -> !passedTasks.contains(task)).collect(Collectors.toList());
    }

    private static Map<Long, Long> mapIdToBasedOnId(CatalogDto catalogDto) {
        Map<Long, Long> idToBasedOnId = new HashMap<>();
        catalogDto.getSchedulerTaskDtos().forEach(schedulerTask -> {
            if (schedulerTask.getSchedulerTaskBasedOn() != 0) {
                idToBasedOnId.put(schedulerTask.getSchedulerTaskId(), schedulerTask.getSchedulerTaskBasedOn());
            }
        });
        return idToBasedOnId;
    }
}
