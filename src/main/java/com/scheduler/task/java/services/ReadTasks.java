package com.scheduler.task.java.services;

import com.scheduler.task.java.dtos.CatalogDto;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface ReadTasks {
    CatalogDto readSchedulerTasksFile() throws IOException, ParserConfigurationException, SAXException;

}
