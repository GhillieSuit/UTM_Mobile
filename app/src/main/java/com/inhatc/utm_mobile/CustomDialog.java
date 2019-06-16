package com.inhatc.utm_mobile;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017-08-07.
 */

public class CustomDialog extends Dialog implements View.OnClickListener{

    private EditText title;
    private EditText message;
    private CustomDialogListener customDialogListener;
    private Context context;

    public CustomDialog(Context context)
    {
        super (context);
        this.context = context;
        //super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    //인터페이스 설정
    interface CustomDialogListener{
        void onPositiveClicked(String t, String m);
        void onNegativeClicked();
    }

    //호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

/*        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);*/

        setContentView(R.layout.custom_dialog);

        title = (EditText)findViewById(R.id.markertitle);
        message = (EditText)findViewById(R.id.markermessage);

        Button oBtn = (Button)this.findViewById(R.id.okButton);
        Button cBtn = (Button)this.findViewById(R.id.cancelButton);
        //버튼 클릭 리스너 등록
        oBtn.setOnClickListener(this);
        cBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.okButton: //확인 버튼을 눌렀을 때
                //각각의 변수에 EidtText에서 가져온 값을 저장
                String t = title.getText().toString();
                String m = message.getText().toString();
                //Double latitude = point.latitude; // 위도
                //Double longitude = point.longitude; // 경도
                customDialogListener.onPositiveClicked(t,m);
                dismiss();
                break;
            case R.id.cancelButton: //취소 버튼을 눌렀을 때
                cancel();
                break;
        }
    }
}
