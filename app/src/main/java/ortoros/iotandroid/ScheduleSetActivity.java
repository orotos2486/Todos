package ortoros.iotandroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleSetActivity extends AppCompatActivity {
    ArrayList<Bookmark> items = new ArrayList<Bookmark>();
    class TodosAdapter extends ArrayAdapter {
        public TodosAdapter(Context context) {
            super(context, R.layout.main_menu_list, items);
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
    }
    //-----------------DB연동--------------------------------------------------
    class Bookmark {
        String what,where,day,time;
        Bookmark(String what, String where, String day , String time) {
            this.what = what; this.where = where; this.day = day; this.time = time;
        }
    }
    public void readDatabase() {
        Schedules schedules = new Schedules(ScheduleSetActivity.this);
        SQLiteDatabase db = schedules.getReadableDatabase();
        String sql = "select * from " + Schedules.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        items.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            String what = cursor.getString(0);
            String where = cursor.getString(1);
            String day = cursor.getString(2);
            String time = cursor.getString(3);
            items.add(new Bookmark(what, where, day, time));
        }
        cursor.close();
        //-------------------------------------------------
//        String items[] = new String[todosList.size()];
//        for (int i = 0; i < todosList.size(); i++) {
//            items[i] = todosList.get(i).what + todosList.get(i).where ;
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                MainActivity.this, android.R.layout.simple_list_item_1, items);
//                ListView listView = (ListView)findViewById(R.id.list);
//                listView.setAdapter(adapter);
        ScheduleSetActivity.TodosAdapter adapter = new ScheduleSetActivity.TodosAdapter(ScheduleSetActivity.this);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        //ItemClickListener
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(MainActivity.this, position+"", Toast.LENGTH_SHORT).show();
//                    }
//                });
        //수정을 요함
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                final int pos = position;//!!!!!!!!!
//                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//                dialog.setMessage("해당 데이터를 삭제하시겠습니까?");
//                dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//                dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {}
//                });
//                dialog.show();
//                return true;
//            }
//        });
    }
}
