package com.olegrazumoff.chat.model;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Oleg
 * Date: 17.11.13
 * Time: 1:37
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="ID")
    private Integer id;

    @Column(name="NAME")
    private String name;

    @Column(name="UNIQUEID")
    private String uniqueId;

    public User() {

    }

    public User(String name, String uniqueId) {
        setName(name);
        setUniqueId(uniqueId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
