package com.olegrazumoff.chat.dao;

import com.olegrazumoff.chat.model.Message;
import com.olegrazumoff.chat.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Query;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Oleg
 * Date: 20.11.13
 * Time: 9:04
 * To change this template use File | Settings | File Templates.
 */
@Repository
@Transactional
public class UserDaoImpl implements UserDao {
    @Autowired
    private SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    public List<User> getAll() {
        return getCurrentSession().createCriteria(User.class).list();
    }

    public User get(String uniqueId) {
        return (User) getCurrentSession().createQuery("from User where uniqueId = " + "'" + uniqueId + "'").uniqueResult();
    }

    public void remove(User user) {
        sessionFactory.getCurrentSession().delete(user);
    }

    public void truncate() {
        org.hibernate.Query query = getCurrentSession().createQuery(String.format("delete from %s", "User"));
        query.executeUpdate();
    }
}
