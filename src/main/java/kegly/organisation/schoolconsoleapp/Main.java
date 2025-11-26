package kegly.organisation.schoolconsoleapp;

import kegly.organisation.schoolconsoleapp.service.SchoolDataFacade;
import kegly.organisation.schoolconsoleapp.ui.ConsoleMenu;

public class Main {

    public static void main(String[] args) {
        try {
            SchoolDataFacade dataFactory = new SchoolDataFacade();

            dataFactory.initializeDatabase();

            ConsoleMenu ui = new ConsoleMenu(dataFactory);
            ui.start();

        } catch (Exception e) {
            System.err.println("Critical application error:");
            e.printStackTrace();
        }
    }
}