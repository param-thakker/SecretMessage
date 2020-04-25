package com.example.param.secretmessage;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Message extends AppCompatActivity {
    TextView textView2;
    TextView textView3;
    Button btnCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        textView2=(TextView)findViewById(R.id.textView2);
        btnCopy=(Button)findViewById(R.id.btnCopy);
        textView3=(TextView)findViewById(R.id.textView3);

        int key=getIntent().getExtras().getInt("key");
        String message;
        if (key==0){
            textView3.setText("Encrypted Message is:");
            message=getIntent().getExtras().getString("Encrypted");
        }

        else{
            textView3.setText("Decrypted Message is:");
            message=getIntent().getExtras().getString("Decrypted");
        }

        textView2.setText(message);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("encryped text", textView2.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });
    }
}
