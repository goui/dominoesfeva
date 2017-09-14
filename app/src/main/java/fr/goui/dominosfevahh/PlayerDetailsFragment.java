package fr.goui.dominosfevahh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.goui.dominosfevahh.model.Model;

public class PlayerDetailsFragment extends Fragment {

    private static final int MIN_NUMBER_OF_PLAYERS = 0;

    @BindView(R.id.player_number_text_view)
    TextView mNumberOfPlayersTextView;

    @BindView(R.id.player_recycler_view)
    RecyclerView mPlayersRecyclerView;

    private Model mModel = Model.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player_details, container, false);
        // binding views
        ButterKnife.bind(this, rootView);
        // setting up recycler view
        mPlayersRecyclerView.setHasFixedSize(true);
        mPlayersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPlayersRecyclerView.setAdapter(new PlayerAdapter(getActivity()));
        return rootView;
    }

    @OnClick(R.id.player_plus_button)
    public void onPlusClick() {
        mModel.addPlayer();
        mNumberOfPlayersTextView.setText(String.valueOf(mModel.getPlayers().size()));
    }

    @OnClick(R.id.player_minus_button)
    public void onMinusClick() {
        if (mModel.getPlayers().size() > MIN_NUMBER_OF_PLAYERS) {
            mModel.removePlayer();
            mNumberOfPlayersTextView.setText(String.valueOf(mModel.getPlayers().size()));
        }
    }
}
