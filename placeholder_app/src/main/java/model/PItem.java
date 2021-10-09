package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "pitems")
public class PItem {

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(canBeNull = true)
    private String contentshown;
    @DatabaseField(canBeNull = false)
    private Integer listid;

    public PItem(Integer listid) {
        this.listid = listid;
    }
    // TODO: setters and getters
}
