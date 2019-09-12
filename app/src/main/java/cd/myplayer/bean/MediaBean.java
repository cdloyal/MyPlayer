package cd.myplayer.bean;

import java.io.Serializable;

/**
 * 作者：chenda
 * 时间：2019/8/28:14:15
 * 邮箱：
 * 说明：
 */
public class MediaBean implements Serializable {
    private static final long serialVersionUID = -7202385966297976484L;
    private long size;              //音视频大小
    private String displayName;    //音视频文件名称
    private long duration;          //音视频时间长度
    private String data;            //Path to the file on disk.

    public MediaBean() {
    }

    public MediaBean(long size, String displayName, long duration, String data) {
        this.size = size;
        this.displayName = displayName;
        this.duration = duration;
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
