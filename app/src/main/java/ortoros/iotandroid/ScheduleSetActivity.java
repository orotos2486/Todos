package ortoros.iotandroid;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScheduleSetActivity extends AppCompatActivity {
    ArrayList<Todos> items = new ArrayList<Todos>();
    class TodosAdapter extends ArrayAdapter {
        public TodosAdapter(Context context) {
            super(context, R.layout.set_menu_list, items);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.main_menu_list, null);
            }
            TextView whatText = (TextView)view.findViewById(R.id.what);
            TextView timeText = (TextView)view.findViewById(R.id.time);
            TextView dayText = (TextView)view.findViewById(R.id.day);
            TextView whereText = (TextView)view.findViewById(R.id.where);
            whatText.setText(items.get(position).what);
            timeText.setText(items.get(position).time);
            dayText.setText(items.get(position).day);
            whereText.setText(items.get(position).where);

            CheckBox checkBox = (CheckBox)view.findViewById(R.id.check);
            if (items.get(position).checked) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            final int pos = position;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    items.get(pos).checked = b;
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ScheduleSetActivity.this,AddScheduleActivity.class);
                    intent.putExtra("what",items.get(pos).what);
                    intent.putExtra("where",items.get(pos).where);
                    intent.putExtra("day",items.get(pos).day);
                    intent.putExtra("time",items.get(pos).time);
                    intent.putExtra("status","update");
                    startActivity(intent);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ScheduleSetActivity.this);
                    dialog.setMessage("해당 데이터를 삭제하시겠습니까?");
                    dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeDatabase(items.get(pos).what);
                            readDatabase();
                        }
                    });
                    dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    dialog.show();
                    return false;
                }
            });
            return view;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_set);
        //액션바 설정하기
        //액션바 타이틀 변경
        getSupportActionBar().setTitle("Select Schedule");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        readDatabase();
    }
    public void newSchedule(View view){
        Intent intent = new Intent(ScheduleSetActivity.this,AddScheduleActivity.class);
        intent.putExtra("status","new");
        startActivity(intent);
    }
    public void goMainActivity(View view){
        for(int i = 0;i<items.size();i++){
            if(items.get(i).checked == true){
                writeDatabase(items.get(i).what,items.get(i).where,items.get(i).day,items.get(i).time,ScheduleSetActivity.this);
            }
        }

        Intent intent = new Intent(ScheduleSetActivity.this,MainActivity.class);
        startActivity(intent);
    }
    //-----------------DB연동--------------------------------------------------
    public static void writeDatabase(String what, String where , String day, String time, Context context) {
        Schedules schedules= new Schedules(context);
        SQLiteDatabase db = schedules.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(schedules.WHAT, what);
        values.put(schedules.WHERE, where);
        values.put(schedules.DAY, day);
        values.put(schedules.TIME,time);
        db.insert(schedules.TABLE_NAME, null, values);
    }
    public void readDatabase() {
        Expected_Schedules schedules = new Expected_Schedules(ScheduleSetActivity.this);
        SQLiteDatabase db = schedules.getReadableDatabase();
        String sql = "select * from "+Expected_Schedules.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        items.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            String what = cursor.getString(0);
            String where = cursor.getString(1);
            String day = cursor.getString(2);
            String time = cursor.getString(3);
            int checked = 0;
            boolean real_checked =false;
            if (checked == 1) real_checked = true;
            items.add(new Todos(what,where,day,time,real_checked));
        }
        cursor.close();

        TodosAdapter adapter = new TodosAdapter(ScheduleSetActivity.this);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

    }
    public void removeDatabase(String what) {
        Expected_Schedules bookmark = new Expected_Schedules(ScheduleSetActivity.this);
        SQLiteDatabase db = bookmark.getWritableDatabase();
        Toast.makeText(ScheduleSetActivity.this,what, Toast.LENGTH_LONG).show();
        String[] args = {what+""};
//        db.delete(Bookmark.TABLE_NAME, "WHAT = ? ", args);
        db.execSQL("DELETE FROM "+Expected_Schedules.TABLE_NAME+" WHERE WHAT=\'"+what+"\'");
    }
}
