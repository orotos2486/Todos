package ortoros.iotandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SPTek on 2017-11-14.
 */

public class Expected_Schedules extends SQLiteOpenHelper{
    public static final String DATABASE_NAME="expected_schedules";
    public static final int DATABASE_VERSION=1;
    public static final String TABLE_NAME="expected";
    public static final String WHAT = "what";
    public static final String WHERE = "loc";
    public static final String TIME = "time";
    public static final String DAY = "day";
    public Expected_Schedules(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + "(" + WHAT +" text PRIMARY KEY,"
                +WHERE +" text,"+ TIME +" text," + DAY + " text)";
        try{
            db.execSQL(sql);
        }catch(Exception e){e.printStackTrace();}

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("drop table if exists " + TABLE_NAME);
        }catch(Exception e){
            e.printStackTrace();
        }
        onCreate(db);
    }
}
