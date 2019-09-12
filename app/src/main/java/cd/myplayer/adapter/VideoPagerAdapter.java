package cd.myplayer.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cd.myplayer.R;
import cd.myplayer.bean.MediaBean;
import cd.myplayer.utils.Utils;

/**
 * 作者：chenda
 * 时间：2019/8/28:15:03
 * 邮箱：
 * 说明：
 */
public class VideoPagerAdapter extends BaseAdapter {

    private ArrayList<MediaBean> mediaBeans;
    private Context context;
    private Utils utils;

    public VideoPagerAdapter(Context context,ArrayList<MediaBean> mediaBeans) {
        this.mediaBeans = mediaBeans;
        this.context = context;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return mediaBeans!=null?mediaBeans.size():0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            holder = new Holder();
            convertView = View.inflate(context, R.layout.item_video_pager,null);
            holder.ivIcon = (ImageView)convertView.findViewById( R.id.iv_icon );
            holder.tvName = (TextView)convertView.findViewById( R.id.tv_name );
            holder.tvDuration = (TextView)convertView.findViewById( R.id.tv_duration );
            holder.tvSize = (TextView)convertView.findViewById( R.id.tv_size );
        }else {
            holder = (Holder) convertView.getTag();
        }

        holder.tvName.setText(mediaBeans.get(position).getDisplayName());
        holder.tvDuration.setText(utils.stringForTime((int) mediaBeans.get(position).getDuration()));
        holder.tvSize.setText(Formatter.formatFileSize(context,mediaBeans.get(position).getSize()));

        convertView.setTag(holder);
        return convertView;
    }

    static class Holder{
        private ImageView ivIcon;
        private TextView tvName;
        private TextView tvDuration;
        private TextView tvSize;
    }
}
