package com.example.param.secretmessage;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    Button btnEncrypt;
    Button btnDecrypt;
    Button btnPaste;
    TextInputEditText editKey;
    TextInputEditText editMessage;
    String message;
    String key;
    String encryptedMessage;
    String outputString;
    String AES="AES";
    DatabaseReference databaseMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseMessages= FirebaseDatabase.getInstance().getReference("secretmessage");

        btnEncrypt=(Button)findViewById(R.id.btnEncrypt);
        btnDecrypt=(Button)findViewById(R.id.btnDecrypt);
        btnPaste=(Button)findViewById(R.id.btnPaste);

        editMessage=(TextInputEditText)findViewById(R.id.editMessage);
        editKey=(TextInputEditText)findViewById(R.id.editKey);

        editMessage.addTextChangedListener(loginWatcher);
        editKey.addTextChangedListener(loginWatcher);


        btnEncrypt.setEnabled(false);
        btnDecrypt.setEnabled(false);

        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editMessage.getText().toString().isEmpty() && !editKey.getText().toString().isEmpty() ) {
                    try {
                        outputString = encrypt(editMessage.getText().toString(), editKey.getText().toString());
                        message=editMessage.getText().toString();
                        key=editKey.getText().toString();
                        encryptedMessage=outputString;

                        Intent intent = new Intent(MainActivity.this, Message.class);
                        intent.putExtra("Encrypted", outputString);
                        intent.putExtra("key",0);
                        //addMessage();
                        String method="register";
                        BackgroundTask backgroundTask=new BackgroundTask(MainActivity.this);
                        backgroundTask.execute(method,message,key,encryptedMessage);
                        finish();



                        startActivity(intent);
                        editMessage.setText("");
                        editKey.setText("");
                        //outputText.setText(outputString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    encryptedMessage=editMessage.getText().toString();
                    key=editKey.getText().toString();
                    String method="retrieve";
                    BackgroundTask backgroundTask=new BackgroundTask(MainActivity.this);
                    backgroundTask.execute(method,encryptedMessage,key);


                    //finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                editMessage.setText(item.getText().toString());

            }
        });



    }
    private TextWatcher loginWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String message=editMessage.getText().toString().trim();
            String key=editKey.getText().toString().trim();
            btnEncrypt.setEnabled(!message.isEmpty() && !key.isEmpty());
            btnDecrypt.setEnabled(!message.isEmpty() && !key.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private String decrypt(String outputString, String password) throws Exception {
        SecretKeySpec key =generateKey(password);
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedValue=Base64.decode(outputString,Base64.DEFAULT);
        byte[] decValue=c.doFinal(decodedValue);
        String decrypted=new String(decValue);
        return decrypted;
    }

    private String encrypt(String data, String password) throws Exception {
        SecretKeySpec key =generateKey(password);
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal=c.doFinal(data.getBytes());
        String encyptedValue= Base64.encodeToString(encVal,Base64.DEFAULT);
        return encyptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] bytes=password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key=digest.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key,"AES");
        return secretKeySpec;

    }


}
