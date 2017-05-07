package jp.techacademy.shino.oomori.taskapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.io.Serializable;
import java.sql.Struct;
import java.util.Date;

/**
 * Created by Shino on 2017/04/11.
 */

public class Task extends RealmObject implements Serializable {
    private String title;
    private String contents;
    private Date date;
    private int categoryID;

    @PrimaryKey
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryID;
    }

    public void setCategoryId(int categoryID) {
        this.categoryID = categoryID;
    }

}
