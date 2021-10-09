package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "functionalities")
public class Functionality {

    @DatabaseField(canBeNull = false)
    private Integer listid;
    @DatabaseField
    private boolean hasdate;
    @DatabaseField
    private boolean haslocal;
    @DatabaseField
    private boolean hascloud;

    // TODO: more functions to be added later with more ideas
//    @DatabaseField
//    private boolean hasfunctionx;
//    @DatabaseField
//    private boolean hasfunctionx;
//    @DatabaseField
//    private boolean hasfunctionx;
//    @DatabaseField
//    private boolean hasfunctionx;

    public Functionality(Integer listid){
        this.listid = listid;
    }

    // TODO: setters and getters



}
