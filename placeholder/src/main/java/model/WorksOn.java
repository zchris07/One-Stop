package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "workson")
public class WorksOn {

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(canBeNull = false)
    private String collabratorid;
    @DatabaseField(canBeNull = false)
    private String listid;

    public WorksOn(){

    }
    public WorksOn(String collabratorid, String listid){
        this.collabratorid = collabratorid;
        this.listid = listid;
    }

    public String getCollabratorid() {
        return this.collabratorid;
    }

    public String getListid(){
        return this.listid;
    }
}
