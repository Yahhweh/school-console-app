package kegly.organisation.schoolconsoleapp;

import kegly.organisation.schoolconsoleapp.dao.CourseDao;
import kegly.organisation.schoolconsoleapp.dao.GroupDao;
import kegly.organisation.schoolconsoleapp.dao.StudentDao;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcCourseDao;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcGroupDao;
import kegly.organisation.schoolconsoleapp.dao.jdbc.JdbcStudentDao;
import kegly.organisation.schoolconsoleapp.db.ConnectionProvider;
import kegly.organisation.schoolconsoleapp.db.SchemaLoader;
import kegly.organisation.schoolconsoleapp.service.SchoolDataFacade;
import kegly.organisation.schoolconsoleapp.ui.ConsoleMenu;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        try {
            ConnectionProvider provider = new ConnectionProvider();

            try (Connection connection = provider.getConnection()) {
                new SchemaLoader().runScript(connection, "schema.sql");
            }

            StudentDao studentDao = new JdbcStudentDao(provider);
            GroupDao groupDao = new JdbcGroupDao(provider);
            CourseDao courseDao = new JdbcCourseDao(provider);

            SchoolDataFacade facade = new SchoolDataFacade(studentDao, groupDao, courseDao);

            facade.initializeDatabase();

            ConsoleMenu ui = new ConsoleMenu(facade);
            ui.start();

        } catch (Exception e) {
            System.err.println("Critical application error:");
            e.printStackTrace();
        }
    }
}