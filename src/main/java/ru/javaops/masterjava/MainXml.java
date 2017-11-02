package ru.javaops.masterjava;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;
import ru.javaops.masterjava.xml.util.XsltProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class MainXml {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) throws IOException, XMLStreamException, TransformerException {
        String projectName = args[0];
        if (!projectName.isEmpty()) {
            printUsersByJAXB(projectName);
            System.out.println();
            Map<String, String> users = getUsersByStAX(projectName);
            System.out.println(users);
            usersToHmtl(users, projectName);
            transformGroupsToHtml(projectName);
        }
    }

    private static void printUsersByJAXB(String projectName) {
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

    private static Map<String, String> getUsersByStAX(String projectName) throws IOException, XMLStreamException {
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {

            Map<String, String> users = new HashMap<>();
            List<String> groupNames = new ArrayList<>();
            List<String> userGroupNames = new ArrayList<>();
            String fullName;
            String email;

            while (processor.startElement("Title", "Project")) {
                if (projectName.equals(processor.getText())) {
                    while (processor.startElement("Group", "Groups")) {
                        groupNames.add(processor.getText());
                    }
                }
            }

            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                email = processor.getAttribute("email");
                fullName = processor.getElementValue("fullName");
                while (processor.startElement("GroupName", "GroupNames")) {
                    userGroupNames.add(processor.getText());
                }

                if (!Collections.disjoint(userGroupNames, groupNames)) {
                    users.putIfAbsent(fullName, email);
                }
                userGroupNames.clear();
            }
            return users;
        }
    }

    private static void usersToHmtl(Map<String, String> users, String projectName) throws IOException {
        String html = html(
                head(
                        title("Users")
                ),
                body(
                        h1("Users in the project: " + projectName),
                        table(
                                tbody(
                                        tr(
                                                th("user"),
                                                th("email")
                                        ),
                                        each(users.entrySet(), i -> tr(
                                                td(i.getKey()),
                                                td(i.getValue())
                                        ))
                                )
                        ).attr("border", 1)
                )
        ).render();
        Files.write(Paths.get("./users.html"), html.getBytes());
    }

    private static void transformGroupsToHtml(String projectName) throws IOException, TransformerException {
        try (InputStream xslInputStream = Resources.getResource("groups.xsl").openStream();
             InputStream xmlInputStream = Resources.getResource("payload.xml").openStream()) {

            XsltProcessor processor = new XsltProcessor(xslInputStream);
            processor.setParameter("project", projectName);
            Files.write(Paths.get("./groups.html"), processor.transform(xmlInputStream).getBytes());
        }
    }
}
