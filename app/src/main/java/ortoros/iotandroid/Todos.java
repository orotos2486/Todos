package ortoros.iotandroid;

/**
 * Created by SPTek on 2017-11-14.
 */

public class Todos {
    String what,where,day,time;
    boolean checked;
    Todos(String what, String where, String day , String time) {
        this.what = what; this.where = where; this.day = day; this.time = time;
        this.checked = false;
    }
}
