package cd.myplayer.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.ArrayList;

import cd.myplayer.R;
import cd.myplayer.presenter.VideoNetPagerPresenter;
import cd.myplayer.presenter.VideoPagerPresenter;
import cd.myplayer.utils.LogUtil;
import cd.myplayer.utils.StatusBarUtil;
import cd.myplayer.view.MainView;
import cd.myplayer.view.MyFragment;
import cd.myplayer.view.VideoPager;

public class MainActivity extends FragmentActivity{

    private ArrayList<Fragment> myFragments;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.d("this.hashCode()="+this.hashCode());
        super.onCreate(savedInstanceState);

        this.context = this;
        findViews();
    }


    private void findViews() {

        //组件Activity--主View--组件Fragment--4个子View
        //组件Activity--主View 分开
        //组件Fragment--4个子View 分开

        myFragments = new ArrayList<>();

        VideoPager videoPager = (VideoPager) View.inflate(context, R.layout.video_pager, null);
        videoPager.setVideoPagerPresenter(new VideoPagerPresenter());

        VideoPager videoNetPager = (VideoPager) View.inflate(context, R.layout.video_pager, null);
        videoNetPager.setVideoPagerPresenter(new VideoNetPagerPresenter());

        myFragments.add(MyFragment.newInstance(videoPager));
        myFragments.add(MyFragment.newInstance(videoPager));
        myFragments.add(MyFragment.newInstance(videoPager));
        myFragments.add(MyFragment.newInstance(videoNetPager));


        MainView mainView = (MainView) View.inflate(this, R.layout.activity_main, null);
        mainView.setMainViewPresenter(new MainView.IMainViewPresenter() {
            @Override
            public ArrayList<Fragment> getFragments() {
                return myFragments;
            }
        });


        setContentView(mainView);
//        setContentView(R.layout.activity_main);
//        mainView = (MainView)  ((ViewGroup)getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);

        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarColor(this, 0xff3097fd);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
