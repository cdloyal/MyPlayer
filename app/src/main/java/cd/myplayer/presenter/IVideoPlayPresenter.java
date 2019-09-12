package cd.myplayer.presenter;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

import cd.myplayer.bean.MediaBean;
import cd.myplayer.viewcontroller.IVideoPlayController;

/**
 * 作者：chenda
 * 时间：2019/8/30:10:45
 * 邮箱：
 * 说明：
 */
public interface IVideoPlayPresenter {
    void setVideoPlayController(IVideoPlayController videoPlayController);
//    void play(Uri uri);

    void setActivity(Activity activity);

    void play(int index);
    void setMediaBeans(ArrayList<MediaBean> mediaBeans);

    void exit();

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void SwitchScreen();
}
