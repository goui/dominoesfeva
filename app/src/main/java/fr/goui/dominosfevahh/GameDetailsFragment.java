package fr.goui.dominosfevahh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.goui.dominosfevahh.event_bus.ValidationEvent;
import fr.goui.dominosfevahh.model.Model;

public class GameDetailsFragment extends Fragment {

    @BindView(R.id.game_details_number_of_rounds_edit_text)
    EditText mNumberOfRoundsEditText;

    @BindView(R.id.game_details_points_by_round_edit_text)
    EditText mPointsByRoundEditText;

    private boolean mIsNumberOfRoundsValid;

    private boolean mIsPointsByRoundValid;

    private Model mModel = Model.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_details, container, false);
        // binding views
        ButterKnife.bind(this, rootView);
        // nb of rounds text watcher
        mNumberOfRoundsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mModel.setNumberOfRounds(!TextUtils.isEmpty(s) ? Integer.valueOf(s.toString()) : 0);
                mIsNumberOfRoundsValid = !TextUtils.isEmpty(s);
                updateValidation();
            }
        });
        // points by round text watcher
        mPointsByRoundEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mModel.setPointsByRound(!TextUtils.isEmpty(s) ? Integer.valueOf(s.toString()) : 0);
                mIsPointsByRoundValid = !TextUtils.isEmpty(s);
                updateValidation();
            }
        });
        return rootView;
    }

    private void updateValidation() {
        EventBus.getDefault().post(new ValidationEvent(mIsNumberOfRoundsValid && mIsPointsByRoundValid));
    }
}
