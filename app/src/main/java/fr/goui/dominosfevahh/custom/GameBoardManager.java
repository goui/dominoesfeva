package fr.goui.dominosfevahh.custom;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fr.goui.dominosfevahh.Constants;
import fr.goui.dominosfevahh.event_bus.BoardSizeChangedEvent;
import fr.goui.dominosfevahh.event_bus.DominoPlayedEvent;
import fr.goui.dominosfevahh.event_bus.DominoTapEvent;
import fr.goui.dominosfevahh.model.Domino;

public class GameBoardManager {
    private static final String TAG = GameBoardManager.class.getSimpleName();

    private GameBoardView gameBoardView;

    private int leftValue = -1;

    private int rightValue = -1;

    private int currentLeftX;

    private int currentLeftY;

    private int currentRightX;

    private int currentRightY;

    private int leftCounter;

    private int rightCounter;

    private boolean isFirstDomino = true;

    public GameBoardManager(GameBoardView gameBoardView) {
        this.gameBoardView = gameBoardView;
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    public void onResume() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBoardSizeChangedEvent(BoardSizeChangedEvent event) {
        currentLeftX = currentRightX = event.getWidth() / 2 - Constants.DOMINO_HEIGHT / 2;
        currentLeftY = currentRightY = event.getHeight() / 2 - Constants.DOMINO_WIDTH / 2;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDominoTapEvent(DominoTapEvent event) {
        if (isValid(event.getDomino())) {
            moveDomino(event.getDomino());
        }
    }

    /**
     * Checks if the domino can be placed on the board.
     *
     * @param domino the domino
     * @return true if we can place it, false otherwise
     */
    private boolean isValid(Domino domino) {
        return domino.getFirst() == leftValue
                || domino.getSecond() == leftValue
                || domino.getFirst() == rightValue
                || domino.getSecond() == rightValue
                || leftValue == -1;
    }

    /**
     * Looks for the position the domino will be placed.
     *
     * @param domino the domino
     */
    private void moveDomino(Domino domino) {
        if (domino.getFirst() == leftValue) {
            Log.i(TAG, "domino.getFirst() == leftValue: ");
            placeDominoLeft(domino, true);
            leftValue = domino.getSecond();
        } else if (domino.getSecond() == leftValue) {
            Log.i(TAG, "domino.getSecond() == leftValue: ");
            placeDominoLeft(domino, false);
            leftValue = domino.getFirst();
        } else if (domino.getFirst() == rightValue) {
            Log.i(TAG, "domino.getFirst() == rightValue: ");
            placeDominoRight(domino, true);
            rightValue = domino.getSecond();
        } else if (domino.getSecond() == rightValue) {
            Log.i(TAG, "domino.getSecond() == rightValue ou first: ");
            placeDominoRight(domino, false);
            rightValue = domino.getFirst();
        } else if (isFirstDomino) {
            Log.i(TAG, "first: ");
            placeDominoRight(domino, false);
            rightValue = domino.getFirst();
            leftValue = domino.getSecond();
        }
        Log.i(TAG, "left: " + leftValue + ", right: " + rightValue);
        gameBoardView.addDomino(domino);
        EventBus.getDefault().post(new DominoPlayedEvent(domino));
    }

    /**
     * Moves the domino to the left of the current chain on the board.
     *
     * @param domino the domino
     * @param firstChosen if the first value of the domino has been chosen
     */
    private void placeDominoLeft(Domino domino, boolean firstChosen) {
        Log.i(TAG, "placeDominoLeft: " + currentLeftX + ";" + currentLeftY + " - " + leftCounter);
        // setting coordinates
        domino.setX(currentLeftX);
        domino.setY(currentLeftY);
        domino.setVertical(leftCounter == 6 || leftCounter == 18);
        // next coordinates
        if (leftCounter < 5 || leftCounter > 18) {
            domino.setInverted(firstChosen);
            currentLeftX = currentLeftX - Constants.DOMINO_HEIGHT - 3;
        } else if (leftCounter == 5) {
            domino.setInverted(firstChosen);
            currentLeftY += Constants.DOMINO_WIDTH + 3;
        } else if (leftCounter == 6) {
            domino.setInverted(!firstChosen);
            currentLeftY += Constants.DOMINO_HEIGHT + 3;
        } else if (leftCounter == 17) {
            domino.setInverted(firstChosen);
            currentLeftX += Constants.DOMINO_HEIGHT / 2;
            currentLeftY += Constants.DOMINO_WIDTH + 3;
        } else if (leftCounter > 6 && leftCounter < 17) {
            domino.setInverted(!firstChosen);
            currentLeftX += Constants.DOMINO_HEIGHT + 3;
        } else if (leftCounter == 18) {
            domino.setInverted(firstChosen);
            currentLeftX -= Constants.DOMINO_HEIGHT / 2;
            currentLeftY += Constants.DOMINO_HEIGHT + 3;
        }
        leftCounter++;
    }

    /**
     * Moves the domino to the right of the current chain on the board.
     *
     * @param domino the domino
     * @param firstChosen if the first value of the domino has been chosen
     */
    private void placeDominoRight(Domino domino, boolean firstChosen) {
        Log.i(TAG, "placeDominoRight: " + currentLeftX + ";" + currentLeftY + " - " + rightCounter);
        // setting coordinates
        domino.setX(currentRightX);
        domino.setY(currentRightY);
        domino.setVertical(rightCounter == 6 || rightCounter == 18);
        // next coordinates
        if (rightCounter < 5 || rightCounter > 18) {
            domino.setInverted(!firstChosen);
            currentRightX += Constants.DOMINO_HEIGHT + 3;
        } else if (rightCounter == 5) {
            domino.setInverted(!firstChosen);
            currentRightX = currentRightX + Constants.DOMINO_HEIGHT / 2;
            currentRightY = currentRightY - Constants.DOMINO_HEIGHT - 3;
        } else if (rightCounter == 6) {
            domino.setInverted(firstChosen);
            currentRightX = currentRightX - Constants.DOMINO_HEIGHT / 2;
            currentRightY = currentRightY - Constants.DOMINO_WIDTH - 3;
        } else if (rightCounter == 17) {
            domino.setInverted(!firstChosen);
            currentRightY = currentRightY - Constants.DOMINO_HEIGHT - 3;
        } else if (rightCounter > 6 && rightCounter < 17) {
            domino.setInverted(firstChosen);
            currentRightX = currentRightX - Constants.DOMINO_HEIGHT - 3;
        } else if (rightCounter == 18) {
            domino.setInverted(!firstChosen);
            currentRightY = currentRightY - Constants.DOMINO_WIDTH - 3;
        }
        rightCounter++;
        if (isFirstDomino) {
            currentLeftX = currentLeftX - Constants.DOMINO_HEIGHT - 3;
            isFirstDomino = false;
            leftCounter++;
        }
    }
}
