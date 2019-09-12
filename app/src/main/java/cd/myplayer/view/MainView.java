package cd.myplayer.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import cd.myplayer.R;

/**
 * 作者：chenda
 * 时间：2019/8/29:14:00
 * 邮箱：
 * 说明：
 */
public class MainView extends LinearLayout {

    public View titlebar;
    public FrameLayout flMain;
    public RadioGroup rgMain;
    public RadioButton rbVideo;
    public RadioButton rbAudio;
    public RadioButton rbNetVideo;
    public RadioButton rbNetAudio;
//    private FragmentActivity fgActigity;
    private ArrayList<Fragment> fragments;
    private Context context;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

//    public void setFragmentActivity(FragmentActivity fgActigity){
//        this.fgActigity = fgActigity;
//    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        titlebar = findViewById( R.id.titlebar );
        flMain = (FrameLayout)findViewById( R.id.fl_main );
        rgMain = (RadioGroup)findViewById( R.id.rg_main );
        rbVideo = (RadioButton)findViewById( R.id.rb_video );
        rbAudio = (RadioButton)findViewById( R.id.rb_audio );
        rbNetVideo = (RadioButton)findViewById( R.id.rb_net_video );
        rbNetAudio = (RadioButton)findViewById( R.id.rb_net_audio );

        rgMain.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

    }

    public interface IMainViewPresenter{
        ArrayList<Fragment> getFragments();
    }
    public void setMainViewPresenter(IMainViewPresenter mainViewPresenter){
        this.fragments = mainViewPresenter.getFragments();
        ((RadioButton)rgMain.getChildAt(0)).setChecked(true);
    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            int checkedPosition=0;

            switch (checkedId){
                case R.id.rb_video:
                    checkedPosition = 0;
                    break;
                case R.id.rb_audio:
                    checkedPosition = 1;
                    break;
                case R.id.rb_net_video:
                    checkedPosition = 2;
                    break;
                case R.id.rb_net_audio:
                    checkedPosition = 3;
                    break;
            }

            if(context==null||fragments==null||fragments.size()<checkedPosition)
                return;

            //点击不同的按钮，Fragment替换不同的页面
            FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fl_main,fragments.get(checkedPosition));

            ft.commit();
        }
    }

}
