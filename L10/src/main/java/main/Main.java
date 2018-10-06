package main;


import base.DBService;
import base.UserDataSet;
import connection.DBServiceImpl;

/**
 * mysql> CREATE USER 'user'@'localhost' IDENTIFIED BY 'qwerty';
 * mysql> GRANT ALL PRIVILEGES ON * . * TO 'user'@'localhost';
 * mysql> select user, host from mysql.user;
 * mysql> create database hw10;
 * mysql> SET GLOBAL time_zone = '+3:00';
 */

public class Main {
    public static void main(String[] args) throws Exception {
        new Main().run();
    }

    private void run() throws Exception {
        try (DBService dbService = getDbService()) {
            System.out.println(dbService.getMetaData());
            dbService.prepareTables();
            dbService.save(new UserDataSet("Дядя Федор", 6));
            UserDataSet res = dbService.load(1, UserDataSet.class);
            System.out.println(res);
        }
    }

    private DBService getDbService() {
        return new DBServiceImpl();
    }
}
