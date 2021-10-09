package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "duedates")
public class Duedate {

    @DatabaseField(generatedId = true, columnName = "id")
    private Integer id;
    @DatabaseField(canBeNull = true)
    private Date duedate;
    @DatabaseField(canBeNull = true)
    private Date startdate;
    @DatabaseField(canBeNull = false)
    private Integer itemid;

    public Duedate(Integer itemid) {
        this.itemid = itemid;
    }
    // TODO: setters and getters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }
}
