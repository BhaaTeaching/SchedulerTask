package com.scheduler.task.java.services;

import com.scheduler.task.java.dtos.CatalogDto;
import com.scheduler.task.java.dtos.SchedulerTaskDto;
import com.scheduler.task.java.enums.SchedulerTaskType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadTasksImpl implements ReadTasks {
    private final static String xmlFileName = "/Users/bhaa.rizik/workspace/SchedulerTask/src/main/java/com/scheduler/task/java/resources/tasks.xml";

    @Override
    public CatalogDto readSchedulerTasksFile() throws IOException, ParserConfigurationException, SAXException {
        List<SchedulerTaskDto> schedulerTaskDtos = new ArrayList<SchedulerTaskDto>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(xmlFileName);
        NodeList list = document.getElementsByTagName("SchedulerTask");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                SchedulerTaskDto schedulerTaskDto = new SchedulerTaskDto(Long.parseLong(element.getAttribute("id")),
                        element.getAttribute("Name"), SchedulerTaskType.valueOf(element.getAttribute("Type")));
                schedulerTaskDto.setSchedulerTaskTime(element.getAttribute("Time"));
                schedulerTaskDto.setSchedulerTaskDetails(element.getAttribute("Details"));
                if (!element.getAttribute("BasedOn").equals("")) {
                    schedulerTaskDto.setSchedulerTaskBasedOn(Long.parseLong(element.getAttribute("BasedOn")));
                }
                schedulerTaskDtos.add(schedulerTaskDto);
            }
        }
        return new CatalogDto(schedulerTaskDtos);
    }
}
