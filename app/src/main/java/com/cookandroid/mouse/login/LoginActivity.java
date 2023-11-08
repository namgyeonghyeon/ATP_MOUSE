package com.cookandroid.mouse.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.mouse.Navigation.NaviActivity;

import com.cookandroid.mouse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText Login_email, Login_password;
    TextView findid, goRegister_btn;
    Button Login_btn;
    String emailPatton = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // 이메일 형식
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login_email = findViewById(R.id.Login_email);
        Login_password = findViewById(R.id.Login_password);
        findid = findViewById(R.id.findid);
        goRegister_btn = findViewById(R.id.goRegister_btn);
        Login_btn = findViewById(R.id.Login_btn);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // "회원가입으로 이동" 텍스트를 클릭했을 때 RegisetrActivity로 이동
        goRegister_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisetrActivity.class));
            }
        });

        // "로그인" 버튼을 클릭했을 때 이벤트 처리
        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        // "아이디/비밀번호 찾기" 텍스트를 클릭했을 때 FindpasswordActivity로 이동
        findid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FindpasswordActivity.class));
            }
        });
    }

    // 로그인 수행하는 메서드
    private void performLogin() {
        String email = Login_email.getText().toString();
        String password = Login_password.getText().toString();

        if (!email.matches(emailPatton)) {
            Login_email.setError("이메일 형식에 맞춰주세요");
        } else if (password.isEmpty() || password.length() < 6) {
            Login_password.setError("비밀번호를 정확하게 입력하세요");
        } else {
            progressDialog.setMessage("잠시만 기다려주세요");
            progressDialog.setTitle("로딩중");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // Firebase Authentication 기능을 사용해 이메일과 비밀번호를 이용하여 사용자를 로그인하는 메서드
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // 다음 활동으로 이동하는 메서드
    private void sendUserToNextActivity() {
        Intent intent = new Intent(LoginActivity.this, NaviActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
