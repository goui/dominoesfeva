package fr.goui.dominosfevahh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.goui.dominosfevahh.event_bus.PlayerNumberChangedEvent;
import fr.goui.dominosfevahh.event_bus.ValidationEvent;
import fr.goui.dominosfevahh.model.Model;
import fr.goui.dominosfevahh.model.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private LayoutInflater mLayoutInflater;

    private Model mModel = Model.getInstance();

    private boolean[] mIsValid;

    public PlayerAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mIsValid = new boolean[mModel.getPlayers().size()];
        updateValidation();
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerViewHolder(mLayoutInflater.inflate(R.layout.item_player, parent, false));
    }

    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, int position) {
        final Player player = mModel.getPlayers().get(position);
        if (player != null) {
            holder.nameEditText.setText(player.getName());
            holder.nameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // setting player name
                    if (!TextUtils.isEmpty(s)) {
                        player.setName(s.toString());
                    }
                    // validating the screen
                    mIsValid[holder.getAdapterPosition()] = !TextUtils.isEmpty(s);
                    updateValidation();
                }
            });
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
    public void onPlayerNumberChangedEvent(PlayerNumberChangedEvent event) {
        mIsValid = new boolean[mModel.getPlayers().size()];
        updateValidation();
        notifyDataSetChanged();
    }

    private void updateValidation() {
        boolean isValid = mIsValid.length > 0 ? mIsValid[0] : false;
        for (int i = 1; i < mIsValid.length; i++) {
            isValid = isValid & mIsValid[i];
        }
        EventBus.getDefault().post(new ValidationEvent(isValid));
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_player_edit_text)
        EditText nameEditText;

        public PlayerViewHolder(View itemView) {
            super(itemView);
            // binding views
            ButterKnife.bind(this, itemView);
        }
    }
}
