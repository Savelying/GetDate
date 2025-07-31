package ru.savelying.getdate.controller.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Role;
import ru.savelying.getdate.model.Status;
import ru.savelying.getdate.utils.ConnectUtils;

import java.util.List;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("genders", Gender.values());
        sce.getServletContext().setAttribute("statuses", Status.values());
        sce.getServletContext().setAttribute("roles", Role.values());
        sce.getServletContext().setAttribute("availPageSizes", List.of(10, 20, 50, 100));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectUtils.closePool();
    }
}
