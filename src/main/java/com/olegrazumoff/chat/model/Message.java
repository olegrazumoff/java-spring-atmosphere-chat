package com.olegrazumoff.chat.model;


import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Oleg
 * Date: 16.11.13
 * Time: 22:11
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="MESSAGE")
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="ID")
    private Integer id;

    @Column(name="AUTHOR")
    private String author;

    @Column(name="TEXT")
    private String text;

    @Column(name="DATE")
    private Date date;

    public Message() {

    }

    public Message(String author, String text, Date date) {
        setAuthor(author);
        setText(text);
        setDate(date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
