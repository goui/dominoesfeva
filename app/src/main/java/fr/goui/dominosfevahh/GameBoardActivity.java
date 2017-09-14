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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        // binding views
        ButterKnife.bind(this);
        // game board
        mGameBoardManager = new GameBoardManager(mGameBoardView);
        // distributing dominoes
        mPlayerLeftView.setDominoesList(mDominoModel.pickRandom(5));
        mPlayerRightView.setDominoesList(mDominoModel.pickRandom(5));
        mPlayerTopView.setDominoesList(mDominoModel.pickRandom(5));
        mPlayerBottomView.setPlayer(true);
        mPlayerBottomView.setMyTurn(true);
        mPlayerBottomView.setDominoesList(mDominoModel.pickRandom(5));
        mBottomPlayerTurnImageView.setVisibility(View.VISIBLE);
        // stack size
        updateStackSize();
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

    @OnClick(R.id.game_board_stack_image_view)
    public void onStackClick() {
        // if possible add a remaining domino to the bottom player
        if (mDominoModel.getStackSize() > 0) {
            makeCurrentPlayerPick();
            updateStackSize();
        }
    }

    private void makeCurrentPlayerPick() {
        switch (mPlayerPosition) {
            case BOTTOM:
                mPlayerBottomView.addDomino(mDominoModel.pickRandom());
                break;
            case LEFT:
                mPlayerLeftView.addDomino(mDominoModel.pickRandom());
                break;
            case TOP:
                mPlayerTopView.addDomino(mDominoModel.pickRandom());
                break;
            case RIGHT:
                mPlayerRightView.addDomino(mDominoModel.pickRandom());
                break;
        }
    }

    private void updateStackSize() {
        mStackSizeTextView.setText(String.valueOf(mDominoModel.getStackSize()));
        mStackSizeTextView.setTextColor(ContextCompat.getColor(this,
                mDominoModel.getStackSize() > 0 ?
                        android.R.color.holo_green_light :
                        android.R.color.holo_red_light));
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
        mPlayerPosition = mPlayerPosition == RIGHT ? BOTTOM : mPlayerPosition + 1;
        mPlayerBottomView.setMyTurn(mPlayerPosition == BOTTOM);
        mBottomPlayerTurnImageView.setVisibility(mPlayerPosition == BOTTOM ? View.VISIBLE : View.GONE);
        mPlayerLeftView.setMyTurn(mPlayerPosition == LEFT);
        mLeftPlayerTurnImageView.setVisibility(mPlayerPosition == LEFT ? View.VISIBLE : View.GONE);
        mPlayerTopView.setMyTurn(mPlayerPosition == TOP);
        mTopPlayerTurnImageView.setVisibility(mPlayerPosition == TOP ? View.VISIBLE : View.GONE);
        mPlayerRightView.setMyTurn(mPlayerPosition == RIGHT);
        mRightPlayerTurnImageView.setVisibility(mPlayerPosition == RIGHT ? View.VISIBLE : View.GONE);
    }

    public static Intent getStartingIntent(Context callingContext) {
        return new Intent(callingContext, GameBoardActivity.class);
    }
}
