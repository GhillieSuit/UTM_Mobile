package com.inhatc.utm_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnOpt;
    private CheckBox chkLayer1;
    private CheckBox chkLayer2;
    private CheckBox chkLayer3;
    private CheckBox chkLayer4;
    private CheckBox chkLayer5;
    private CheckBox chkLayer6;
    private int checked = 63;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        chkLayer1 = (CheckBox)findViewById(R.id.chkBox1);
        chkLayer1.setOnClickListener(this);
        chkLayer2 = (CheckBox)findViewById(R.id.chkBox2);
        chkLayer2.setOnClickListener(this);
        chkLayer3 = (CheckBox)findViewById(R.id.chkBox3);
        chkLayer3.setOnClickListener(this);
        chkLayer4 = (CheckBox)findViewById(R.id.chkBox4);
        chkLayer4.setOnClickListener(this);
        chkLayer5 = (CheckBox)findViewById(R.id.chkBox5);
        chkLayer5.setOnClickListener(this);
        chkLayer6 = (CheckBox)findViewById(R.id.chkBox6);
        chkLayer6.setOnClickListener(this);

        btnOpt = (Button)findViewById(R.id.btnOpt2);
        btnOpt.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String strData = extras.getString("chkVar");
            setCheckBox(strData);
        }
    }

    public void onClick(View v){
        if (v == btnOpt) {
            Intent CallIntent = getIntent();
            CallIntent.putExtra("chkVar", Integer.toString(checked));
            setResult(RESULT_OK, CallIntent);
            finish();
        } else {
            checked = 0;
            if (chkLayer1.isChecked()) checked += 1;
            if (chkLayer2.isChecked()) checked += 2;
            if (chkLayer3.isChecked()) checked += 4;
            if (chkLayer4.isChecked()) checked += 8;
            if (chkLayer5.isChecked()) checked += 16;
            if (chkLayer6.isChecked()) checked += 32;
        }
    }

    public void setCheckBox(String chkVar) {
        checked = Integer.parseInt(chkVar);

        chkLayer1.setChecked(false);
        chkLayer2.setChecked(false);
        chkLayer3.setChecked(false);
        chkLayer4.setChecked(false);
        chkLayer5.setChecked(false);
        chkLayer6.setChecked(false);

        if ((checked & 1) == 1) chkLayer1.setChecked(true);
        if ((checked & 2) == 2) chkLayer2.setChecked(true);
        if ((checked & 4) == 4) chkLayer3.setChecked(true);
        if ((checked & 8) == 8) chkLayer4.setChecked(true);
        if ((checked & 16) == 16) chkLayer5.setChecked(true);
        if ((checked & 32) == 32) chkLayer6.setChecked(true);
    }
}
