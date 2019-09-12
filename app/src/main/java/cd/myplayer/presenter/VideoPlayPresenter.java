package cd.myplayer.presenter;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.ArrayList;

import cd.myplayer.bean.MediaBean;
import cd.myplayer.viewcontroller.IVideoPlayController;

/**
 * 作者：chenda
 * 时间：2019/8/30:10:46
 * 邮箱：
 * 说明：
 */
public class VideoPlayPresenter implements IVideoPlayPresenter {


    private IVideoPlayController videoPlayController;
    private ArrayList<MediaBean> mediaBeans;
    private Activity activity;

    @Override
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setVideoPlayController(IVideoPlayController videoPlayController) {
        this.videoPlayController = videoPlayController;
    }

//    @Override
//    public void play(Uri uri) {
//        videoPlayController.play(uri);
//    }

    @Override
    public void play(int index) {
        if (videoPlayController == null || mediaBeans == null || index < 0 || mediaBeans.size() <= 0)
            return;

//        Uri uri = Uri.parse(mediaBeans.get(index).getData());
//        play(uri);

        videoPlayController.play(index);
    }

    @Override
    public void setMediaBeans(ArrayList<MediaBean> mediaBeans) {
        if (videoPlayController == null)
            return;

        this.mediaBeans = mediaBeans;
        videoPlayController.setMediaBeans(mediaBeans);
    }

    @Override
    public void exit() {
        if (activity != null)
            activity.finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (videoPlayController == null)
            return;
        videoPlayController.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (videoPlayController == null)
            return;
        videoPlayController.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void SwitchScreen() {
        if (activity == null)
            return;

        int  orientation = activity.getRequestedOrientation();
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || orientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            //垂直方向，那么切换成水平方向
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
//        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void onStop() {
        if (videoPlayController == null)
            return;
        videoPlayController.onStop();
    }
}
