package shawn.car_project;


import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder.Callback;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class MySurface extends SurfaceView implements Callback
{
    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";
    private SurfaceHolder sfh;
    private Canvas canvas;
    private String urlstr;
    private Paint p;
    private Bitmap mBitmap;
    private Bitmap bmp;
    private int count = 0;
    private static int mScreenWidth;
    private static int mScreenHeight;
    InputStream inputStream = null;
    OutputStream outputStream =null;
    public boolean Is_Scale = false;

    public MySurface(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
        p = new Paint();

        p.setAntiAlias(true);
        sfh = this.getHolder();
        sfh.addCallback(this);
        this.setKeepScreenOn(true);
        setFocusable(true);
        this.getWidth();
        this.getHeight();

    }

    private void initialize()
    {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        this.setKeepScreenOn(true);
    }



    class DrawVideo extends Thread
    {
        public DrawVideo(){}

        @Override
        protected Object clone() throws CloneNotSupportedException
        {
            // TODO Auto-generated method stub
            return super.clone();
        }

        public void run()
        {
            Paint pt = new Paint();
            pt.setAntiAlias(true);
            pt.setColor(Color.GREEN);
            pt.setTextSize(20);
            pt.setStrokeWidth(1);

            int bufSize = 512 * 1024;
            byte[] jpg_buf = new byte[bufSize];	        // buffer to read jpg

            int readSize = 4096;
            byte[] buffer = new byte[readSize];	        // buffer to read stream

            while (true)
            {
                long Time = 0;
                long Span = 0;
                int fps = 0;
                String str_fps = "0 fps";

                URL url = null;
                HttpURLConnection urlConn = null;

                try
                {
                    url = new URL(urlstr);
                    urlConn = (HttpURLConnection)url.openConnection();

                    Time = System.currentTimeMillis();

                    int read = 0;
                    int status = 0;
                    int jpg_count = 0;


                    while (true)
                    {
                        read = urlConn.getInputStream().read(buffer, 0, readSize);

                        if (read  > 0)
                        {

                            for (int i = 0; i < read; i++)
                            {
                                switch (status)
                                {
                                    //Content-Length:
                                    case 0: if (buffer[i] == (byte)'C') status++; else status = 0; break;
                                    case 1: if (buffer[i] == (byte)'o') status++; else status = 0; break;
                                    case 2: if (buffer[i] == (byte)'n') status++; else status = 0; break;
                                    case 3: if (buffer[i] == (byte)'t') status++; else status = 0; break;
                                    case 4: if (buffer[i] == (byte)'e') status++; else status = 0; break;
                                    case 5: if (buffer[i] == (byte)'n') status++; else status = 0; break;
                                    case 6: if (buffer[i] == (byte)'t') status++; else status = 0; break;
                                    case 7: if (buffer[i] == (byte)'-') status++; else status = 0; break;
                                    case 8: if (buffer[i] == (byte)'L') status++; else status = 0; break;
                                    case 9: if (buffer[i] == (byte)'e') status++; else status = 0; break;
                                    case 10: if (buffer[i] == (byte)'n') status++; else status = 0; break;
                                    case 11: if (buffer[i] == (byte)'g') status++; else status = 0; break;
                                    case 12: if (buffer[i] == (byte)'t') status++; else status = 0; break;
                                    case 13: if (buffer[i] == (byte)'h') status++; else status = 0; break;
                                    case 14: if (buffer[i] == (byte)':') status++; else status = 0; break;
                                    case 15:
                                        if (buffer[i] == (byte)0xFF) status++;
                                        jpg_count = 0;
                                        jpg_buf[jpg_count++] = (byte)buffer[i];
                                        break;
                                    case 16:
                                        if (buffer[i] == (byte)0xD8)
                                        {
                                            status++;
                                            jpg_buf[jpg_count++] = (byte)buffer[i];
                                        }
                                        else
                                        {
                                            if (buffer[i] != (byte)0xFF) status = 15;

                                        }
                                        break;
                                    case 17:
                                        jpg_buf[jpg_count++] = (byte)buffer[i];
                                        if (buffer[i] == (byte)0xFF) status++;
                                        if (jpg_count >= bufSize) status = 0;
                                        break;
                                    case 18:
                                        jpg_buf[jpg_count++] = (byte)buffer[i];
                                        if (buffer[i] == (byte)0xD9)
                                        {
                                            status = 0;

                                            fps++;
                                            Span = System.currentTimeMillis()-Time;
                                            if(Span > 1000L)
                                            {
                                                Time = System.currentTimeMillis();
                                                str_fps = String.valueOf(fps)+" fps";
                                                fps = 0;
                                            }

                                            canvas = sfh.lockCanvas();
                                            canvas.drawColor(Color.BLACK);

                                            bmp = BitmapFactory.decodeStream(new ByteArrayInputStream(jpg_buf));

                                            int width = mScreenWidth;
                                            int height = mScreenHeight;

                                            float rate_width = (float)mScreenWidth / (float)bmp.getWidth();
                                            float rate_height = (float)mScreenHeight / (float)bmp.getHeight();

                                            if(Is_Scale)
                                            {
                                                if(rate_width>rate_height) width = (int)((float)bmp.getWidth()*rate_height);
                                                if(rate_width<rate_height) height = (int)((float)bmp.getHeight()*rate_width);

                                            }

                                            mBitmap = Bitmap.createScaledBitmap(bmp, width, height, false);
                                            canvas.drawBitmap(mBitmap, (mScreenWidth-width)/2,(mScreenHeight-height)/2, null);

                                            canvas.drawText(str_fps, 2, 22, pt);

                                            sfh.unlockCanvasAndPost(canvas);

                                        }
                                        else
                                        {
                                            if (buffer[i] != (byte)0xFF) status = 17;
                                        }
                                        break;
                                    default:
                                        status = 0;
                                        break;

                                }
                            }
                        }
                    }
                }
                catch (IOException ex)
                {
                    urlConn.disconnect();
                    ex.printStackTrace();
                }
            }

        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    public void GetCameraIP(String p){urlstr=p;}
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }
    public static String getSDPath(){
        return Environment.getExternalStorageDirectory().getPath();
    }
    public void saveFile(Context context) throws IOException {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast toast=Toast.makeText(context, "已截图", Toast.LENGTH_SHORT);
            toast.show();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void recordFile() throws IOException {
        File filePic;
        Bitmap bm = bmp;
        String path =  getSDPath() +"/carproject/";
        String fileName = String.format("%0"+3+"d", count);
        count++;
        try {
            filePic = new File(path + fileName + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void initialcount()
    {
        count = 0;
    }
    public void surfaceCreated(SurfaceHolder holder) {
        new DrawVideo().start();

    }
}