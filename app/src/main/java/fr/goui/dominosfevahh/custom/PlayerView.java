package fr.goui.dominosfevahh.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.goui.dominosfevahh.Constants;
import fr.goui.dominosfevahh.R;
import fr.goui.dominosfevahh.event_bus.DominoPlayedEvent;
import fr.goui.dominosfevahh.event_bus.DominoTapEvent;
import fr.goui.dominosfevahh.model.Domino;

/**
 * Draws one player's dominoes.
 */
public class PlayerView extends View {
    private static final String TAG = PlayerView.class.getSimpleName();

    private Point screenSize;

    private Paint blackPaint;

    private Paint greyFilledPaint;

    private Paint textBlackPaint;

    private Paint greenFilledPaint;

    private Paint whiteFilledPaint;

    private int width;

    private int height;

    private List<Domino> dominoes;

    private Map<Rect, Domino> items;

    private boolean isHorizontal;

    private boolean isPlayer = true;

    private boolean isTouched;

    private int touchedPositionX;

    private int touchedPositionY;

    private boolean isMyTurn;

    public PlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // getting the orientation
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PlayerView,
                0, 0);
        try {
            isHorizontal = a.getInteger(R.styleable.PlayerView_orientation, 0) == 0;
        } finally {
            a.recycle();
        }
        // initialization
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
        greyFilledPaint = new Paint();
        greyFilledPaint.setColor(ContextCompat.getColor(context, R.color.colorLightTransparentGrey));
        greyFilledPaint.setStyle(Paint.Style.FILL);
        greyFilledPaint.setAntiAlias(true);
        textBlackPaint = new Paint();
        textBlackPaint.setColor(ContextCompat.getColor(context, android.R.color.black));
        textBlackPaint.setTextSize(32);
        textBlackPaint.setTextAlign(Paint.Align.CENTER);
        textBlackPaint.setAntiAlias(true);
        textBlackPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        greenFilledPaint = new Paint();
        greenFilledPaint.setColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
        greenFilledPaint.setStyle(Paint.Style.FILL);
        greenFilledPaint.setAntiAlias(true);
        whiteFilledPaint = new Paint();
        whiteFilledPaint.setColor(ContextCompat.getColor(context, android.R.color.white));
        whiteFilledPaint.setStyle(Paint.Style.FILL);
        whiteFilledPaint.setAntiAlias(true);
        //
        dominoes = new ArrayList<>();
        items = new HashMap<>();
    }

    /**
     * Sets the dominoes list.
     * They will be drawn.
     *
     * @param dominoes the list of dominoes
     */
    public void setDominoesList(List<Domino> dominoes) {
        this.dominoes = dominoes;
        invalidate();
    }

    /**
     * Indicates if this is a player or a computer.
     * We don't want to show an opponent's dominoes.
     * We want to be able to tap a player's dominoes.
     *
     * @param player true if a player, false otherwise
     */
    public void setPlayer(boolean player) {
        this.isPlayer = player;
    }

    /**
     * Determines if this is this player's turn to play;
     *
     * @param isMyTurn true if it is this player's turn to play, dalse otherwise
     */
    public void setMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
        invalidate();
    }

    /**
     * Adds a domino to the current list.
     *
     * @param domino the added domino
     */
    public void addDomino(Domino domino) {
        dominoes.add(domino);
        items.clear();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (dominoes != null) {
            // domino's top;left coordinates
            int left = isHorizontal ?
                    (width - (dominoes.size() * Constants.DOMINO_WIDTH + (dominoes.size() - 1) * Constants.DOMINO_MARGIN)) / 2 :
                    (width - Constants.DOMINO_HEIGHT) / 2;
            int top = isHorizontal ?
                    (height - Constants.DOMINO_HEIGHT) / 2 :
                    (height - (dominoes.size() * Constants.DOMINO_WIDTH + (dominoes.size() - 1) * Constants.DOMINO_MARGIN)) / 2;
            // drawing all dominoes
            canvas.drawColor(Color.WHITE);
            for (Domino domino : dominoes) {
                // setting up domino
                domino.setX(left);
                domino.setY(top);
                domino.setVertical(isHorizontal);
                // drawing
                Rect rect = DominoDrawer.drawDomino(domino, isPlayer, canvas, blackPaint);
                if (isPlayer) { // if this is a player we keep the placement of all dominoes
                    items.put(rect, domino);
                }
                // offset for the next domino
                left = isHorizontal ? left + Constants.DOMINO_WIDTH + Constants.DOMINO_MARGIN : left;
                top = isHorizontal ? top : top + Constants.DOMINO_WIDTH + Constants.DOMINO_MARGIN;
            }
            if (!isPlayer) { // if it is not a player we show the number of dominoes
                canvas.drawCircle(width / 2, height / 2, 30, greyFilledPaint);
                canvas.drawText(String.valueOf(dominoes.size()), width / 2, height / 2 - (textBlackPaint.ascent() + textBlackPaint.descent()) / 2, textBlackPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: // touch
                touchedPositionX = (int) event.getX();
                touchedPositionY = (int) event.getY();
                isTouched = true;
                break;
            case MotionEvent.ACTION_UP: // maybe a tap
                if (isTouched) {
                    onTap();
                    isTouched = false;
                }
                break;
        }
        return true;
    }

    /**
     * When the user taps on the screen we try to see if it was on a domino.
     */
    private void onTap() {
        if (isMyTurn) {
            for (Rect rect : items.keySet()) {
                if (rect.contains(touchedPositionX, touchedPositionY)) {
                    Domino tappedDomino = items.get(rect);
                    Log.i(TAG, "onTap: " + tappedDomino.getFirst() + ";" + tappedDomino.getSecond());
                    EventBus.getDefault().post(new DominoTapEvent(tappedDomino));
                    break;
                }
            }
        }
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    public void onResume() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDominoPlayedEvent(DominoPlayedEvent event) {
        dominoes.remove(event.getDomino());
        items.clear();
        invalidate();
    }
}