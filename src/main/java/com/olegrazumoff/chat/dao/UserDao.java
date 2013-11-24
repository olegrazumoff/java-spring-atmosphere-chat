package com.olegrazumoff.chat.dao;

import com.olegrazumoff.chat.model.Message;
import com.olegrazumoff.chat.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Oleg
 * Date: 17.11.13
 * Time: 1:08
 * To change this template use File | Settings | File Templates.
 */
public interface UserDao {
    void add(User user);
    User get(String uniqueId);
    List<User> getAll();
    void remove(User user);
    void truncate();
}
