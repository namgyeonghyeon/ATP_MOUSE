package com.cookandroid.mouse.Navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cookandroid.mouse.R;
import com.cookandroid.mouse.Sensor.BpmActivity;
import com.cookandroid.mouse.Sensor.HomeActivity;
import com.cookandroid.mouse.Sensor.TemperatureSensorActivity;
import com.cookandroid.mouse.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NaviActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;
    ImageView quit;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quit = findViewById(R.id.quit);
        mBottomNV = findViewById(R.id.nav_view);
        progressDialog=new ProgressDialog(this);

        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());

                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.navigation_1);

        // 로그아웃을 버튼을 클릭했을때 이벤트 처리
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("로그아웃중입니다");
                progressDialog.setTitle("로딩중");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        startActivity(new Intent(NaviActivity.this, LoginActivity.class));
                        Toast.makeText(NaviActivity.this, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                    }
                }, 2000); // 2초 후에 ProgressDialog 닫기 및 LoginActivity 시작
            }
        });
    }

    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        // 각각 프레그먼트의 화면으로 넘어가기
        if (fragment == null) {
            if (id == R.id.navigation_1) {
                fragment = new HomeActivity();

            } else if (id == R.id.navigation_2){

                fragment = new TemperatureSensorActivity();
            }else {
                fragment = new BpmActivity();
            }

            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }
}