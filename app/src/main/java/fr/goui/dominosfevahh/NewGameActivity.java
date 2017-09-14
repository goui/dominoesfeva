package fr.goui.dominosfevahh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.goui.dominosfevahh.event_bus.ValidationEvent;
import fr.goui.dominosfevahh.model.Model;
import fr.goui.dominosfevahh.model.Player;

public class NewGameActivity extends AppCompatActivity {

    @BindView(R.id.new_game_back_button)
    Button mBackButton;

    @BindView(R.id.new_game_next_button)
    Button mNextButton;

    @BindView(R.id.new_game_start_button)
    Button mStartButton;

    private FragmentManager mFragmentManager;

    private CreationStep mCreationStep = CreationStep.GAME_DETAILS;

    private Model mModel = Model.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        // binding views
        ButterKnife.bind(this);
        // getting the fragment manager
        mFragmentManager = getSupportFragmentManager();
        // putting first fragment
        mFragmentManager.beginTransaction()
                .replace(R.id.new_game_content, new GameDetailsFragment())
                .commit();
    }

    @OnClick(R.id.new_game_next_button)
    public void onNextClick() {
        // updating step
        mCreationStep = CreationStep.PLAYER_DETAILS;
        // putting player details fragment
        mFragmentManager.beginTransaction()
                .replace(R.id.new_game_content, new PlayerDetailsFragment())
                .addToBackStack(null)
                .commit();
        // hiding next button
        mNextButton.setVisibility(View.INVISIBLE);
        // showing start button
        mStartButton.setVisibility(View.VISIBLE);
        // showing back button
        mBackButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.new_game_start_button)
    public void onStartClick() {
        // saving the fact that there is a pending game
        saveGameInfo();
        // going to the game activity
        startActivity(GameActivity.getStartingIntent(this, false));
        // we don't want to modify the pending game anymore
        finish();
    }

    private void saveGameInfo() {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()
                .putInt(getString(R.string.preference_file_pending_game_number_of_rounds), mModel.getNumberOfRounds())
                .putInt(getString(R.string.preference_file_pending_game_points_by_round), mModel.getPointsByRound())
                .putInt(getString(R.string.preference_file_pending_game_number_of_players), mModel.getPlayers().size())
                .putBoolean(getString(R.string.preference_file_pending_game), true);
        for (int i = 0; i < mModel.getPlayers().size(); i++) {
            editor.putString(getString(R.string.preference_file_pending_game_player_name) + i, mModel.getPlayers().get(i).getName());
        }
        editor.apply();
    }

    @OnClick(R.id.new_game_back_button)
    public void onBackClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mCreationStep == CreationStep.PLAYER_DETAILS) {
            // updating step
            mCreationStep = CreationStep.GAME_DETAILS;
            // showing next button
            mNextButton.setVisibility(View.VISIBLE);
            // hiding start button
            mStartButton.setVisibility(View.GONE);
            // hiding back button
            mBackButton.setVisibility(View.GONE);
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onValidationEvent(ValidationEvent event) {
        if (mCreationStep == CreationStep.GAME_DETAILS) {
            mNextButton.setEnabled(event.isValid());
        } else {
            mStartButton.setEnabled(event.isValid());
        }
    }

    public static Intent getStartingIntent(Context callingContext) {
        return new Intent(callingContext, NewGameActivity.class);
    }

    private enum CreationStep {
        GAME_DETAILS, PLAYER_DETAILS;
    }
}
