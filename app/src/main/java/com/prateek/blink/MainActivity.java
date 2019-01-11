package com.prateek.blink;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    int clickCount = 0;
    Button btn_ok;
    EditText et_name;
    TextView tv_app_name, tv_label_say_name;
    ConstraintLayout cslayout;

    ToggleButton tb_switch;
    boolean flashLightStatus = false;
    boolean deviceHasCameraFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(deviceHasCameraFlash){
            if(flashLightStatus == false){//when light on
                FlashLightOn();//we should off
            }
        }

        cslayout = (ConstraintLayout) findViewById(R.id.cslayout);



        tv_label_say_name = (TextView) findViewById(R.id.tv_label_say_name);
        tv_app_name = (TextView) findViewById(R.id.tv_app_name);

        btn_ok = (Button) findViewById(R.id.btn_ok);
        setButtonFor(clickCount);

        tb_switch = (ToggleButton) findViewById(R.id.tb_switch);
        //tb_switch.setVisibility(View.INVISIBLE);

        et_name = (EditText) findViewById(R.id.et_name);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btn_ok.setText(R.string.said);
                btn_ok.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        deviceHasCameraFlash = getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (clickCount){
                    case 0:
                        String name = et_name.getText().toString();
                        tv_app_name.append(": Hey,"+name);
                        tv_label_say_name.setVisibility(View.INVISIBLE);
                        et_name.setVisibility(View.INVISIBLE);
                        clickCount++;
                        setButtonFor(clickCount);
                        break;
                    case 1:
                        clickCount++;
                        setButtonFor(clickCount);
                        //readNotifications();
                        break;
                    default:
                        Log.d(TAG, "onClick: "+clickCount);
                        break;


                }

            }
        });

        tb_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deviceHasCameraFlash){
                    if(flashLightStatus){//when light on
                        FlashLightOff();//we should off
                    }
                    else { //when light off
                        FlashLightOn();//we should on
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "No flash available on your device.", Toast.LENGTH_SHORT).show();
                }

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(deviceHasCameraFlash){
            if(flashLightStatus == false){//when light on
                FlashLightOn();//we should off
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(deviceHasCameraFlash){
            if(flashLightStatus == true){//when light on
                FlashLightOff();//we should off
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(deviceHasCameraFlash){
            if(flashLightStatus == true){//when light on
                FlashLightOff();//we should off
            }
        }
    }

    public void setButtonFor(int clickCount){
        switch (clickCount){
            case 0:
                btn_ok.setText(R.string.type_name);
                btn_ok.setTextColor(Color.WHITE);
                btn_ok.setEnabled(true); // TODO: 08-Oct-17  make this false post development
                btn_ok.setBackgroundResource(R.color.colorPurple);
                break;
            case 1:
                btn_ok.setText(R.string.use_flash);
                btn_ok.setEnabled(false);
                tb_switch.setVisibility(View.VISIBLE);
                btn_ok.setBackgroundResource(R.color.colorYellow);
                break;
            case 2:
                break;
            default:
                Log.d(TAG, "setButtonFor: Default");
                break;
        }
    }

    private void FlashLightOn() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{
            String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
            camManager.setTorchMode(cameraId, true);
            flashLightStatus=true;
            tb_switch.setBackgroundResource(R.color.colorTurqouise);
            tb_switch.setTextColor(Color.WHITE);
            cslayout.setBackgroundResource(R.color.colorWhite);
        } catch (Exception e){

        }
    }
    private void FlashLightOff() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        //camManager.registerTorchCallback();
        try{

            String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
            camManager.setTorchMode(cameraId, false);
            flashLightStatus=false;
            tb_switch.setBackgroundResource(R.color.colorYellow);
            tb_switch.setTextColor(Color.WHITE);
            cslayout.setBackgroundResource(R.color.colorSilver);
        } catch (Exception e){

        }
    }
}
