package com.scheduler.task.java.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

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


public class FileBasedTasksRepositoryImpl implements FileBasedTasksRepository {
    private final static String xmlFileName = "/Users/bhaa.rizik/workspace/SchedulerTask/src/com/scheduler/task/java/resources/tasks.xml";

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
                schedulerTaskDtos.add(
                        new SchedulerTaskDto(Long.parseLong(element.getAttribute("id")),
                                element.getAttribute("Name"), SchedulerTaskType.valueOf(element.getAttribute("Type"))));

            }
        }
        return new CatalogDto(schedulerTaskDtos);
    }

    @Override
    public void searchAndRunPassedTasks(CatalogDto catalogDto) {
        Calendar taskExecutionHour = Calendar.getInstance();
        Calendar currentHour = Calendar.getInstance();
        currentHour.set(Calendar.HOUR_OF_DAY, currentHour.getTime().getHours());
        currentHour.set(Calendar.MINUTE, currentHour.getTime().getMinutes());
        currentHour.set(Calendar.SECOND, 0);

        catalogDto.getSchedulerTaskDtos().forEach(schedulerTask -> {
            if (schedulerTask.getSchedulerTaskTime() != null) {
                String[] parts = schedulerTask.getSchedulerTaskTime().split(":");
                taskExecutionHour.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                taskExecutionHour.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                taskExecutionHour.set(Calendar.SECOND, 0);
                if (currentHour.before(taskExecutionHour)) {
                    applyRunningTask(schedulerTask);
                }
            }
        });
    }

    @Override
    public void runTasks(CatalogDto catalogDto) {
        Map<Long, Long> idToBasedOnId = mapIdToBasedOnId(catalogDto);

        catalogDto.getSchedulerTaskDtos().forEach(schedulerTask -> {
            new Runnable() {
                @Override
                public void run() {
                    if (schedulerTask.getSchedulerTaskBasedOn() != 0) {
                        catalogDto.getSchedulerTaskDtos().forEach(schedulerTask1 -> {
                            if (schedulerTask1.getSchedulerTaskId() == idToBasedOnId.get(schedulerTask.getSchedulerTaskId())) {
                                if (schedulerTask1.isDone()) {
                                    applyRunningTask(schedulerTask);
                                }
                            }
                        });
                    } else {
                        applyRunningTask(schedulerTask);
                    }
                    //      timer.cancel();
                }
            };
        });
        System.out.println("start");


    }

    private Map<Long, Long> mapIdToBasedOnId(CatalogDto catalogDto) {
        Map<Long, Long> idToBasedOnId = new HashMap<>();
        catalogDto.getSchedulerTaskDtos().forEach(schedulerTask -> {
            if (schedulerTask.getSchedulerTaskBasedOn() != 0) {
                idToBasedOnId.put(schedulerTask.getSchedulerTaskId(), schedulerTask.getSchedulerTaskBasedOn());
            }
        });
        return idToBasedOnId;
    }

    private void applyRunningTask(SchedulerTaskDto schedulerTask) {
        Calendar date = Calendar.getInstance();
        Timer timer = new Timer();
        Executor executor = new Executor(schedulerTask);
        date.set(Calendar.HOUR, Integer.parseInt(schedulerTask.getSchedulerTaskTime().split(":")[0]));
        date.set(Calendar.HOUR, Integer.parseInt(schedulerTask.getSchedulerTaskTime().split(":")[1]));
        System.out.println("Task " + schedulerTask.getSchedulerTaskId() + "Started");
        timer.schedule(executor, date.getTime());
    }
}
