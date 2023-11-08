package com.cookandroid.mouse.Sensor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.cookandroid.mouse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Fragment {

    private ListView tempListView;
    private ListView bpmListView;
    private DatabaseReference tempRef;
    private DatabaseReference bpmRef;
    private List<Double> tempDataList;
    private ArrayAdapter<Double> tempAdapter;
    private List<Integer> bpmDataList;
    private ArrayAdapter<Integer> bpmAdapter;

    // Firebase 인증 객체 및 사용자 객체 선언
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tempListView = view.findViewById(R.id.temp_list);
        tempDataList = new ArrayList<>();
        tempAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, tempDataList);
        tempListView.setAdapter(tempAdapter);

        bpmListView = view.findViewById(R.id.bpm_list);
        bpmDataList = new ArrayList<>();
        bpmAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, bpmDataList);
        bpmListView.setAdapter(bpmAdapter);

        // Firebase 인증 및 사용자 객체 초기화
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // 사용자의 UID를 사용하여 해당 사용자의 temperature 데이터베이스 참조
        tempRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("temperature");
        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tempDataList.clear(); // 이전 데이터 삭제
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Double temperature = childSnapshot.child("temperature").getValue(Double.class);
                    if (temperature != null) {
                        tempDataList.add(0, temperature);
                    }
                }
                tempAdapter.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 에러 처리
            }
        });

        // 사용자의 UID를 사용하여 해당 사용자의 bpm 데이터베이스 참조
        bpmRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("bpm");
        bpmRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                bpmDataList.clear(); // 이전 데이터 삭제
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Integer bpm = childSnapshot.child("bpm").getValue(Integer.class);
                    if (bpm != null && bpm != -1) {
                        bpmDataList.add(0, bpm);
                    }
                }
                bpmAdapter.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 에러 처리
            }
        });
    }
}