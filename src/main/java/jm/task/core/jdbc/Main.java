package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService us = new UserServiceImpl();

        us.createUsersTable();


        us.saveUser("Архип", "Марков", (byte) 11);
        us.saveUser("Демьян", "Быков", (byte) 32);
        us.saveUser("Григорий", "Авдеев", (byte) 34);
        us.saveUser("Захар", "Мясников", (byte) 22);

        for (User user :
                us.getAllUsers()) {
            System.out.println(user);
        }

        us.removeUserById(1L);
        us.removeUserById(10L);

        for (User user :
                us.getAllUsers()) {
            System.out.println(user);
        }

        us.cleanUsersTable();

        us.dropUsersTable();

    }
}
