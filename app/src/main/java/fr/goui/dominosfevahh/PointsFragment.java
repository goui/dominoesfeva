package fr.goui.dominosfevahh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.goui.dominosfevahh.event_bus.PointSetEvent;
import fr.goui.dominosfevahh.model.Model;

public class PointsFragment extends Fragment {

    @BindView(R.id.points_number_picker)
    NumberPicker mNumberPicker;

    @BindView(R.id.points_player_name_text_view)
    TextView mNameTextView;

    private Model mModel = Model.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_points, container, false);
        // binding views
        ButterKnife.bind(this, rootView);
        // getting position
        final int position = getArguments().getInt(PointsActivity.EXTRA_PLAYER_POSITION);
        // setting up the number picker
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(mModel.getPointsByRound());
        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                EventBus.getDefault().post(new PointSetEvent(position, newVal));
            }
        });
        // setting player name
        mNameTextView.setText(mModel.getPlayers().get(position).getName());
        return rootView;
    }
}
