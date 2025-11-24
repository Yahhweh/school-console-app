package kegly.organisation.schoolconsoleapp;

import kegly.organisation.schoolconsoleapp.service.SchoolDataFactory;
import kegly.organisation.schoolconsoleapp.ui.ConsoleMenu;

public class Main {

    public static void main(String[] args) {
        try {
            SchoolDataFactory dataFactory = new SchoolDataFactory();

            dataFactory.initializeDatabase();

            ConsoleMenu ui = new ConsoleMenu(dataFactory);
            ui.start();

        } catch (Exception e) {
            System.err.println("Critical application error:");
            e.printStackTrace();
        }
    }
}