package fr.goui.dominosfevahh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import com.rd.PageIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.goui.dominosfevahh.event_bus.PointSetEvent;
import fr.goui.dominosfevahh.model.Model;

public class PointsActivity extends AppCompatActivity {

    public static final String EXTRA_PLAYER_POSITION = "EXTRA_PLAYER_POSITION";

    @BindView(R.id.points_view_pager)
    ViewPager mViewPager;

    @BindView(R.id.points_page_indicator_view)
    PageIndicatorView mPageIndicatorView;

    private Model mModel = Model.getInstance();

    private int[] mPoints = new int[mModel.getPlayers().size()];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        // binding views
        ButterKnife.bind(this);
        // setting up view pager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mPageIndicatorView.setViewPager(mViewPager);
        mPageIndicatorView.setCount(mModel.getPlayers().size());
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
    public void onPointSetEvent(PointSetEvent event) {
        mPoints[event.getPlayerPosition()] = event.getPlayerPoints();
    }

    @OnClick(R.id.points_confirm_button)
    public void onConfirmClick() {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit();
        for (int i = 0; i < mPoints.length; i++) {
            mModel.addPointsToPlayerAtPosition(i, mPoints[i]);
            editor.putInt(getString(R.string.preference_file_pending_game_player_points) + i, mModel.getPlayers().get(i).getNumberOfPoints());
        }
        editor.apply();
        setResult(RESULT_OK);
        finish();
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new PointsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(EXTRA_PLAYER_POSITION, position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mModel.getPlayers().size();
        }
    }

    public static Intent getStartingIntent(Context callingContext) {
        return new Intent(callingContext, PointsActivity.class);
    }
}
