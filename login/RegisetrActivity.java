package com.cookandroid.mouse.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.mouse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisetrActivity extends AppCompatActivity {

    TextView alreadyHaveaccount; // 뒤로가기
    EditText register_email, register_password, retryPassword;
    Button register_btn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // 이메일 형식
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveaccount = findViewById(R.id.alreadyHaveaccount);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        retryPassword = findViewById(R.id.retryPassword);
        register_btn = findViewById(R.id.register_btn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // "이미 계정이 있으신가요?" 텍스트를 클릭했을 때 LoginActivity로 이동
        alreadyHaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisetrActivity.this, LoginActivity.class));
            }
        });

        // "회원가입" 버튼을 클릭했을 때 이벤트 처리
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuthentication();
            }
        });
    }

    // 회원가입을 수행하는 메서드
    private void performAuthentication() {
        String email = register_email.getText().toString();
        String password = register_password.getText().toString();
        String repassword = retryPassword.getText().toString();

        if (!email.matches(emailPattern)) {
            register_email.setError("이메일 형식에 맞춰주세요");
        } else if (password.isEmpty() || password.length() < 6) {
            register_password.setError("비밀번호를 6글자 이상으로 설정해주세요");
        } else if (!password.equals(repassword)) {
            retryPassword.setError("비밀번호가 일치하지 않습니다");
        } else {
            progressDialog.setMessage("잠시만 기다려주세요");
            progressDialog.setTitle("로딩중");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // Firebase Authentication을 사용하여 이메일과 비밀번호를 이용한 사용자 계정 생성을 시도하는 메소드
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(RegisetrActivity.this, "가입 성공", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisetrActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisetrActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // 다음 활동으로 이동하는 메서드
    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisetrActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
