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

}
