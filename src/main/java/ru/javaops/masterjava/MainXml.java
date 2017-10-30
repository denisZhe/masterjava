package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MainXml {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) {
        String projectName = args[0];
        if (!projectName.isEmpty()) {
            try {
                Payload payload = JAXB_PARSER.unmarshal(
                        Resources.getResource("payload.xml").openStream());

                List<String> groupNames = payload.getProjects().getProject().stream()
                        .filter(project -> project.getTitle().equals(projectName))
                        .map(project -> project.getGroups().getGroup())
                        .collect(Collector.of(ArrayList::new,
                                (groupsNames, groups) -> {
                                    groupsNames.addAll(groups.stream().map(Group::getValue).collect(Collectors.toList()));
                                },
                                (left, right) -> {
                                    left.addAll(right);
                                    return left;
                                }));

                payload.getUsers().getUser().stream()
                        .filter(user -> !Collections.disjoint(user.getGroupNames().getGroupName(), groupNames))
                        .forEach(user -> System.out.println(user.getFullName()));
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
            }
        }
    }
}
