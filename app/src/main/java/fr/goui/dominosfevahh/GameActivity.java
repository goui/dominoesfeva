package fr.goui.dominosfevahh;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.goui.dominosfevahh.event_bus.PlayersModifiedEvent;
import fr.goui.dominosfevahh.model.Model;
import fr.goui.dominosfevahh.model.Player;

public class GameActivity extends AppCompatActivity {

    private static final String EXTRA_CONTINUED_GAME = "EXTRA_CONTINUED_GAME";
    private static final int REQUEST_CODE = 123;

    @BindView(R.id.game_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.game_current_leader_text_view)
    TextView mCurrentLeaderTextView;

    private Model mModel = Model.getInstance();

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // binding views
        ButterKnife.bind(this);
        // getting shared preferences
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        // putting pending game info in model if continued game
        if (getIntent().getBooleanExtra(EXTRA_CONTINUED_GAME, false)) {
            mModel.setNumberOfRounds(mSharedPreferences.getInt(getString(R.string.preference_file_pending_game_number_of_rounds), 3));
            mModel.setPointsByRound(mSharedPreferences.getInt(getString(R.string.preference_file_pending_game_points_by_round), 100));
            mModel.getPlayers().clear();
            for (int i = 0; i < mSharedPreferences.getInt(getString(R.string.preference_file_pending_game_number_of_players), 0); i++) {
                mModel.addPlayer();
                Player player = mModel.getPlayers().get(i);
                player.setNumberOfPoints(mSharedPreferences.getInt(getString(R.string.preference_file_pending_game_player_points) + i, 0));
                player.setNumberOfRounds(mSharedPreferences.getInt(getString(R.string.preference_file_pending_game_player_rounds) + i, 0));
                player.setName(mSharedPreferences.getString(getString(R.string.preference_file_pending_game_player_name) + i, "Player"));
            }
            updateCurrentLeader();
            EventBus.getDefault().post(new PlayersModifiedEvent());
        }
        // setting up the recycler view
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new PlayerInGameAdapter(this));
    }

    @OnClick(R.id.game_end_round_button)
    public void onEndRoundClick() {
        startActivityForResult(PointsActivity.getStartingIntent(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            updateCurrentLeader();
            EventBus.getDefault().post(new PlayersModifiedEvent());
            if (mModel.isRoundFinished()) {
                Player leader = mModel.getCurrentRoundLeader();
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.Round_won_by_, leader.getName()))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishRound();
                                EventBus.getDefault().post(new PlayersModifiedEvent());
                            }
                        })
                        .show();
            }
        }
    }

    private void finishRound() {
        Player leader = mModel.getCurrentRoundLeader();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < mModel.getPlayers().size(); i++) {
            Player player = mModel.getPlayers().get(i);
            if (player.equals(leader)) {
                leader.setNumberOfRounds(leader.getNumberOfRounds() + 1);
                editor.putInt(getString(R.string.preference_file_pending_game_player_rounds) + i, leader.getNumberOfRounds());
            }
            player.setNumberOfPoints(0);
            editor.putInt(getString(R.string.preference_file_pending_game_player_points) + i, 0);
        }
        editor.apply();
        if (mModel.isGameFinished()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.Game_won_by_, mModel.getWinner().getName()))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishGame();
                            EventBus.getDefault().post(new PlayersModifiedEvent());
                        }
                    })
                    .show();
        }
    }

    private void finishGame() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < mModel.getPlayers().size(); i++) {
            Player player = mModel.getPlayers().get(i);
            player.setNumberOfPoints(0);
            player.setNumberOfRounds(0);
            editor.putInt(getString(R.string.preference_file_pending_game_player_rounds) + i, 0);
            editor.putInt(getString(R.string.preference_file_pending_game_player_points) + i, 0);
        }
        editor.apply();
        finish();
    }

    private void updateCurrentLeader() {
        mCurrentLeaderTextView.setText(mModel.getCurrentRoundLeader().getName());
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.Quit_qm))
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(getString(R.string.Cancel), null)
                .show();
    }

    public static Intent getStartingIntent(Context callingContext, boolean continuedGame) {
        Intent intent = new Intent(callingContext, GameActivity.class);
        intent.putExtra(EXTRA_CONTINUED_GAME, continuedGame);
        return intent;
    }
}
