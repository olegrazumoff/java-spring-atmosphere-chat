package com.olegrazumoff.chat.dao;

import com.olegrazumoff.chat.model.Message;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
public class MessageDaoImpl implements MessageDao {
    @Autowired
    private SessionFactory sessionFactory;

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void add(Message message) {
        sessionFactory.getCurrentSession().save(message);
    }

    public List<Message> get(int count) {
        return getCurrentSession().createCriteria(Message.class).setMaxResults(10).addOrder(Order.desc("date")).list();
    }

    public void truncate() {
        Query query = getCurrentSession().createQuery(String.format("delete from %s", "Message"));
        query.executeUpdate();
    }
}
