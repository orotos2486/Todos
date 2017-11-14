package ortoros.iotandroid;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddScheduleActivity extends AppCompatActivity {
    private String smonth;
    private String sday;
    private String shour;
    private String sminute;
    private TextView testWhat,testWhere,testDay,testTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        getSupportActionBar().setTitle("Add Schedule");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int i;
        Intent intent = getIntent();

        EditText what = (EditText)findViewById(R.id.what);
        if(intent.getStringExtra("status").equals("update")){
          what.setText(intent.getStringExtra("what"));


              }

        EditText where = (EditText)findViewById(R.id.where);
        if(intent.getStringExtra("status").equals("update")){
            where.setText(intent.getStringExtra("where"));}

        final String months[] = {"01", "02","03","04","05","06","07","08","09","10","11","12"};
        ArrayAdapter<String> monthAdapter =
                new ArrayAdapter<String>(AddScheduleActivity.this,
                        android.R.layout.simple_spinner_item, months);
        Spinner month = (Spinner)findViewById(R.id.month);
        month.setAdapter(monthAdapter);

        if(intent.getStringExtra("status").equals("update")){
            String day_month = intent.getStringExtra("time").substring(0,2);
            for(i = 0 ; i<months.length;i++){
                if(day_month.equals(months[i])){
                    break;
                }
            }
            month.setSelection(i);
        }

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

        if(intent.getStringExtra("status").equals("update")){
            String day_day = intent.getStringExtra("time").substring(3,5);
            for(i = 0 ; i<months.length;i++){
                if(day_day.equals(days[i])){
                    break;
                }
            }
            day.setSelection(i);
        }

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

        if(intent.getStringExtra("status").equals("update")){
            String time_hour = intent.getStringExtra("day").substring(0,2);
            for(i = 0 ; i<months.length;i++){
                if(time_hour.equals(hours[i])){
                    break;
                }
            }
            hour.setSelection(i);
        }

        hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shour = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        final String minutes[] = { "05","10","15","20","25","30","35","40","45","50","55","60"};
        ArrayAdapter<String> minuteAdapter =
                new ArrayAdapter<String>(AddScheduleActivity.this,
                        android.R.layout.simple_spinner_item, minutes);
        Spinner minute = (Spinner)findViewById(R.id.minute);
        minute.setAdapter(minuteAdapter);

        if(intent.getStringExtra("status").equals("update")){
            String time_minute = intent.getStringExtra("day").substring(3,5);
            for(i = 0 ; i<months.length;i++){
                if(time_minute.equals(minutes[i])){
                    break;
                }
            }
            minute.setSelection(i);
        }
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

        writeDatabase(whatText.getText().toString(), whereText.getText().toString(),stringDay,stringTime,AddScheduleActivity.this);
//        testWhat = (TextView)findViewById(R.id.testwhat);
//        testWhere = (TextView)findViewById(R.id.testwhere);
//        testDay = (TextView)findViewById(R.id.testday);
//        testTime = (TextView)findViewById(R.id.testtime);
//        testWhat.setText(whatText.getText().toString());
//        testWhere.setText(whereText.getText().toString());
//        testDay.setText(stringDay);
//        testTime.setText(stringTime);
        Intent intent = new Intent(AddScheduleActivity.this,ScheduleSetActivity.class);
        startActivity(intent);

    }
    public static void writeDatabase(String what, String where , String day, String time, Context context) {
        Expected_Schedules schedules= new Expected_Schedules(context);
        SQLiteDatabase db = schedules.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(schedules.WHAT, what);
        values.put(schedules.WHERE, where);
        values.put(schedules.DAY, day);
        values.put(schedules.TIME,time);
        db.insert(schedules.TABLE_NAME, null, values);
    }


}
