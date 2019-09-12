package cd.myplayer.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cd.myplayer.R;
import cd.myplayer.bean.MediaBean;
import cd.myplayer.presenter.IVideoPlayPresenter;
import cd.myplayer.utils.BaseHandler;
import cd.myplayer.utils.LogUtil;
import cd.myplayer.utils.Utils;
import cd.myplayer.viewcontroller.IVideoPlayController;

import static android.content.Context.AUDIO_SERVICE;

/***
 * VideoView方法
 * int getCurrentPosition()：获取当前播放的位置，单位毫秒
 * int getDuration()：获取当前播放视频的总长度，单位毫秒
 * isPlaying()：当前VideoView是否在播放视频。
 * void pause()：暂停，视频资源未绑定就绪，或者已经暂停时，再次 pause不会有影响
 * void seekTo(int msec)：从第几毫秒开始播放。
 * void resume()：重新播放。
 * void setVideoPath(String path)：以文件路径的方式设置 VideoView 播放的视频源。
 * void setVideoURI(Uri uri)：以 Uri 的方式设置 VideoView 播放的视频源，可以是网络 Uri 或本地 Uri。
 * void start()：开始播放，视频资源未绑定就绪，或者已经在播放时，再次 start 不会有影响
 * void stopPlayback()：停止播放，并释放资源
 * setMediaController(MediaController controller)：设置MediaController控制器。
 * setOnCompletionListener(MediaPlayer.onCompletionListener l)：监听播放完成的事件。
 * setOnErrorListener(MediaPlayer.OnErrorListener l)：监听播放发生错误时候的事件。
 *     setOnPreparedListener(MediaPlayer.OnPreparedListener l)：：监听视频装载完成的事件。
 */

public class VideoPlayer extends RelativeLayout implements View.OnClickListener, IVideoPlayController {

    /**
     * 定时隐藏控制栏，5000ms
     */
    private static final int HIDE_RLCONTROLLER = 0;
    /**
     * 定时更新进度条，5000ms
     */
    private static final int UPDATA_VIDEOSEEK = 1;

    private final Context context;
    public VideoView videoview;
    public View rlLoading;
    public View llBuffer;

    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnVideoExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSwitchScreen;
    private MyReciver myReciver;

    private int maxVolume;
    private ArrayList<MediaBean> mediaBeans;
    private AudioManager am;
    private View rlMediaController;
    private GestureDetector gestureDetector;
    private int currentPlayIndex;
    private boolean isMute = false; //是否静音
    private boolean isOnCompletion; //有一个bug，还有用个标志位
    private int currentVolume;

    private MyHandler handler = new MyHandler(this);
    private Utils utils;
    private IVideoPlayPresenter videoPlayPresenter;
    private int startVidoePosition = 0;
    private boolean isNetUri;

    private static class MyHandler extends BaseHandler<VideoPlayer> {

        MyHandler(VideoPlayer videoPlayer) {
            super(videoPlayer);
        }

        @Override
        public void handlerMessage(VideoPlayer videoPlayer, Message msg) {
            if (videoPlayer == null)
                return;

            switch (msg.what) {
                case HIDE_RLCONTROLLER:
                    videoPlayer.hideRlController();
                    break;

                case UPDATA_VIDEOSEEK:
                    videoPlayer.updateCurrentTime();
                    videoPlayer.updateSysTime();

                    if(videoPlayer.isNetUri){
                        //显示第二进度
                        int buffer = videoPlayer.videoview.getBufferPercentage();   //当前款冲进度0~100
                        int secondaryProgress = videoPlayer.seekbarVideo.getMax() * (buffer/100);
                        LogUtil.d("secondaryProgress="+secondaryProgress+",getCurrentPosition()="+videoPlayer.videoview.getCurrentPosition());
                        videoPlayer.seekbarVideo.setSecondaryProgress(secondaryProgress);
                    }
//                    LogUtil.d("updata videoview.getCurrentPosition()="+videoPlayer.videoview.getCurrentPosition());
                    break;

                default:
                    break;
            }
        }
    }


    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
    }

    private void findViews() {
        videoview = (VideoView) findViewById(R.id.videoview);
        rlLoading = (View) findViewById(R.id.rl_loading);
        llBuffer = (View) findViewById(R.id.ll_buffer);

        rlMediaController = findViewById(R.id.rl_media_controller);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvTime = (TextView) findViewById(R.id.tv_time);
        btnVoice = (Button) findViewById(R.id.btn_voice);
        seekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
        btnSwitchPlayer = (Button) findViewById(R.id.btn_switch_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnVideoExit = (Button) findViewById(R.id.btn_video_exit);
        btnVideoPre = (Button) findViewById(R.id.btn_video_pre);
        btnVideoStartPause = (Button) findViewById(R.id.btn_video_start_pause);
        btnVideoNext = (Button) findViewById(R.id.btn_video_next);
        btnVideoSwitchScreen = (Button) findViewById(R.id.btn_video_switch_screen);

        btnVoice.setOnClickListener(this);
        btnSwitchPlayer.setOnClickListener(this);
        btnVideoExit.setOnClickListener(this);
        btnVideoPre.setOnClickListener(this);
        btnVideoStartPause.setOnClickListener(this);
        btnVideoNext.setOnClickListener(this);
        btnVideoSwitchScreen.setOnClickListener(this);

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtil.d("onCompletion videoview.getCurrentPosition()=" + videoview.getCurrentPosition());
                isOnCompletion = true;
                handler.removeMessages(UPDATA_VIDEOSEEK);
                seekbarVideo.setProgress(videoview.getDuration());
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_play_selector);
            }
        });
        videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoview.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    LogUtil.d("MediaPlayer.OnInfoListener() what=" + what + ",extra=" + extra);
                    switch (what){
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            //开始卡顿--显示加载动画、加载速度

                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            //结束卡顿--因此加载动画、加载速度

                            break;
                    }
                    return false;
                }
            });
        }

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                LogUtil.d("videoview onPrepared()");

                rlLoading.setVisibility(GONE);

                videoview.seekTo(startVidoePosition);
                start();
                startVidoePosition=0;
            }
        });

        seekbarVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    currentVolume = progress;
                    updateVolume();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE_RLCONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                currentVolume = seekBar.getProgress();
//                updateVolume();
                handler.removeMessages(HIDE_RLCONTROLLER);
                handler.sendEmptyMessageDelayed(HIDE_RLCONTROLLER, 5000);
            }
        });
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                LogUtil.d("onProgressChanged() fromUser="+fromUser);
                if (fromUser) {
                    videoview.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtil.d("onStartTrackingTouch()");
                handler.removeMessages(HIDE_RLCONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.d("onStartTrackingTouch()");
//                videoview.seekTo(seekBar.getProgress());
                handler.removeMessages(HIDE_RLCONTROLLER);
                handler.sendEmptyMessageDelayed(HIDE_RLCONTROLLER, 5000);
            }
        });

        rlMediaController.setVisibility(GONE);

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        myReciver = new MyReciver();
        context.registerReceiver(myReciver, filter);

        //实例化AudioManager
        am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        LogUtil.d("maxVolume=" + maxVolume);

        gestureDetector = new GestureDetector(context, new MyOnGestureListener());

        utils = new Utils();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtil.d("dispatchKeyEvent" + " event.getKeyCode()" + event.getKeyCode() + "event.getAction()" + event.getAction());

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                currentVolume = --currentVolume < 0 ? 0 : currentVolume;
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                currentVolume = ++currentVolume > maxVolume ? maxVolume : currentVolume;
            }
            LogUtil.d("dispatchKeyEvent currentVolume=" + currentVolume);
            handler.removeMessages(HIDE_RLCONTROLLER);
            showRlController();
            updateVolume();
        } else if (event.getAction() == KeyEvent.ACTION_UP) {
            handler.removeMessages(HIDE_RLCONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_RLCONTROLLER, 50000);
        }

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
                return true;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        gestureDetector.onTouchEvent(event);

        return true;
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        //快速连续点击两下是不会触发的
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            super.onSingleTapConfirmed(e);

            LogUtil.d("onSingleTapConfirmed()");
            if (rlMediaController.getVisibility() == VISIBLE)
                hideRlController();
            else {
                showRlController();
                handler.removeMessages(HIDE_RLCONTROLLER);
                handler.sendEmptyMessageDelayed(HIDE_RLCONTROLLER, 5000);
            }

            return true;
        }

        //双击
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            super.onDoubleTap(e);
            if (videoview.isPlaying())
                videoview.pause();
            else
                videoview.start();

            return true;
        }
    }


    @Override
    public void onClick(View v) {

        if (v == btnVoice) {
            // Handle clicks for btnVoice
            isMute = !isMute;
            updateVolume();
        } else if (v == btnSwitchPlayer) {
            // Handle clicks for btnSwitchPlayer
        } else if (v == btnVideoExit) {
            // Handle clicks for btnVideoExit
            videoview.pause();
            videoview.stopPlayback();
            if (videoPlayPresenter != null)
                videoPlayPresenter.exit();
        } else if (v == btnVideoPre) {
            // Handle clicks for btnVideoPre
            videoview.pause();
            videoview.stopPlayback();
            currentPlayIndex = --currentPlayIndex < 0 ? 0 : currentPlayIndex;
            play(currentPlayIndex);
        } else if (v == btnVideoStartPause) {
            // Handle clicks for btnVideoStartPause
            if (videoview.isPlaying()) {
                pause();
            } else {
                start();
            }
        } else if (v == btnVideoNext) {
            // Handle clicks for btnVideoNext
            videoview.pause();
            videoview.stopPlayback();
            currentPlayIndex = ++currentPlayIndex > mediaBeans.size() - 1 ? mediaBeans.size() - 1 : currentPlayIndex;
            play(currentPlayIndex);
        } else if (v == btnVideoSwitchScreen) {
            // Handle clicks for btnVideoSwitchScreen
            videoPlayPresenter.SwitchScreen();

        }

        handler.removeMessages(HIDE_RLCONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_RLCONTROLLER, 5000);
    }

    @Override
    protected void onDetachedFromWindow() {
        LogUtil.d("onDetachedFromWindow()");
        if (myReciver != null)
            context.unregisterReceiver(myReciver);
        super.onDetachedFromWindow();
    }

    private class MyReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("BATTERY onReceive intent.getAction()=" + intent.getAction());
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                if (level <= 0) {
                    ivBattery.setImageResource(R.drawable.ic_battery_0);
                } else if (level <= 10) {
                    ivBattery.setImageResource(R.drawable.ic_battery_10);
                } else if (level <= 20) {
                    ivBattery.setImageResource(R.drawable.ic_battery_20);
                } else if (level <= 40) {
                    ivBattery.setImageResource(R.drawable.ic_battery_40);
                } else if (level <= 60) {
                    ivBattery.setImageResource(R.drawable.ic_battery_60);
                } else if (level <= 80) {
                    ivBattery.setImageResource(R.drawable.ic_battery_80);
                } else if (level <= 100) {
                    ivBattery.setImageResource(R.drawable.ic_battery_100);
                } else {
                    ivBattery.setImageResource(R.drawable.ic_battery_100);
                }
            }
        }
    }

    private void showRlController() {
        if (rlMediaController.getVisibility() == VISIBLE)
            return;
        rlMediaController.setVisibility(VISIBLE);
        updateName(mediaBeans.get(currentPlayIndex).getDisplayName());
        updateSysTime();
        currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        updateVolume();
        updateDuration();
        updateCurrentTime();
    }

    private void hideRlController() {
        handler.removeMessages(UPDATA_VIDEOSEEK);
        rlMediaController.setVisibility(GONE);
    }

    private void updateName(String name) {
        //设置标题
        tvName.setText(name);
    }

    private void updateSysTime() {
        //设置系统时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String sysTime = dateFormat.format(new Date());
        tvTime.setText(sysTime);
    }

    private void updateVolume() {
        seekbarVoice.setMax(maxVolume);
        if (isMute) {
            //设置成静音
            seekbarVoice.setProgress(0);
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        } else {
            seekbarVoice.setProgress(currentVolume);
            am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        }
    }

    private void updateDuration() {
        if (currentPlayIndex < 0 || mediaBeans == null || mediaBeans.size() - 1 < currentPlayIndex)
            return;

        int duratioin = (int) mediaBeans.get(currentPlayIndex).getDuration();
        if(isNetUri){
            duratioin = videoview.getDuration();
        }

        tvDuration.setText(utils.stringForTime(duratioin));
        seekbarVideo.setMax(duratioin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekbarVideo.setMin(0);
        }
    }

    private void updateCurrentTime() {
        if (currentPlayIndex < 0 || mediaBeans == null || mediaBeans.size() - 1 < currentPlayIndex)
            return;

        seekbarVideo.setProgress(videoview.getCurrentPosition());
        tvCurrentTime.setText(utils.stringForTime(videoview.getCurrentPosition()));

        handler.removeMessages(UPDATA_VIDEOSEEK);
        handler.sendEmptyMessageDelayed(UPDATA_VIDEOSEEK,500);
    }

    @Override
    public void play(int index) {
        if (index < 0 || mediaBeans == null || mediaBeans.size() - 1 < index)
            return;

        rlLoading.setVisibility(VISIBLE);


        currentPlayIndex = index;
        MediaBean mediaBean = mediaBeans.get(index);

        isNetUri = isNetUri(mediaBean.getData());

        videoview.setVideoURI(Uri.parse(mediaBean.getData()));

    }

    private void start() {
        if (videoview.isPlaying())
            return;


        if (isOnCompletion) {
            videoview.resume();
            updateCurrentTime();
        } else {
            videoview.start();
        }
        isOnCompletion = false;

        btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
    }

    //    private void stop(){
//        videoview.
//        btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_play_selector);
//    }
//    private void release(){
//        videoview
//    }
    private void pause() {
        if (!videoview.isPlaying())
            return;

        videoview.pause();
        btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_play_selector);
    }



    public void setVideoPlayPresenter(IVideoPlayPresenter videoPlayPresenter) {
        this.videoPlayPresenter = videoPlayPresenter;
//        videoPlayPresenter.setVideoPlayController(new IVideoPlayController() {
//            @Override
//            public void play(Uri uri) {
//                if(uri==null)
//                    return;
//
//                videoview.setVideoURI(uri);
//                videoview.start();
//            }
//        });

        videoPlayPresenter.setVideoPlayController(this);
    }

    @Override
    public void setMediaBeans(ArrayList<MediaBean> mediaBeans) {
        VideoPlayer.this.mediaBeans = mediaBeans;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentPlayIndex",currentPlayIndex);
        outState.putInt("startVidoePosition",startVidoePosition);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        currentPlayIndex = savedInstanceState.getInt("currentPlayIndex");
        startVidoePosition = savedInstanceState.getInt("startVidoePosition");
        LogUtil.d("startVidoePosition="+startVidoePosition);
    }

    @Override
    public void onStop() {
        startVidoePosition = videoview.getCurrentPosition();
        LogUtil.d("startVidoePosition="+startVidoePosition);
    }


    /**
     * 流媒体播放器，流媒体服务器
     * */

    private boolean isNetUri(String path){
        boolean result = false;

        if(path!=null&&(path.toLowerCase().startsWith("http")
                ||path.toLowerCase().startsWith("mms")
                ||path.toLowerCase().startsWith("rtsp")))
            result = true;

        return result;
    }

}
