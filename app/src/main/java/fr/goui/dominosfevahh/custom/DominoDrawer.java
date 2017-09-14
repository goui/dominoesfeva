package fr.goui.dominosfevahh.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import fr.goui.dominosfevahh.Constants;
import fr.goui.dominosfevahh.model.Domino;

/**
 * Regroups all the methods to draw dominoes and the numbers inside them.
 */
public class DominoDrawer {

    /**
     * Draws a domino at x;y position.
     *
     * @param domino      the domino to draw
     * @param showNumbers if we want to show the numbers
     * @param canvas      the canvas to draw in
     * @param paint       the painter
     * @return the domino rectangle
     */
    public static Rect drawDomino(Domino domino, boolean showNumbers, Canvas canvas, Paint paint) {
        // drawing the shape
        boolean isVertical = domino.isVertical();
        int left = domino.getX();
        int top = domino.getY();
        int right = isVertical ? left + Constants.DOMINO_WIDTH : left + Constants.DOMINO_HEIGHT;
        int bottom = isVertical ? top + Constants.DOMINO_HEIGHT : top + Constants.DOMINO_WIDTH;
        canvas.drawRect(left, top, right, bottom, paint);
        if (showNumbers) {
            // drawing the mid line
            int startX = isVertical ? left + Constants.DOMINO_MID_LINE_PADDING : left + Constants.DOMINO_VERTICAL_OFFSET;
            int startY = isVertical ? top + Constants.DOMINO_VERTICAL_OFFSET : top + Constants.DOMINO_MID_LINE_PADDING;
            int stopX = isVertical ? right - Constants.DOMINO_MID_LINE_PADDING : left + Constants.DOMINO_VERTICAL_OFFSET;
            int stopY = isVertical ? top + Constants.DOMINO_VERTICAL_OFFSET : bottom - Constants.DOMINO_MID_LINE_PADDING;
            canvas.drawLine(startX, startY, stopX, stopY, paint);
            // drawing the numbers
            drawNumber(domino.getFirst(), !domino.isInverted(), left, top, isVertical, canvas, paint);
            drawNumber(domino.getSecond(), domino.isInverted(), left, top, isVertical, canvas, paint);
        }
        return new Rect(
                isVertical ? left - Constants.DOMINO_MARGIN / 2 : left,
                isVertical ? top : top - Constants.DOMINO_MARGIN / 2,
                isVertical ? right + Constants.DOMINO_MARGIN / 2 : right,
                isVertical ? bottom : bottom + Constants.DOMINO_MARGIN / 2
        );
    }

    /**
     * Draws the right number for the specified value.
     *
     * @param number     the number to draw
     * @param isFirst    if this is the first domino's number
     * @param x          the origin x
     * @param y          the origin y
     * @param isVertical the domino orientation
     * @param canvas     the canvas to draw in
     * @param paint      the painter
     */
    private static void drawNumber(int number, boolean isFirst, int x, int y, boolean isVertical, Canvas canvas, Paint paint) {
        switch (number) {
            case 1:
                drawOne(canvas, x, y, isFirst, isVertical, paint);
                break;
            case 2:
                drawTwo(canvas, x, y, isFirst, isVertical, paint);
                break;
            case 3:
                drawThree(canvas, x, y, isFirst, isVertical, paint);
                break;
            case 4:
                drawFour(canvas, x, y, isFirst, isVertical, paint);
                break;
            case 5:
                drawFive(canvas, x, y, isFirst, isVertical, paint);
                break;
            case 6:
                drawSix(canvas, x, y, isFirst, isVertical, paint);
                break;
            default:
                break;
        }
    }

    /**
     * Draws a one from the left;top origin of the domino.
     *
     * @param canvas     the canvas to draw in
     * @param left       the origin x
     * @param top        the origin y
     * @param isFirst    if this is the first domino's number
     * @param isVertical he view orientation (dominoes are inverted)
     * @param paint      the painter
     */
    private static void drawOne(Canvas canvas, int left, int top, boolean isFirst, boolean isVertical, Paint paint) {
        // one = mid mid
        // h mid mid = x+20;y+18
        // v mid mid = x+18;y+20
        int cx = isVertical ?
                left + 2 * Constants.DOMINO_X_OFFSET :
                left + 2 * Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET);
        int cy = isVertical ?
                top + 2 * Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET) :
                top + 2 * Constants.DOMINO_X_OFFSET;
        canvas.drawCircle(cx, cy, 3, paint);
    }

    /**
     * Draws a two from the left;top origin of the domino.
     *
     * @param canvas     the canvas to draw in
     * @param left       the origin x
     * @param top        the origin y
     * @param isFirst    if this is the first domino's number
     * @param isVertical he view orientation (dominoes are inverted)
     * @param paint      the painter
     */
    private static void drawTwo(Canvas canvas, int left, int top, boolean isFirst, boolean isVertical, Paint paint) {
        // two = top right + bottom left
        // h top right = x+30;y+9
        // v top right = x+9;y+10
        int cx = isVertical ?
                left + 3 * Constants.DOMINO_X_OFFSET :
                left + Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET);
        int cy = isVertical ?
                top + Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET) :
                top + Constants.DOMINO_X_OFFSET;
        canvas.drawCircle(cx, cy, 3, paint);
        // h bottom left = x+10;y+27
        // v bottom left = x+30;y+27
        cx = isVertical ?
                left + Constants.DOMINO_X_OFFSET :
                left + 3 * Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET);
        cy = isVertical ?
                top + 3 * Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET) :
                top + 3 * Constants.DOMINO_X_OFFSET;
        canvas.drawCircle(cx, cy, 3, paint);
    }

    /**
     * Draws a three from the left;top origin of the domino.
     *
     * @param canvas     the canvas to draw in
     * @param left       the origin x
     * @param top        the origin y
     * @param isFirst    if this is the first domino's number
     * @param isVertical he view orientation (dominoes are inverted)
     * @param paint      the painter
     */
    private static void drawThree(Canvas canvas, int left, int top, boolean isFirst, boolean isVertical, Paint paint) {
        // three = top right + mid mid + bottom left
        // three = one + two
        drawOne(canvas, left, top, isFirst, isVertical, paint);
        drawTwo(canvas, left, top, isFirst, isVertical, paint);
    }

    /**
     * Draws a four from the left;top origin of the domino.
     *
     * @param canvas     the canvas to draw in
     * @param left       the origin x
     * @param top        the origin y
     * @param isFirst    if this is the first domino's number
     * @param isVertical he view orientation (dominoes are inverted)
     * @param paint      the painter
     */
    private static void drawFour(Canvas canvas, int left, int top, boolean isFirst, boolean isVertical, Paint paint) {
        // four = top left + top right + bottom left + bottom right
        // four = two + top left + bottom right
        drawTwo(canvas, left, top, isFirst, isVertical, paint);
        // h top left = x+10;y+9
        // v top left = x+9;y+10
        int cx = isVertical ?
                left + Constants.DOMINO_X_OFFSET :
                left + Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET);
        int cy = isVertical ?
                top + Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET) :
                top + 3 * Constants.DOMINO_X_OFFSET;
        canvas.drawCircle(cx, cy, 3, paint);
        // h bottom right = x+30;y+27
        // v bottom right = x+27;y+30
        cx = isVertical ?
                left + 3 * Constants.DOMINO_X_OFFSET :
                left + 3 * Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET);
        cy = isVertical ?
                top + 3 * Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET) :
                top + Constants.DOMINO_X_OFFSET;
        canvas.drawCircle(cx, cy, 3, paint);
    }

    /**
     * Draws a five from the left;top origin of the domino.
     *
     * @param canvas     the canvas to draw in
     * @param left       the origin x
     * @param top        the origin y
     * @param isFirst    if this is the first domino's number
     * @param isVertical he view orientation (dominoes are inverted)
     * @param paint      the painter
     */
    private static void drawFive(Canvas canvas, int left, int top, boolean isFirst, boolean isVertical, Paint paint) {
        // five = top left + top right + mid mid + bottom left + bottom right
        // five = one + four
        drawOne(canvas, left, top, isFirst, isVertical, paint);
        drawFour(canvas, left, top, isFirst, isVertical, paint);
    }

    /**
     * Draws a six from the left;top origin of the domino.
     *
     * @param canvas     the canvas to draw in
     * @param left       the origin x
     * @param top        the origin y
     * @param isFirst    if this is the first domino's number
     * @param isVertical he view orientation (dominoes are inverted)
     * @param paint      the painter
     */
    private static void drawSix(Canvas canvas, int left, int top, boolean isFirst, boolean isVertical, Paint paint) {
        // six = top left + top right + mid left + mid right + bottom left + bottom right
        // six = four + mid left + mid right
        drawFour(canvas, left, top, isFirst, isVertical, paint);
        // h mid left = x+10;y+18
        // v mid left = x+18;y+10
        int cx = isVertical ?
                left + Constants.DOMINO_X_OFFSET :
                left + 2 * Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET);
        int cy = isVertical ?
                top + 2 * Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET) :
                top + Constants.DOMINO_X_OFFSET;
        canvas.drawCircle(cx, cy, 3, paint);
        // h mid right = x+30;y+18
        // v mid right = x+18;y+30
        cx = isVertical ?
                left + 3 * Constants.DOMINO_X_OFFSET :
                left + 2 * Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET);
        cy = isVertical ?
                top + 2 * Constants.DOMINO_Y_OFFSET + (isFirst ? 0 : Constants.DOMINO_VERTICAL_OFFSET) :
                top + 3 * Constants.DOMINO_X_OFFSET;
        canvas.drawCircle(cx, cy, 3, paint);
    }
}
