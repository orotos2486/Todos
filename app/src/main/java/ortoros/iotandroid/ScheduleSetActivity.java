package ortoros.iotandroid;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ScheduleSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_set);
        //액션바 설정하기
        //액션바 타이틀 변경
        getSupportActionBar().setTitle("TODOS");
        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
