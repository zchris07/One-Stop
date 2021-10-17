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
    private boolean hascalendar;
    @DatabaseField
    private boolean hasnotes;

    // TODO: more functions to be added later with more ideas

//    @DatabaseField
//    private boolean hasfunctionx;
//    @DatabaseField
//    private boolean hasfunctionx;
//    @DatabaseField
//    private boolean hasfunctionx;

    public Functionality(Integer listid){
        this.listid = listid;
    }


    public Integer getListid() {
        return listid;
    }

    public void setListid(Integer listid) {
        this.listid = listid;
    }

    public boolean getHasdate() {
        return hasdate;
    }

    public void setHasdate(boolean hasdate) {
        this.hasdate = hasdate;
    }

    public boolean getHascalendar() {
        return hascalendar;
    }

    public void setHascalendar(boolean hascalendar) {
        this.hascalendar = hascalendar;
    }

    public boolean getHasnotes() {
        return hasnotes;
    }

    public void setHasnotes(boolean hasdate) {
        this.hasdate = hasnotes;
    }

    @Override
    public String toString() {
        return "Functionality{" +
                "listid=" + listid +
                ", hasdate=" + hasdate;
    }
}
