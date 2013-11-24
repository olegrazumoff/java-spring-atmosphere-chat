package com.olegrazumoff.chat.dao;

import com.olegrazumoff.chat.model.Message;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Oleg
 * Date: 17.11.13
 * Time: 1:08
 * To change this template use File | Settings | File Templates.
 */
public interface MessageDao {
    void add(Message message);
    List<Message> get(int count);
    void truncate();
}
