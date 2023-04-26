package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        UserService us = new UserServiceImpl();

        User[] users = new User[] {
                new User("Архип", "Марков", (byte) 11),
                new User("Бенедикт", "Щербаков", (byte) 11),
                new User("Ипполит", "Белов", (byte) 11),
                new User("Бронислав", "Сергеев", (byte) 11)
        };

        us.createUsersTable();
        for (User user :
                users) {
            us.saveUser(user.getName(), user.getLastName(), user.getAge());
        }

        for (User user :
                us.getAllUsers()) {
            System.out.println(user.toString());
        }
        us.removeUserById(1L);
        us.cleanUsersTable();
        us.dropUsersTable();
        Util.closeConnection();
    }
}
