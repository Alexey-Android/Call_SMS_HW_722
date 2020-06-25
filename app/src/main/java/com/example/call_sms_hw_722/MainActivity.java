package com.example.call_sms_hw_722;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {


        Button btnCall = findViewById(R.id.button_call);
        Button btnSMS = findViewById(R.id.button_sms);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callByNumber();
            }
        });

        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendTextMessage();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
// Проверяем результат запроса на право позвонить
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// Разрешение получено, осуществляем звонок
                    callByNumber();
                } else {
// Разрешение не дано. Закрываем приложение
                    finish();
                }
            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// Разрешение получено, осуществляем звонок

                    sendTextMessage();
                } else {
// Разрешение не дано. Закрываем приложение
                    finish();
                }
            }
        }
    }

    private void callByNumber() {

        final EditText etCall = findViewById(R.id.et_call);
        final String call = etCall.getText().toString();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
// Разрешение не получено
// Делаем запрос на добавление разрешения звонка
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
// Разрешение уже получено
            Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call));
// Звоним
            startActivity(dialIntent);
        }
    }

    @SuppressLint("IntentReset")
    private void sendTextMessage() {

        final EditText etSMS = findViewById(R.id.et_sms);
        final String sms = etSMS.getText().toString();
        final EditText etCall = findViewById(R.id.et_call);
        final String call = etCall.getText().toString();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
// Разрешение не получено
// Делаем запрос на добавление разрешения отправки SMS
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
// Разрешение уже получено
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("smsto:"));
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", call);
            smsIntent.putExtra("sms_body", sms);
            startActivity(Intent.createChooser(smsIntent, "Отправить смс с помощью"));
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(call, null, sms, null, null);

        }
    }
}