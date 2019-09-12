package cd.myplayer.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import cd.myplayer.bean.MediaBean;

/**
 * 作者：chenda
 * 时间：2019/8/28:14:40
 * 邮箱：
 * 说明：
 */
public class MediaUtils {


    public static ArrayList<MediaBean> queryLocalVideo(Context context){
        ArrayList<MediaBean> mediaBeans = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] objects = {
                MediaStore.Video.Media.DISPLAY_NAME,//在Sdcard显示的名称
                MediaStore.Video.Media.DURATION,//视频的长度
                MediaStore.Video.Media.SIZE,//视频文件大小
                MediaStore.Video.Media.DATA//视频的绝对地址
        };
        Cursor cursor =  contentResolver.query(uri, objects, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()){
                String displayName = cursor.getString(0);
                long duration = cursor.getLong(1);
                long size = cursor.getLong(2);
                String data = cursor.getString(3);

                MediaBean video = new MediaBean( size,  displayName,  duration,  data);
                mediaBeans.add(video);
            }
            cursor.close();
        }

        return mediaBeans;
    }

}
