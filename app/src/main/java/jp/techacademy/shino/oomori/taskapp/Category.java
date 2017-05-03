package jp.techacademy.shino.oomori.taskapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.io.Serializable;

/**
 * Created by Shino on 2017/04/30.
 */

public class Category extends RealmObject implements Serializable {
    private String category;

    @PrimaryKey
    private int id;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
