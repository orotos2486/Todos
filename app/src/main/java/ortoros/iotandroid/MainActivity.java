package ortoros.iotandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int count=0;
    private MinewBeaconManager mMinewBeaconManager;
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean isScanning;
    private TextView mStart_scan;
    private int rssi;
    private boolean checked;

    ArrayList<Todos> todosList = new ArrayList<Todos>();
    class TodosAdapter extends ArrayAdapter {
        public TodosAdapter(Context context) {
            super(context, R.layout.main_menu_list, todosList);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
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
            CheckBox checkBox = (CheckBox)view.findViewById(R.id.check);
            checkBox.setChecked(todosList.get(position).checked);
            if (todosList.get(position).checked) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            final int pos = position;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Schedules schedules = new Schedules(MainActivity.this);
                    SQLiteDatabase db = schedules.getWritableDatabase();
                    int sb =(b)? 1 : 0;
                    db.execSQL("UPDATE " + Schedules.TABLE_NAME + " SET checked=" + sb + " WHERE WHAT=\'" + todosList.get(position).what + "\'");
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setMessage("해당 데이터를 삭제하시겠습니까?");
                    dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeDatabase(todosList.get(position).what);
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
        initView();
        initManager();
        checkBluetooth();
        initListener();
        readDatabase();

    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    //액션버튼을 클릭했을때의 동작
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//        //or switch문을 이용하면 될듯 하다.
//        if (id == R.id.action_search) {
//            Toast.makeText(this, "검색 클릭", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //액션바 숨기기
//    private void hideActionBar() {
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null)
//            actionBar.hide();
//    }
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
            int checked = cursor.getInt(4);
            boolean real_checked =false;
            if (checked == 1) real_checked = true;
            todosList.add(new Todos(what,where,day,time,real_checked));
        }
        cursor.close();
        //-------------------------------------------------

        TodosAdapter adapter = new TodosAdapter(MainActivity.this);
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

    }
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                break;
        }

    }


    private void initView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        mStart_scan = (TextView) findViewById(R.id.start_scan);

    }

    private void initManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }


    private void initListener() {
//        mStart_scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

        if (mMinewBeaconManager != null) {
            BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
            switch (bluetoothState) {
                case BluetoothStateNotSupported:
                    Toast.makeText(MainActivity.this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case BluetoothStatePowerOff:
                    showBLEDialog();
                    return;
                case BluetoothStatePowerOn:
                    break;
            }
        }

        try {
            mMinewBeaconManager.startScan();
        } catch (Exception e) {
            e.printStackTrace();
        }



        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {
            }
            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
            }


            @Override
            public void onRangeBeacons(final List<MinewBeacon> minewBeacons) {
                for (int i = 0; i < minewBeacons.size(); i++) {
                    MinewBeacon obj = minewBeacons.get(i);
                    String major = obj.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major).getStringValue();
                    String minor = obj.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor).getStringValue();
                    if (major.equals("30001") && minor.equals("13451") ) {
                        rssi  = obj.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_RSSI).getIntValue();
                        if(rssi>-60&&count<=0){
                            NotificationManager notificationManager= (NotificationManager)MainActivity.this.getSystemService(MainActivity.this.NOTIFICATION_SERVICE);
                            Intent intent1 = new Intent(MainActivity.this.getApplicationContext(),MainActivity.class); //인텐트 생성.

                            Notification.Builder builder = new Notification.Builder(getApplicationContext());

                            PendingIntent pendingNotificationIntent = PendingIntent.getActivity( MainActivity.this,0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                            builder.setSmallIcon(R.drawable.non_select_border).setTicker("HETT").setWhen(System.currentTimeMillis())
                                    .setNumber(1).setContentTitle("Todos").setContentText("챙기실 물건은 다 챙기셨습니까?")
                                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).
                                    setContentIntent(pendingNotificationIntent).setAutoCancel(true).setOngoing(true);
                            notificationManager.notify(1, builder.build());
                            count=1000;
                        }
                        count--;
                        break;
                    }
                }

            }


            @Override
            public void onUpdateState(BluetoothState state) {
                switch (state) {
                    case BluetoothStatePowerOn:
                        Toast.makeText(getApplicationContext(), "BluetoothStatePowerOn", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothStatePowerOff:
                        Toast.makeText(getApplicationContext(), "BluetoothStatePowerOff", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stop scan
        if (isScanning) {
            mMinewBeaconManager.stopScan();
        }
    }

    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                break;
        }
    }
    public void removeDatabase(String what) {
        Schedules schedules = new Schedules(MainActivity.this);
        SQLiteDatabase db = schedules.getWritableDatabase();
        Toast.makeText(MainActivity.this,what, Toast.LENGTH_LONG).show();
        String[] args = {what+""};
//        db.delete(Bookmark.TABLE_NAME, "WHAT = ? ", args);
        db.execSQL("DELETE FROM "+Schedules.TABLE_NAME+" WHERE WHAT=\'"+what+"\'");
    }
}

