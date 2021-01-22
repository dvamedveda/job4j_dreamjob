package ru.job4j.dream.servlet;

import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Сервлет для скачивания фото по id.
 */
public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Store store = PsqlStore.getInst();
        int imageId = Integer.parseInt(req.getParameter("image_id"));
        String imageCatalog = System.getProperty("catalina.home") + File.separator + "bin" + File.separator;
        String imagePath = imageCatalog + store.getImagePath(imageId);
        File file = new File(imagePath);
        resp.setContentType("image/png");
        resp.setContentType("name=" + file.getName());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {
            while (in.available() > 0) {
                out.write(in.read());
            }
            out.flush();
        }
    }
}