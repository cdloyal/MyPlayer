package cd.myplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;

import cd.myplayer.R;
import cd.myplayer.bean.MediaBean;
import cd.myplayer.presenter.IVideoPlayPresenter;
import cd.myplayer.presenter.VideoPlayPresenter;
import cd.myplayer.utils.LogUtil;
import cd.myplayer.utils.StatusBarUtil;
import cd.myplayer.view.VideoPlayer;

import static cd.myplayer.utils.StatusBarUtil.setRootViewFitsSystemWindows;

/**
 * 作者：chenda
 * 时间：2019/8/28:16:39
 * 邮箱：
 * 说明：
 */
public class VideoPlayerActivity extends Activity {


    private VideoPlayPresenter iVideoPlayPresenter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_system_video_player);
        //这里注意下 因为在评论区发现有网友调用setRootViewFitsSystemWindows 里面 winContent.getChildCount()=0 导致代码无法继续
        //是因为你需要在setContentView之后才可以调用 setRootViewFitsSystemWindows

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
//        StatusBarUtil.setTranslucentStatus(this);

        VideoPlayer videoPlayer = (VideoPlayer) ((ViewGroup)getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
        iVideoPlayPresenter = new VideoPlayPresenter();
        iVideoPlayPresenter.setActivity(this);
        videoPlayer.setVideoPlayPresenter(iVideoPlayPresenter);

        Intent intent = getIntent();

        Uri uri = intent.getData();
        Bundle bundle = intent.getExtras();
        if(uri!=null){
//            iVideoPlayPresenter.play(uri);
        }else if(bundle!=null){
            ArrayList<MediaBean> mediaBeans = (ArrayList<MediaBean>) bundle.get("mediaBeans");
            int index = (int) bundle.get("index");
            if(mediaBeans==null||index<0)
                return;
            iVideoPlayPresenter.setMediaBeans(mediaBeans);
            iVideoPlayPresenter.play(index);
        }


    }


    @Override
    protected void onPause() {
        LogUtil.d("onPause()");
        iVideoPlayPresenter.onStop();
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtil.d("onStop()");

        super.onStop();

    }

    @Override
    protected void onDestroy() {
        LogUtil.d("onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtil.d("onSaveInstanceState()");
        iVideoPlayPresenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.d("onRestoreInstanceState()");
        iVideoPlayPresenter.onRestoreInstanceState(savedInstanceState);
    }

    //    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        boolean dispatchKeyEvent = super.dispatchKeyEvent(event);
//        LogUtil.d("dispatchKeyEvent="+dispatchKeyEvent+"event.getKeyCode()"+event.getKeyCode()+"event.getAction()"+event.getAction());
//        return dispatchKeyEvent;
//    }
//
//
//    /**
//     * 硬件按钮
//     * */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//
//        LogUtil.d("VideoPlayerActivity onKeyDown() keyCode="+keyCode);
//        switch (keyCode){
//            case  KeyEvent.KEYCODE_VOLUME_DOWN:
//            case  KeyEvent.KEYCODE_VOLUME_UP:
//                return true;
//            default:
//                break;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

}
