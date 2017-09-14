package fr.goui.dominosfevahh.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import fr.goui.dominosfevahh.event_bus.BoardSizeChangedEvent;
import fr.goui.dominosfevahh.model.Domino;

/**
 * Draws the game board.
 */
public class GameBoardView extends View {
    private static final String TAG = GameBoardView.class.getSimpleName();

    private Point screenSize;

    private Paint blackPaint;

    private int width;

    private int height;

    private List<Domino> dominoes;

    public GameBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // getting screen size
        screenSize = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(screenSize);
        // creating paints
        blackPaint = new Paint();
        blackPaint.setColor(ContextCompat.getColor(context, android.R.color.black));
        blackPaint.setStyle(Paint.Style.STROKE);
        blackPaint.setStrokeWidth(1);
        blackPaint.setAntiAlias(true);
        //
        dominoes = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        EventBus.getDefault().post(new BoardSizeChangedEvent(width, height));
        Log.i(TAG, "onMeasure: " + width + ";" + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBoard(canvas);
        drawDominoes(canvas);
    }

    private void drawBoard(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, blackPaint);
    }

    private void drawDominoes(Canvas canvas) {
        for (Domino domino : dominoes) {
            DominoDrawer.drawDomino(domino, true, canvas, blackPaint);
        }
    }

    public void addDomino(Domino domino) {
        dominoes.add(domino);
        invalidate();
    }
}
