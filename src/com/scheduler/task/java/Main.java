package com.scheduler.task.java;

import com.scheduler.task.java.dtos.CatalogDto;
import com.scheduler.task.java.services.FileBasedTasksRepository;
import com.scheduler.task.java.services.FileBasedTasksRepositoryImpl;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        FileBasedTasksRepository fileBasedTasksRepository = new FileBasedTasksRepositoryImpl();
        CatalogDto catalogDto = null;
        try {
            catalogDto = fileBasedTasksRepository.readSchedulerTasksFile();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        fileBasedTasksRepository.searchAndRunPassedTasks(catalogDto);
        fileBasedTasksRepository.runTasks(catalogDto);
    }
}
