package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS myDbTest.users (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(45) NULL, " +
                    "lastName VARCHAR(45) NULL, " +
                    "age TINYINT NULL, " +
                    "UNIQUE INDEX id_UNIQUE (id ASC));").executeUpdate();
            session.getTransaction().commit();
            System.out.println("----Таблица создана/уже имеется в базе данных----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            session.getTransaction().commit();
            System.out.println("----Таблица удалена из базы данных----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction tx = null;
        try (Session session = Util.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            session.persist(new User(name, lastName, age));
            tx.commit();
            System.out.printf("----Пользователь с именем %s добавлен в таблицу----\n", name);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.printf("----Прозошла ошибка при добавлении пользователя с именем %s\n" +
                    "Пользователь не был добавлен в таблицу!----\n", name);
        }

    }

    @Override
    public void removeUserById(long id) {
        Transaction tx = null;
        try (Session session = Util.getSessionFactory().openSession()){
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
            }
            System.out.printf("----Произошла ошибка при удалении пользователя!\n" +
                    "Пользователь с id '%s' не был удален!----\n", id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
            System.out.println("----Таблица users очищена----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
