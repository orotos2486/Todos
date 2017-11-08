package ortoros.iotandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    class Item {
        String what, time, day, where;

        Item(String what, String time, String day, String where) {
            this.what = what;
            this.time = time;
            this.day = day;
            this.where = where;
        }
    }
    ArrayList<Item> items = new ArrayList<Item>();


    class ItemAdapter extends ArrayAdapter {
        public ItemAdapter(Context context) {
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
        items.add(
                new Item("분리수거","06:00","9/29","밖"));
        items.add(new Item("페휴지","07:30","10/29","분리수거장"));
        ItemAdapter adapter = new ItemAdapter(MainActivity.this);
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

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
}
