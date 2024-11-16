package com.example.hihi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {

    private EditText edtPhoneNumber, edtOtpCode;
    private Button btnSendOtp, btnVerifyOtp;
    private FirebaseAuth mAuth;
    private String verificationId;
    private ProgressDialog progressDialog;

    private static final String TAG = "LoginOtpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        mAuth = FirebaseAuth.getInstance();
        edtPhoneNumber = findViewById(R.id.edt_phone_number1);
        edtOtpCode = findViewById(R.id.edt_otp_code1);
        btnSendOtp = findViewById(R.id.btn_send_otp1);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp1);

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edtPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(LoginOtpActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendOtp(phoneNumber);
            }
        });

        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpCode = edtOtpCode.getText().toString().trim();
                if (TextUtils.isEmpty(otpCode)) {
                    Toast.makeText(LoginOtpActivity.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyOtp(otpCode);
            }
        });
    }

    private void sendOtp(String phoneNumber) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang gửi OTP...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)        // Số điện thoại cần xác minh
                        .setTimeout(60L, TimeUnit.SECONDS)  // Thời gian chờ mã OTP
                        .setActivity(this)                 // Activity hiện tại
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                // Tự động xác minh thành công hoặc mã đã được điền sẵn
                                progressDialog.dismiss();
                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginOtpActivity.this, "Gửi OTP thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, "onVerificationFailed", e);
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginOtpActivity.this, "OTP đã được gửi", Toast.LENGTH_SHORT).show();
                                LoginOtpActivity.this.verificationId = verificationId;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOtp(String otpCode) {
        if (verificationId == null) {
            Toast.makeText(this, "Vui lòng gửi OTP trước", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xác minh OTP...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otpCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Xác thực thành công
                            Toast.makeText(LoginOtpActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginOtpActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            // Nếu xác thực thất bại
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginOtpActivity.this, "Mã OTP không hợp lệ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}