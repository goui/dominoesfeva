package fr.goui.dominosfevahh;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.goui.dominosfevahh.custom.GameBoardManager;
import fr.goui.dominosfevahh.custom.GameBoardView;
import fr.goui.dominosfevahh.custom.PlayerView;
import fr.goui.dominosfevahh.event_bus.DominoPlayedEvent;
import fr.goui.dominosfevahh.model.Domino;
import fr.goui.dominosfevahh.model.DominoModel;

public class GameBoardActivity extends AppCompatActivity {
    private static final String TAG = GameBoardActivity.class.getSimpleName();

    private static final int BOTTOM = 0;
    private static final int LEFT = 1;
    private static final int TOP = 2;
    private static final int RIGHT = 3;

    @BindView(R.id.game_board_view)
    GameBoardView mGameBoardView;

    @BindView(R.id.game_board_player_left_view)
    PlayerView mPlayerLeftView;

    @BindView(R.id.game_board_player_right_view)
    PlayerView mPlayerRightView;

    @BindView(R.id.game_board_player_top_view)
    PlayerView mPlayerTopView;

    @BindView(R.id.game_board_player_bottom_view)
    PlayerView mPlayerBottomView;

    @BindView(R.id.game_board_stack_size_text_view)
    TextView mStackSizeTextView;

    @BindView(R.id.game_board_player_bottom_turn_image_view)
    ImageView mBottomPlayerTurnImageView;

    @BindView(R.id.game_board_player_left_turn_image_view)
    ImageView mLeftPlayerTurnImageView;

    @BindView(R.id.game_board_player_top_turn_image_view)
    ImageView mTopPlayerTurnImageView;

    @BindView(R.id.game_board_player_right_turn_image_view)
    ImageView mRightPlayerTurnImageView;

    private DominoModel mDominoModel = new DominoModel();

    private GameBoardManager mGameBoardManager;

    private int mPlayerPosition;

    private PlayerView[] mPlayerViews = new PlayerView[4];

    private PlayerView mCurrentPlayerView;

    private ImageView[] mPlayersTurnImageViews = new ImageView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        // binding views
        ButterKnife.bind(this);
        // game board
        mGameBoardManager = new GameBoardManager(mGameBoardView);
        // players
        mPlayerViews[BOTTOM] = mPlayerBottomView;
        mPlayerViews[LEFT] = mPlayerLeftView;
        mPlayerViews[TOP] = mPlayerTopView;
        mPlayerViews[RIGHT] = mPlayerRightView;
        mPlayersTurnImageViews[BOTTOM] = mBottomPlayerTurnImageView;
        mPlayersTurnImageViews[LEFT] = mLeftPlayerTurnImageView;
        mPlayersTurnImageViews[TOP] = mTopPlayerTurnImageView;
        mPlayersTurnImageViews[RIGHT] = mRightPlayerTurnImageView;
        // distributing dominoes
        mPlayerLeftView.setDominoesList(mDominoModel.pickRandom(5));
        mPlayerRightView.setDominoesList(mDominoModel.pickRandom(5));
        mPlayerTopView.setDominoesList(mDominoModel.pickRandom(5));
        mPlayerBottomView.setPlayer(true);
        mPlayerBottomView.setDominoesList(mDominoModel.pickRandom(5));
        mBottomPlayerTurnImageView.setVisibility(View.VISIBLE);
        // choose first player
        chooseFirstPlayer(6);
        // stack size
        updateStackText();
        updateStackColor();
    }

    @OnClick(R.id.game_board_info_image_view)
    public void onInfoClick() {
        // TODO show players and stats and characters
    }

    @OnClick(R.id.game_board_settings_image_view)
    public void onSettingsClick() {
        // TODO show settings
    }

    @OnClick(R.id.game_board_uno_image_view)
    public void onUnoClick() {
        // TODO activate uno to avoid counter uno
    }

    /**
     * Chooses the first player to play.
     *
     * @param doubleNumber the number of the highest double not in the stack.
     */
    private void chooseFirstPlayer(int doubleNumber) {
        Log.i(TAG, "chooseFirstPlayer with: " + doubleNumber);
        if (mDominoModel.isDoubleInStack(doubleNumber)) {
            Log.i(TAG, "this double is in the stack");
            chooseFirstPlayer(--doubleNumber);
        } else {
            Log.i(TAG, "this double is NOT in the stack");
            for (int i = 0; i < 4; i++) {
                if (mPlayerViews[i].hasDouble(doubleNumber)) {
                    Log.i(TAG, "player " + i + " is the first player");
                    mCurrentPlayerView = mPlayerViews[i];
                    mCurrentPlayerView.setMyTurn(true);
                    mPlayerPosition = i;
                    mPlayersTurnImageViews[i].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @OnClick(R.id.game_board_stack_image_view)
    public void onStackClick() {
        // if possible add a remaining domino to the current player
        if (isStackAvailableForCurrentPlayer() && mDominoModel.getStackSize() > 0) {
            mCurrentPlayerView.addDomino(mDominoModel.pickRandom());
            updateStackText();
            updateStackColor();
        }
        // if there is no more domino in the stack just pass
        else if (isStackAvailableForCurrentPlayer()) {
            onDominoPlayedEvent(null);
            updateStackColor();
        }
    }

    /**
     * Updates the char written on top of the stack.
     */
    private void updateStackText() {
        mStackSizeTextView.setText(mDominoModel.getStackSize() > 0 ?
                String.valueOf(mDominoModel.getStackSize()) :
                "P");
    }

    /**
     * Puts the green color on the stack if it can be clicked, the red one if not.
     */
    private void updateStackColor() {
        mStackSizeTextView.setTextColor(ContextCompat.getColor(this, isStackAvailableForCurrentPlayer() ?
                android.R.color.holo_green_light :
                android.R.color.holo_red_light));
    }

    /**
     * If the stack can be clicked by current player.
     *
     * @return true if it is, false otherwise
     */
    private boolean isStackAvailableForCurrentPlayer() {
        boolean ret = true;
        for (Domino domino : mCurrentPlayerView.getDominoes()) {
            if (domino.getFirst() == mGameBoardManager.getLeftValue()
                    || domino.getFirst() == mGameBoardManager.getRightValue()
                    || domino.getSecond() == mGameBoardManager.getLeftValue()
                    || domino.getSecond() == mGameBoardManager.getRightValue()
                    || mGameBoardManager.getLeftValue() == -1) {
                ret = false;
            }
        }
        return ret;
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        mGameBoardManager.onPause();
        mPlayerLeftView.onPause();
        mPlayerRightView.onPause();
        mPlayerTopView.onPause();
        mPlayerBottomView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGameBoardManager.onResume();
        mPlayerLeftView.onResume();
        mPlayerRightView.onResume();
        mPlayerTopView.onResume();
        mPlayerBottomView.onResume();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDominoPlayedEvent(DominoPlayedEvent event) {
        // changing current player
        mPlayerPosition = mPlayerPosition == RIGHT ? BOTTOM : mPlayerPosition + 1;
        mPlayerBottomView.setMyTurn(mPlayerPosition == BOTTOM);
        mBottomPlayerTurnImageView.setVisibility(mPlayerPosition == BOTTOM ? View.VISIBLE : View.GONE);
        mPlayerLeftView.setMyTurn(mPlayerPosition == LEFT);
        mLeftPlayerTurnImageView.setVisibility(mPlayerPosition == LEFT ? View.VISIBLE : View.GONE);
        mPlayerTopView.setMyTurn(mPlayerPosition == TOP);
        mTopPlayerTurnImageView.setVisibility(mPlayerPosition == TOP ? View.VISIBLE : View.GONE);
        mPlayerRightView.setMyTurn(mPlayerPosition == RIGHT);
        mRightPlayerTurnImageView.setVisibility(mPlayerPosition == RIGHT ? View.VISIBLE : View.GONE);
        switch (mPlayerPosition) {
            case BOTTOM:
                mCurrentPlayerView = mPlayerBottomView;
                break;
            case LEFT:
                mCurrentPlayerView = mPlayerLeftView;
                break;
            case TOP:
                mCurrentPlayerView = mPlayerTopView;
                break;
            case RIGHT:
                mCurrentPlayerView = mPlayerRightView;
                break;
        }
        // updating the stack
        updateStackColor();
    }

    public static Intent getStartingIntent(Context callingContext) {
        return new Intent(callingContext, GameBoardActivity.class);
    }

    // TODO complete rules
    // force player to play the double in first turn
    // end game when player has no more domino
    // end game when nobody can play anymore
    // allow to choose where to place domino
}
