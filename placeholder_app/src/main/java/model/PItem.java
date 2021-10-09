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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContentshown() {
        return contentshown;
    }

    public void setContentshown(String contentshown) {
        this.contentshown = contentshown;
    }

    public Integer getListid() {
        return listid;
    }

    public void setListid(Integer listid) {
        this.listid = listid;
    }
}
