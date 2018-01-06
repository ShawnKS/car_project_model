package shawn.car_project;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CarMainActivity extends AppCompatActivity {
    String videoUrl,controlUrl,port;
    EditText CameraIP,ControlIP,Port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_main);
        if (Build.VERSION.SDK_INT >= 11) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraIP = (EditText)findViewById(R.id.cameraIP);
                ControlIP = (EditText)findViewById(R.id.controlIP);
                Port = (EditText)findViewById(R.id.port);
                videoUrl = CameraIP.getText().toString();
                controlUrl = ControlIP.getText().toString();
                port = Port.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("CameraIP",videoUrl);
                intent.putExtra("ControlIP",controlUrl);
                intent.putExtra("Port",port);
                intent.putExtra("Is_scale",true);
                intent.setClass(CarMainActivity.this,driveActivity.class);
                startActivity(intent);
            }
        });
    }
}
