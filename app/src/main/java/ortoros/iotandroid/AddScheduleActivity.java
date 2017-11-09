package ortoros.iotandroid;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddScheduleActivity extends AppCompatActivity {
    private String smonth;
    private String sday;
    private String shour;
    private String sminute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        getSupportActionBar().setTitle("Add Schedule");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //액션바 숨기기
        //hideActionBar();
        final String months[] = { "01", "02","03","04","05","06","07","08","09","10","11","12"};
        ArrayAdapter<String> monthAdapter =
                new ArrayAdapter<String>(AddScheduleActivity.this,
                        android.R.layout.simple_spinner_item, months);
        Spinner month = (Spinner)findViewById(R.id.month);
        month.setAdapter(monthAdapter);
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smonth = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        final String days[] = { "01", "02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17",
        "18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        ArrayAdapter<String> dayAdapter =
                new ArrayAdapter<String>(AddScheduleActivity.this,
                        android.R.layout.simple_spinner_item, days);
        Spinner day = (Spinner)findViewById(R.id.day);
        day.setAdapter(dayAdapter);
        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sday = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        final String hours[] = { "01", "02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17",
                "18","19","20","21","22","23","24"};
        ArrayAdapter<String> hourAdapter =
                new ArrayAdapter<String>(AddScheduleActivity.this,
                        android.R.layout.simple_spinner_item, hours);
        Spinner hour = (Spinner)findViewById(R.id.hour);
        hour.setAdapter(hourAdapter);
        hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shour = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        final String minutes[] = { "5","10","15","20","25","30","35","40","45","50","55","60"};
        ArrayAdapter<String> minuteAdapter =
                new ArrayAdapter<String>(AddScheduleActivity.this,
                        android.R.layout.simple_spinner_item, minutes);
        Spinner minute = (Spinner)findViewById(R.id.minute);
        minute.setAdapter(minuteAdapter);
        minute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sminute = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }
    public void startApply(View view){
        EditText whatText = (EditText)findViewById(R.id.what);
        EditText whereText = (EditText)findViewById(R.id.where);
        String stringDay = smonth+"/"+sday;
        String stringTime = shour+":"+sminute;
        writeDatabase(whatText.getText().toString(), whereText.getText().toString(),stringDay,stringTime);
        finish();
    }
    public void writeDatabase(String what, String where , String day, String time) {
        Bookmark bookmark = new Bookmark(AddScheduleActivity.this);
        SQLiteDatabase db = bookmark.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Bookmark.WHAT, what);
        values.put(Bookmark.WHERE, where);
        values.put(Bookmark.DAY, day);
        values.put(Bookmark.TIME,time);
        db.insert(Bookmark.TABLE_NAME, null, values);
    }
}
