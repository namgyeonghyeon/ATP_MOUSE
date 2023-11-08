package com.cookandroid.mouse.Sensor;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class BpmActivity extends Fragment {

    private DatabaseReference bpmRef;
    private TextView BpmGage;
    private ValueEventListener bpmValueEventListener;
    private static final String TAG = "BpmActivity";
    // Firebase 인증 객체 및 사용자 객체 선언
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private LineChart lineChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.activity_bpmsensor, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BpmGage = view.findViewById(R.id.BpmGage);
        lineChart = view.findViewById(R.id.LineChart2);

        // Firebase 인증 및 사용자 객체 초기화
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // 사용자의 UID를 사용하여 해당 사용자의 bpm 데이터베이스 참조
        bpmRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("bpm");
        bpmValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int latestBpm = -1; // 초기값을 -1로 설정
                List<Entry> entries = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    if (dataMap != null) {
                        Object bpmValue = dataMap.get("bpm");
                        if (bpmValue instanceof Integer) {
                            int bpm = (Integer) bpmValue;
                            if (bpm != -1) { // -1이 아닌 경우에만 엔트리에 추가
                                latestBpm = bpm; // 가장 최신의 값을 저장
                                entries.add(new Entry(entries.size(), bpm));
                            }
                        } else if (bpmValue instanceof Long) {
                            long bpmLong = (Long) bpmValue;
                            if (bpmLong != -1) { // -1이 아닌 경우에만 엔트리에 추가
                                latestBpm = (int) bpmLong; // 가장 최신의 값을 저장
                                entries.add(new Entry(entries.size(), bpmLong));
                            }
                        }
                    }
                }

                // -1이 아닌 경우에만 업데이트 수행
                if (latestBpm != -1) {
                    String bpmText = String.valueOf(latestBpm);
                    BpmGage.setText(bpmText);

                    // BpmGage 수치에 따른 색상 글씨 변경
                    if (latestBpm >= 120) {
                        BpmGage.setTextColor(getResources().getColor(R.color.red)); // 빨간색으로 설정한 color 리소스를 사용합니다.
                    } else if (latestBpm >= 100) {
                        BpmGage.setTextColor(getResources().getColor(R.color.darkorange));
                    } else if (latestBpm >= 60) {
                        BpmGage.setTextColor(getResources().getColor(R.color.blue));
                    } else if (latestBpm <= 60) {
                        BpmGage.setTextColor(getResources().getColor(R.color.red)); // 빨간색으로 설정한 color 리소스를 사용합니다.
                    }

                    entries.add(new Entry(entries.size(), latestBpm));
                }

                LineDataSet dataSet = new LineDataSet(entries, "BPM");
                dataSet.setColor(Color.RED); // 선 색상
                dataSet.setCircleColor(Color.RED); // 선을 잇는 원 색상
                dataSet.setLineWidth(2f); // 선 굵기 설정
                dataSet.setDrawValues(false); // 각 지점마다 수치가 보이게 설정


                List<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(dataSet);

                LineData lineData = new LineData(dataSets);
                lineChart.getDescription().setEnabled(false); // description label 안보이게 설정
                lineChart.setData(lineData);
                lineChart.invalidate(); // 차트 다시 그리기(차트 업데이트)
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        bpmRef.addValueEventListener(bpmValueEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bpmValueEventListener != null) {
            bpmRef.removeEventListener(bpmValueEventListener);
        }
    }
}
