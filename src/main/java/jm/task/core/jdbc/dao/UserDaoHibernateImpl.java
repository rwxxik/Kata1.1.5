package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.getFactory().openSession()) {
            session.beginTransaction();
            NativeQuery query = session.createNativeQuery("CREATE TABLE IF NOT EXISTS myDbTest.users (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(45) NULL, " +
                    "lastName VARCHAR(45) NULL, " +
                    "age TINYINT NULL, " +
                    "UNIQUE INDEX id_UNIQUE (id ASC));");
            query.executeUpdate();
            session.getTransaction().commit();
            System.out.println("----Таблица создана/уже имеется в базе данных----");
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getFactory().openSession()) {
            session.beginTransaction();
            NativeQuery query = session.createNativeQuery("DROP TABLE IF EXISTS users");
            query.executeUpdate();
            session.getTransaction().commit();
            System.out.println("----Таблица удалена из базы данных----");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Session session = Util.getFactory().openSession();
        session.beginTransaction();
        try {
            session.persist(user);
            session.getTransaction().commit();
            System.out.printf("----Пользователь с именем %s добавлен в таблицу----\n", name);
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.printf("----Прозошла ошибка при добавлении пользователя с именем %s\n" +
                    "Пользователь не был добавлен в таблицу!----\n", name);

        } finally {
            session.close();
        }

    }

    @Override
    public void removeUserById(long id) {
        Session session = Util.getFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                System.out.printf("----Пользователь c id '%s' удален из базы данных----\n", id);
            } else {
                System.out.printf("----Пользователя с id '%s' не существует в базе данных----\n", id);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
                System.out.printf("----Произошла ошибка при удалении пользователя!\n" +
                        "Пользователь с id '%s' не был удален!----\n", id);
            }
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getFactory().openSession()) {
            session.beginTransaction();
            NativeQuery query = session.createNativeQuery("DELETE FROM users");
            query.executeUpdate();
            session.getTransaction().commit();
            System.out.println("----Таблица users очищена----");
        }
    }
}
