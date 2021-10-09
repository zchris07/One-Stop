package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "plists")
public class PList {

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(canBeNull = true)
    private Integer userid;

    public PList() {

    }

    public PList(Integer userid) {
        this.userid = userid;
    }

    // TODO: setters and getters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
