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
}
