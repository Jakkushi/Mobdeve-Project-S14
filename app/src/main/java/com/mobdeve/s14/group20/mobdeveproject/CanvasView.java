package com.mobdeve.s14.group20.mobdeveproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CanvasView extends View {

    private final int paintColor = Color.BLACK;
    private Paint paint;
    private Path path = new Path();

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.setDrawingCacheEnabled(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                break;
            default:
                return false;
        }

        postInvalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    public void clearScreen() {
        path.reset();
        postInvalidate();
    }

    public void saveScreen(CharSequence filename) throws FileNotFoundException {
        Integer increment = 0; //Generates a counter for duplicate names
        File mainPath = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES) + "/Tous Les Jours/"); // Main folder

        //Check if the main folder already exists
        if (!mainPath.exists()) {
            mainPath.mkdirs();
        }

        File sketchPath = new File(mainPath + "/Sketches"); // Create subfolder

        //Check if the subfolder already exists
        if (!sketchPath.exists()) {
            sketchPath.mkdirs();
        }

        Bitmap b = viewToBitmap(this); //convert the view to a bitmap to save

        //generate the filename of the bitmap to save
        File outputFile = new File(sketchPath, filename+"_"+increment.toString()+".png");
        Log.i("FILENAME", filename.toString());
        while (outputFile.exists()) {
            increment++;
            outputFile = new File(sketchPath, filename+"_"+increment.toString()+".png");
            //Log.i("FILENAME", filename.toString());
        }

        //save the bitmap as a file
        b.compress(Bitmap.CompressFormat.PNG, 95, new FileOutputStream(outputFile));
    }

    private void setupPaint() {
        paint = new Paint();
        paint.setColor(paintColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    private Bitmap viewToBitmap (View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}
