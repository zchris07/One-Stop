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



    public Integer getListid() {
        return listid;
    }

    public void setListid(Integer listid) {
        this.listid = listid;
    }

    public boolean isHasdate() {
        return hasdate;
    }

    public void setHasdate(boolean hasdate) {
        this.hasdate = hasdate;
    }

    public boolean isHaslocal() {
        return haslocal;
    }

    public void setHaslocal(boolean haslocal) {
        this.haslocal = haslocal;
    }

    public boolean isHascloud() {
        return hascloud;
    }

    public void setHascloud(boolean hascloud) {
        this.hascloud = hascloud;
    }

    @Override
    public String toString() {
        return "Functionality{" +
                "listid=" + listid +
                ", hasdate=" + hasdate +
                ", haslocal=" + haslocal +
                ", hascloud=" + hascloud +
                '}';
    }
}
