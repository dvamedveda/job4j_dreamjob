package ru.job4j.dream.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Сервлет для загрузки фото
 */
public class UploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        File repository = new File(System.getProperty("catalina.home") + File.separator + "bin" + File.separator);
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        Store store = PsqlStore.getInst();
        String path = "";
        boolean successUpload = true;
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File("images");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                if (!item.isFormField() && item.getName().equals("")) {
                    successUpload = false;
                    break;
                }
                if (!item.isFormField() && !item.getName().equals("")) {
                    path = folder + File.separator + item.getName();
                    File file = new File(path);
                    try (FileOutputStream out = new FileOutputStream(file);
                         InputStream in = item.getInputStream()) {
                        while (in.available() > 0) {
                            out.write(in.read());
                        }
                        out.flush();
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        int imageId = store.saveImage(path);
        String redirect = "";
        if (successUpload) {
            redirect = "/candidate/edit.jsp?id=" + req.getParameter("id") + "&image=" + imageId;
        } else {
            redirect = "/candidate/edit.jsp?id=" + req.getParameter("id") + "&image=-1";
        }
        resp.sendRedirect(req.getContextPath() + redirect);
    }
}