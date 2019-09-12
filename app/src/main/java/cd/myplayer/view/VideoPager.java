package cd.myplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cd.myplayer.R;
import cd.myplayer.adapter.VideoPagerAdapter;
import cd.myplayer.bean.MediaBean;
import cd.myplayer.presenter.IVideoPagerPresenter;
import cd.myplayer.viewcontroller.IVideoPagerController;

/**
 * 作者：chenda
 * 时间：2019/8/27:16:50
 * 邮箱：
 * 说明：
 */
public class VideoPager extends RelativeLayout {

    private final Context context;
    public View pb_loading;
    public View tv_nomedia;
    public ListView lv_video_pager;
    private ArrayList<MediaBean> mediaBeans;

    public VideoPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        this.context = context;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        pb_loading = findViewById(R.id.pb_loading);
        tv_nomedia = findViewById(R.id.tv_nomedia);
        lv_video_pager = findViewById(R.id.lv_video_pager);

        pb_loading.setVisibility(View.VISIBLE);
        tv_nomedia.setVisibility(View.GONE);
        lv_video_pager.setVisibility(View.GONE);

    }

    private void isNoVideo(){
        if(mediaBeans==null||mediaBeans.size()==0){
            pb_loading.setVisibility(View.GONE);
            tv_nomedia.setVisibility(View.VISIBLE);
            lv_video_pager.setVisibility(View.GONE);
            return;
        }

        pb_loading.setVisibility(View.GONE);
        tv_nomedia.setVisibility(View.GONE);
        lv_video_pager.setVisibility(View.VISIBLE);
    }

    public void setVideoPagerPresenter(IVideoPagerPresenter videoPagerPresenter){
        videoPagerPresenter.setContext(context);
        videoPagerPresenter.setIVideoPagerController(new IVideoPagerController(){
            @Override
            public void onVideoDataInited(ArrayList<MediaBean> mediaBeans) {
                VideoPager.this.mediaBeans = mediaBeans;
                isNoVideo();
                lv_video_pager.setAdapter(new VideoPagerAdapter(context,mediaBeans));
            }
        });
        videoPagerPresenter.initData();
        lv_video_pager.setOnItemClickListener(videoPagerPresenter.getOnItemClickListener());
    }

}
