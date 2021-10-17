package model;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;


public class Notes {
    @DatabaseField(generatedId = true, columnName = "id")
    private Integer id;
    @DatabaseField(canBeNull = false)
    private String content;
    @DatabaseField(canBeNull = false)
    private Integer itemid;

    public Notes(Integer itemid, String content) {
        this.itemid = itemid;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }
}
