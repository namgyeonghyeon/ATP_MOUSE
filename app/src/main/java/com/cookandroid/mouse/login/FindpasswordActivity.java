package com.cookandroid.mouse.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.mouse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindpasswordActivity extends AppCompatActivity {

    TextView ToLogin;
    EditText Findid_email;
    Button Findid_btn;
    private String Email;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);

        ToLogin = findViewById(R.id.ToLogin);
        Findid_email = findViewById(R.id.Findid_email);
        Findid_btn = findViewById(R.id.Findid_btn);
        progressDialog=new ProgressDialog(this);

        // "로그인으로 돌아가기" 텍스트를 클릭했을 때 LoginActivity로 이동
        ToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindpasswordActivity.this, LoginActivity.class));
            }
        });

        // "비밀번호 찾기" 버튼을 클릭했을 때 이벤트 처리
        Findid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = Findid_email.getText().toString();

                //Email의 길이가 0이라면 Toast 출력
                if(Email.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(FindpasswordActivity.this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    findpasswordActivity();
                }
            }
        });
    }

    // 비밀번호 찾기 로직을 수행하는 메서드
    private void findpasswordActivity() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // 대화형 컴포먼트
        progressDialog.setMessage("잠시만 기다려주세요");
        progressDialog.setTitle("로딩중");
        progressDialog.setCanceledOnTouchOutside(false); //외부를 선택해도 false로 ProgressDialog가 닫히지 않도록 설정
        progressDialog.show();

        //이메일이 auth에 등록되어있을시 비밀번호 재설정 메일 발송
        auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss(); //ProgressDialog종료
                    Toast.makeText(FindpasswordActivity.this, "이메일을 확인하세요", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FindpasswordActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(FindpasswordActivity.this, "이메일을 정확히 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
