package jp.ac.ritsumei.ise.phy.exp2.is0429rs.canvassample04;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import static android.view.MotionEvent.ACTION_DOWN;

public class MySurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private SurfaceHolder sHolder;
    private Thread thread;



    public MySurfaceView(Context context) {
        super(context);
        initialize();

    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        sHolder = getHolder();
        sHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //drawDuck();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;

    }

    static final long FPS = 60;
    static final long FRAME_TIME = 1000 / FPS;
    boolean flag = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == ACTION_DOWN) {
                flag = !flag;
            }
        return super.onTouchEvent(event);

    }

    @Override
    public void run() {
        long loopCount = 0;
        long waitTime = 0;

        /*描画初期設定を行う*/
        Canvas c = sHolder.lockCanvas();
        c.drawColor(Color.WHITE);
        Paint p = new Paint();

        float x = c.getWidth() / 2;
        float y = c.getHeight() / 2;
        float vx = 10;
        float vy = 10;

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.wheelduck3);

        Rect srcRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        Rect dectRect = new Rect(0, 0, c.getWidth() / 5, c.getHeight() / 5);

        //c.drawBitmap(bmp, srcRect, dectRect, p);
        sHolder.unlockCanvasAndPost(c);



        long startTime = System.currentTimeMillis();

        while(thread != null) {
            try {
                loopCount++;

                if (flag == true) {
                    x = x + vx;
                } else {
                    y = y + vy;
                }

                sHolder.lockCanvas();

                if (x >= c.getWidth() - 5 ||  x < 0) {
                    vx = -1 * vx;

                }

                if (y >= c.getHeight() - 5 || y < 0) {
                    vy = -1 * vy;
                }

                c.drawColor(Color.WHITE);
                c.translate(x, y);
                c.drawBitmap(bmp, srcRect, dectRect, p);

                sHolder.unlockCanvasAndPost(c);

                waitTime = (loopCount * FRAME_TIME) - (System.currentTimeMillis() - startTime);

                if(waitTime > 0) {
                    Thread.sleep(waitTime);
                }
            } catch (InterruptedException e) {

            }
        }

    }

}
