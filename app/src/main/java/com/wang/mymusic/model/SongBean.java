package com.wang.mymusic.model;

/**
 * Created by wang on 2017/2/23.
 */

public class SongBean {

    /**
     * error_code : 22000
     * bitrate : {"file_bitrate":64,"free":1,"file_link":"http://yinyueshiting.baidu.com/data2/music/42783748/42783748.mp3?xcode=35d8a0d4300a46135342a3548728ab76","file_extension":"mp3","original":0,"file_size":2679447,"file_duration":322,"show_link":"","song_file_id":42783748,"replay_gain":"0.000000"}
     * songinfo : {"artist_id":"130","all_artist_id":"130","album_no":"1","pic_big":"http://musicdata.baidu.com/data2/pic/88582702/88582702.jpg@s_0,w_150","pic_small":"http://musicdata.baidu.com/data2/pic/88582702/88582702.jpg@s_0,w_90","relate_status":"0","resource_type":"2","copy_type":"1","lrclink":"http://musicdata.baidu.com/data2/lrc/238975978/238975978.lrc","pic_radio":"http://musicdata.baidu.com/data2/pic/88582702/88582702.jpg@s_0,w_300","toneid":"600902000004240302","all_rate":"64,128,192,256,320,flac","play_type":"","has_mv_mobile":1,"pic_premium":"http://musicdata.baidu.com/data2/pic/88582702/88582702.jpg@s_0,w_500","pic_huge":"","resource_type_ext":"0","publishtime":"1993-09-01","si_presale_flag":"0","del_status":"1","song_id":"877578","title":"海阔天空","ting_uid":"1100","author":"Beyond","album_id":"197864","album_title":"海阔天空","is_first_publish":0,"havehigh":2,"charge":0,"has_mv":1,"learn":1,"song_source":"web","piao_id":"0","korean_bb_song":"0","mv_provider":"1100000000","special_type":0,"collect_num":866,"share_num":83,"comment_num":137}
     */

    private BitrateBean bitrate;
    private SonginfoBean songinfo;

    public BitrateBean getBitrate() {
        return bitrate;
    }

    public void setBitrate(BitrateBean bitrate) {
        this.bitrate = bitrate;
    }

    public SonginfoBean getSonginfo() {
        return songinfo;
    }

    public void setSonginfo(SonginfoBean songinfo) {
        this.songinfo = songinfo;
    }

    public static class BitrateBean {
        /**
         * file_bitrate : 64
         * free : 1
         * file_link : http://yinyueshiting.baidu.com/data2/music/42783748/42783748.mp3?xcode=35d8a0d4300a46135342a3548728ab76
         * file_extension : mp3
         * original : 0
         * file_size : 2679447
         * file_duration : 322
         * show_link :
         * song_file_id : 42783748
         * replay_gain : 0.000000
         */

        private String file_link;
        private int file_size;
        private String file_extension;

        public String getFile_link() {
            return file_link;
        }

        public void setFile_link(String file_link) {
            this.file_link = file_link;
        }

        public int getFile_size() {
            return file_size;
        }

        public void setFile_size(int file_size) {
            this.file_size = file_size;
        }

        public String getFile_extension() {
            return file_extension;
        }

        public void setFile_extension(String file_extension) {
            this.file_extension = file_extension;
        }
    }

    public static class SonginfoBean {
        /**
         * artist_id : 130
         * all_artist_id : 130
         * album_no : 1
         * pic_big : http://musicdata.baidu.com/data2/pic/88582702/88582702.jpg@s_0,w_150
         * pic_small : http://musicdata.baidu.com/data2/pic/88582702/88582702.jpg@s_0,w_90
         * relate_status : 0
         * resource_type : 2
         * copy_type : 1
         * lrclink : http://musicdata.baidu.com/data2/lrc/238975978/238975978.lrc
         * pic_radio : http://musicdata.baidu.com/data2/pic/88582702/88582702.jpg@s_0,w_300
         * toneid : 600902000004240302
         * all_rate : 64,128,192,256,320,flac
         * play_type :
         * has_mv_mobile : 1
         * pic_premium : http://musicdata.baidu.com/data2/pic/88582702/88582702.jpg@s_0,w_500
         * pic_huge :
         * resource_type_ext : 0
         * publishtime : 1993-09-01
         * si_presale_flag : 0
         * del_status : 1
         * song_id : 877578
         * title : 海阔天空
         * ting_uid : 1100
         * author : Beyond
         * album_id : 197864
         * album_title : 海阔天空
         * is_first_publish : 0
         * havehigh : 2
         * charge : 0
         * has_mv : 1
         * learn : 1
         * song_source : web
         * piao_id : 0
         * korean_bb_song : 0
         * mv_provider : 1100000000
         * special_type : 0
         * collect_num : 866
         * share_num : 83
         * comment_num : 137
         */

        private String pic_small;
        private String lrclink;
        private String title;
        private int file_duration;
        private String author;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPic_small() {
            return pic_small;
        }

        public void setPic_small(String pic_small) {
            this.pic_small = pic_small;
        }

        public String getLrclink() {
            return lrclink;
        }

        public void setLrclink(String lrclink) {
            this.lrclink = lrclink;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getFile_duration() {
            return file_duration;
        }

        public void setFile_duration(int file_duration) {
            this.file_duration = file_duration;
        }
    }
}
