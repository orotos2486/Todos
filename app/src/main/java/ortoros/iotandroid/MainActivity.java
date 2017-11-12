package ortoros.iotandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Todos> todosList = new ArrayList<Todos>();
    class Todos {
        String what,where,day,time;
        Todos(String what, String where, String day , String time) {
            this.what = what; this.where = where; this.day = day; this.time = time;
        }
    }
    class TodosAdapter extends ArrayAdapter {
        public TodosAdapter(Context context) {
            super(context, R.layout.main_menu_list, todosList);
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
            whatText.setText(todosList.get(position).what);
            timeText.setText(todosList.get(position).time);
            dayText.setText(todosList.get(position).day);
            whereText.setText(todosList.get(position).where);
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //액션바 설정하기
        //액션바 타이틀 변경
        getSupportActionBar().setTitle("TODOS");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //액션바 숨기기
        //hideActionBar();
//        items.add(
//                new Item("분리수거","06:00","9/29","밖"));
//        items.add(new Item("페휴지","07:30","10/29","분리수거장"));
//        TodosAdapter adapter = new TodosAdapter(MainActivity.this);
//        ListView listView = (ListView)findViewById(R.id.list);
//        listView.setAdapter(adapter);
        readDatabase();

    }
    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == R.id.action_search) {
            Toast.makeText(this, "검색 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //액션바 숨기기
    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
    }
    public void startSetButton(View view){
        Intent intent = new Intent(MainActivity.this, ScheduleSetActivity.class);
        startActivity(intent);
    }
    //-----------------DB연동--------------------------------------------------

    public void readDatabase() {
        Schedules schedules= new Schedules(MainActivity.this);
        SQLiteDatabase db = schedules.getReadableDatabase();
        String sql = "select * from "+Schedules.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        todosList.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            String what = cursor.getString(0);
            String where= cursor.getString(1);
            String day= cursor.getString(2);
            String time = cursor.getString(3);
            todosList.add(new Todos(what,where,day,time));
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
        TodosAdapter adapter = new TodosAdapter(MainActivity.this);
        ListView listView = (ListView)findViewById(R.id.list);
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
