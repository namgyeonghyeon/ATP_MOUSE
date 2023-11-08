package com.cookandroid.mouse.Sensor;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.cookandroid.mouse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemperatureSensorActivity extends Fragment {


    private DatabaseReference tempRef;
    private TextView tempGage;
    private ValueEventListener tempValueEventListener;
    private static final String TAG = "TemperatureSensorActivity";

    // Firebase 인증 객체 및 사용자 객체 선언
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.activity_temperaturesensor, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tempGage = view.findViewById(R.id.TempGage);
        lineChart = view.findViewById(R.id.LineChart);

        // Firebase 인증 및 사용자 객체 초기화
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // 사용자의 UID를 사용하여 해당 사용자의 temperature 데이터베이스 참조
        tempRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("temperature");
        tempValueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Entry> entries = new ArrayList<>();

                double latestTemperature = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    if (dataMap != null) {
                        Object tempValue = dataMap.get("temperature");
                        if (tempValue instanceof Double) {
                            Double temp = (Double) tempValue;
                            latestTemperature = temp; // 가장 최신의 값을 저장
                        } else if (tempValue instanceof Long) {
                            long bpmLong = (Long) tempValue;
                            latestTemperature = (double) bpmLong; // 가장 최신의 값을 저장
                        }

                        // 그래프 데이터를 추가
                        entries.add(new Entry(entries.size(), (float) latestTemperature));
                    }
                }
                String temperatureString = String.valueOf(latestTemperature);
                tempGage.setText(temperatureString);

                // 온도가 임계값을 초과하는 경우 텍스트 색상을 빨간색으로 변경
                if (latestTemperature >= 40.0) {
                    tempGage.setTextColor(getResources().getColor(R.color.red)); // 빨간색으로 설정한 color 리소스를 사용합니다.
                } else if(latestTemperature >= 33.0){
                    tempGage.setTextColor(getResources().getColor(R.color.darkorange));
                } else if(latestTemperature >= 27.0){
                    tempGage.setTextColor(getResources().getColor(R.color.blue));
                }else {
                    tempGage.setTextColor(getResources().getColor(R.color.black)); // 기본 텍스트 색상으로 설정
                }

                // 그래프 생성 및 설정
                LineDataSet dataSet = new LineDataSet(entries, "Temperature");
                dataSet.setColor(Color.CYAN); // 선 색상
                dataSet.setCircleColor(Color.CYAN); // 선을 잇는 원 색상
                dataSet.setLineWidth(2f); // 선 굵기 설정
                dataSet.setDrawValues(false); // 각 지점마다 수치가 보이게설정(true)

                List<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(dataSet);

                LineData lineData = new LineData(dataSets);
                lineChart.getDescription().setEnabled(false); // description label 안보이게 설정
                lineChart.setData(lineData);
                lineChart.invalidate(); // 차트 다시 그리기(차트 업데이트)
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        };
        tempRef.addValueEventListener(tempValueEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tempValueEventListener != null) {
            tempRef.removeEventListener(tempValueEventListener);
        }
    }
}