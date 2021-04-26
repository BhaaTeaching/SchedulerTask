package com.scheduler.task.java;

import com.scheduler.task.java.dtos.CatalogDto;
import com.scheduler.task.java.dtos.SchedulerTaskDto;
import com.scheduler.task.java.enums.SchedulerTaskType;
import com.scheduler.task.java.services.FileBasedTasksRepository;
import com.scheduler.task.java.services.FileBasedTasksRepositoryImpl;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileBasedTasksRepository fileBasedTasksRepository = new FileBasedTasksRepositoryImpl();

        SchedulerTaskDto schedulerTaskDto1 = new SchedulerTaskDto(1, "first", SchedulerTaskType.COMMAND);
        schedulerTaskDto1.setSchedulerTaskTime("12:39");
        SchedulerTaskDto schedulerTaskDto2 = new SchedulerTaskDto(2, "second", SchedulerTaskType.SCRIPT);
        schedulerTaskDto2.setSchedulerTaskTime("12:40");

        CatalogDto catalogDto = new CatalogDto(List.of(schedulerTaskDto1, schedulerTaskDto2));
      //  while (true) {
            try {
                fileBasedTasksRepository.readSchedulerTasksFile();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            fileBasedTasksRepository.searchAndRunPassedTasks(catalogDto);
            fileBasedTasksRepository.runTasks(catalogDto);
//            Thread.sleep();
//        }

//        try {
//            fileBasedTasksRepository.readSchedulerTasksFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
    }
}
