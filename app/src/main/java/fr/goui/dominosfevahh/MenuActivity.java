package fr.goui.dominosfevahh;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends AppCompatActivity {

    @BindView(R.id.menu_continue_button)
    Button mContinueButton;

    private boolean mIsStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // binding views
        ButterKnife.bind(this);
        // if there is a unfinished pending game, showing continue button
        mContinueButton.setVisibility(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)
                .getBoolean(getString(R.string.preference_file_pending_game), false) ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.menu_continue_button)
    public void onContinueClick() {
        startActivity(GameActivity.getStartingIntent(this, true));
        mIsStarted = true;
    }

    @OnClick(R.id.menu_new_button)
    public void onNewClick() {
        startActivity(NewGameActivity.getStartingIntent(this));
        mIsStarted = true;
    }

    @OnClick(R.id.menu_stats_button)
    public void onStatsClick() {
        startActivity(GameBoardActivity.getStartingIntent(this));
    }

    @OnClick(R.id.menu_exit_button)
    public void onExitClick() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.Exit_qm))
                .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.Cancel), null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsStarted) {
            mContinueButton.setVisibility(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)
                    .getBoolean(getString(R.string.preference_file_pending_game), false) ? View.VISIBLE : View.GONE);
        }
    }
}
