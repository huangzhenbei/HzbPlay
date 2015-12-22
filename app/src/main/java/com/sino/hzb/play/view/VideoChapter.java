package com.sino.hzb.play.view;


public class VideoChapter {
    public VideoChapter(){};
    public enum Status {
        Init, Playing, Played
    }

    public int anchor_time;
    public String anchor_title;
    public int video_id;

    public Status status = Status.Init;
    public int duration;
    public boolean isLastChapter;

    public int getByteSize() {
        int size = 0;
        size += 2 * 32;
        size += anchor_title == null ? 0 : anchor_title.getBytes().length;
                
        return size;
    }
}
