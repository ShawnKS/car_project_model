package shawn.car_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.Image;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

public class driveActivity extends AppCompatActivity {
    public static String CameraIP;
    public static String CtrlIP;
    public static String CtrlPort;
    private Socket socket;
    private float startY=0;
    private float startX=0;
    private int angleX=1;
    private int angleY=1;
    private int videostatus =0;
    private int dangle=0;
    private View mVideoView;
    MySurface r;
    OutputStream socketWriter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVideoView = LayoutInflater.from(this).inflate(R.layout.activity_drive, null);
        setContentView(mVideoView);
        r = (MySurface)findViewById(R.id.mysurfaceViewVideo);
        LongClickButton btnForward = (LongClickButton) findViewById(R.id.button_forward);
        LongClickButton btnBackward = (LongClickButton) findViewById(R.id.button_backward);
        LongClickButton btnLeft = (LongClickButton) findViewById(R.id.button_left);
        LongClickButton btnRight = (LongClickButton) findViewById(R.id.button_right);
        Button photo = (Button)findViewById(R.id.button_photo);
        Button P = (Button)findViewById(R.id.P);
        ImageButton up = (ImageButton)findViewById(R.id.up);
        ImageButton down = (ImageButton)findViewById(R.id.down);
        ImageButton left = (ImageButton)findViewById(R.id.left);
        ImageButton right = (ImageButton)findViewById(R.id.right);
        final Button video = (Button)findViewById(R.id.button_vedio);
        Button zhua=(Button)findViewById(R.id.zhua);
        Button song=(Button)findViewById(R.id.song);
        Intent intent = getIntent();
        CameraIP = intent.getStringExtra("CameraIP");
        CtrlIP = intent.getStringExtra("ControlIP");
        CtrlPort = intent.getStringExtra("Port");
        r.GetCameraIP(CameraIP);
        InitSocket();
        Log.d("vantasywificar", "try to initsocket controlIP is :++++" + CtrlIP);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable videoRun=new Runnable() {
                    @Override
                    public void run() {
                        while(videostatus==0)
                            try {
                                r.saveFile(driveActivity.this);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                };
                ThreadPoolUtils.execute(videoRun);
                }
        });
        up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0xff});
                            Stop();
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x03, (byte) 0x02, (byte) 0x02, (byte) 0xff});
                            Stop();
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0xff});
                            Stop();
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x03, (byte) 0x02, (byte) 0x00, (byte) 0xff});
                            Stop();
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0xff});
                            Stop();
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x03, (byte) 0x01, (byte) 0x02, (byte) 0xff});
                            Stop();
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0xff});
                            Stop();
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x03, (byte) 0x01, (byte) 0x00, (byte) 0xff});
                            Stop();
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0xff});
                    try {
                        Thread.sleep(120);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    Stop();
                    socketWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        zhua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x03, (byte) 0x03, (byte) 0x02, (byte) 0xff});
                    try {
                        Thread.sleep(120);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    Stop();
                    socketWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x03, (byte) 0x03, (byte) 0x00, (byte) 0xff});
                    try {
                        Thread.sleep(120);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    Stop();
                    socketWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        try {
                            Stop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0xff});
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        btnBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        try {
                            Stop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0xff});
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        try {
                            Stop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0xff});
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        try {
                            Stop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        try {
                            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0xff});
                            socketWriter.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (videostatus)
                {
                    case 0:
                        startrecord();
                        videostatus = 1;
                        video.setText("停止并输出");
                        break;
                    case 1:
                        record();
                        videostatus = 0;
                        video.setText("开始录制");
                        break;
                        default:
                            break;
                }
            }
        });
    }
    public void startrecord()
    {
        Runnable videoRun=new Runnable() {
        @Override
        public void run() {
            while(videostatus==0)
                try {
                    r.recordFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    };
        ThreadPoolUtils.execute(videoRun);
    }
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float distanceX = startY - endX;
                float distanceY = startY - endY;
                    final double FLING_MIN_DISTANCE = 0.5;
                    final double FLING_MIN_VELOCITY = 0.5;
                if (distanceX > FLING_MIN_DISTANCE && Math.abs(distanceX) > FLING_MIN_VELOCITY) {
                    turnX(1);
                }
                if (distanceX < FLING_MIN_DISTANCE && Math.abs(distanceX) > FLING_MIN_VELOCITY) {
                    turnX(-1);
                }
                    if (distanceY > FLING_MIN_DISTANCE && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                        turnY(1);
                    }
                    if (distanceY < FLING_MIN_DISTANCE && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                        turnY(-1);
                    }
                break;
        }

        return super.onTouchEvent(event);
    }
    //            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        socket = new Socket(InetAddress.getByName(CtrlIP), Integer.parseInt(CtrlPort));
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
        public void InitSocket(){
        try {
            socket = new Socket(InetAddress.getByName(CtrlIP), Integer.parseInt(CtrlPort));
        } catch (IOException e) {
            e.printStackTrace();
        }
            try{
            socketWriter = socket.getOutputStream();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void turnX(int ang) {
        if(angleX<=0)angleX=1;
        if(angleX>=180)angleX=179;
        if (angleX > 0 && angleX < 180) {
            angleX = angleX + ang;
            try {
                socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x01, (byte) 0x07, (byte) angleX, (byte) 0xff});
                socketWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Stop();
    }
    public  void turnY(int ang) {
        if(angleY<=0)angleY=1;
        if(angleY>=90)angleY=89;
        if (angleY > 0 && angleY < 90) {
            angleY = angleY + ang;
            try {
                socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x01, (byte) 0x08, (byte) angleY, (byte) 0xff});
                socketWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Stop();
    }
    public void record()
    {
        Runnable compoundRun=new Runnable() {
            @Override
            public void run() {
                String imageUrl = Environment.getExternalStorageDirectory().getPath()+"/carproject/";
                String outputUrl = Environment.getExternalStorageDirectory().getPath() +"/carproject/";
                String[] commands = new String[10];
                commands[0] = "ffmpeg";
                commands[1] = "-f";
                commands[2] = "'%03d.jpeg'";
                commands[3] = "-i";
                commands[4] = imageUrl;
                commands[5] = "-vcodec";
                commands[6] = "libx264";
                commands[7] = "-r";
                commands[8] = "10";
                commands[9] = outputUrl;

                FFmpegKit.execute(commands, new FFmpegKit.KitInterface() {
                    @Override
                    public void onStart() {
                        Log.d("FFmpegLog LOGCAT","FFmpeg 命令行开始执行了...");
                    }

                    @Override
                    public void onProgress(int progress) {
                        Log.d("FFmpegLog LOGCAT","done com"+"FFmpeg 命令行执行进度..."+progress);
                    }

                    @Override
                    public void onEnd(int result) {
                        Log.d("FFmpegLog LOGCAT","FFmpeg 命令行执行完成...");
//                        getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//                        Message msg = new Message();
//                        msg.what = 1;
//                        mHandler.sendMessage(msg);
                    }
                });
            }
        };
        ThreadPoolUtils.execute(compoundRun);
    }
    public void Stop()
    {
        try {
            socketWriter.write(new byte[]{(byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff});
            socketWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        public void onDestroy(){
        super.onDestroy();
        }
}
