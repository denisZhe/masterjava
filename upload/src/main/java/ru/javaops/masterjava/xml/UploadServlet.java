package ru.javaops.masterjava.xml;

import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB)
public class UploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("fileUpload.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Set<User> users = new HashSet<>();

        Collection<Part> parts = req.getParts();
        for (Part part : parts) {
            if ("file".equals(part.getName())) {
                try (StaxStreamProcessor processor = new StaxStreamProcessor(part.getInputStream())) {
                    JaxbParser parser = new JaxbParser(User.class);
                    while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                        users.add(parser.unmarshal(processor.getReader(), User.class));
                    }
                } catch (XMLStreamException | JAXBException e) {
                    e.printStackTrace();
                }
            }
        }

        req.setAttribute("users", users);
        req.getRequestDispatcher("/users.jsp").forward(req, resp);
    }
}
