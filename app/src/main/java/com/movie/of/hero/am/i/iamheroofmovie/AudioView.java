package com.movie.of.hero.am.i.iamheroofmovie;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AudioView extends View {
    Bitmap mTmpImage;
    private ArrayList<Byte> mByteList;
    private Rect mRect = new Rect();
    private Rect mVisibleRect;
    private float mPointX = 0;
    private int mLastIndex = 0;

    private Paint mForePaint = new Paint();

    public AudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mVisibleRect = new Rect();
        mByteList = new ArrayList<Byte>();
        mForePaint.setStrokeWidth(1f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(Color.rgb(0, 128, 255));
    }

    public void updateVisualizer(byte[] bytes) {
        for(byte b : bytes){
            mByteList.add(b);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerY = mVisibleRect.centerY();
        Canvas bmp = new Canvas(mTmpImage);

        for(int i = mLastIndex;i < mByteList.size();++i){
            if(i + 1 >= mByteList.size()) continue;
            float next = mPointX + 1;
            bmp.drawLine(mPointX, centerY + mByteList.get(i), next, centerY + mByteList.get(i + 1), mForePaint);
            mPointX = next;
        }
        canvas.drawBitmap(mTmpImage, null, mVisibleRect, null);
        mLastIndex = mByteList.size() - 1;
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        mVisibleRect = new Rect(0,0,xNew, yNew);
        mTmpImage = Bitmap.createBitmap(xNew, yNew, Bitmap.Config.ARGB_8888);
    }

    public void release() {
        if(mTmpImage != null){
            mTmpImage.recycle();
            mTmpImage = null;
        }
    }
}
