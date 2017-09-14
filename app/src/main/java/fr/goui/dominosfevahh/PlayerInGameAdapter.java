package fr.goui.dominosfevahh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.goui.dominosfevahh.event_bus.PlayersModifiedEvent;
import fr.goui.dominosfevahh.model.Model;
import fr.goui.dominosfevahh.model.Player;

public class PlayerInGameAdapter extends RecyclerView.Adapter<PlayerInGameAdapter.PlayerInGameViewHolder> {

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private Model mModel = Model.getInstance();

    public PlayerInGameAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public PlayerInGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerInGameViewHolder(mLayoutInflater.inflate(R.layout.item_player_in_game, parent, false));
    }

    @Override
    public void onBindViewHolder(PlayerInGameViewHolder holder, int position) {
        Player player = mModel.getPlayers().get(position);
        if (player != null) {
            holder.numberOfRoundsTextView.setText(String.valueOf(player.getNumberOfRounds()));
            holder.nameTextView.setText(player.getName());
            holder.numberOfPointsTextView.setText(String.valueOf(player.getNumberOfPoints()));
        }
    }

    @Override
    public int getItemCount() {
        return mModel.getPlayers().size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayersModifiedEvent(PlayersModifiedEvent event) {
        notifyDataSetChanged();
    }

    static class PlayerInGameViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_player_in_game_number_of_rounds_text_view)
        TextView numberOfRoundsTextView;

        @BindView(R.id.item_player_in_game_name_text_view)
        TextView nameTextView;

        @BindView(R.id.item_player_in_game_number_of_points_text_view)
        TextView numberOfPointsTextView;

        public PlayerInGameViewHolder(View itemView) {
            super(itemView);
            // binding views
            ButterKnife.bind(this, itemView);
        }
    }
}
