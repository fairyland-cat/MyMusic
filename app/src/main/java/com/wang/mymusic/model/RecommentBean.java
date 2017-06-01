package com.wang.mymusic.model;

import java.util.List;

/**
 * Created by wang on 2017/2/15.
 */

public class RecommentBean {


    /**
     * song_list : [{"artist_id":"89","language":"国语","pic_big":"http://musicdata.baidu.com/data2/pic/46e568a3a6e226b660530c00a7c1e9ae/276867494/276867494.jpg@s_0,w_150","pic_small":"http://musicdata.baidu.com/data2/pic/46e568a3a6e226b660530c00a7c1e9ae/276867494/276867494.jpg@s_0,w_90","country":"内地","area":"0","publishtime":"2016-11-14","album_no":"1","lrclink":"http://musicdata.baidu.com/data2/lrc/124788a17930411d87ece9f05e437b8d/533392121/533392121.lrc","copy_type":"1","hot":"706980","all_artist_ting_uid":"1078","resource_type":"0","is_new":"0","rank_change":"0","rank":"1","all_artist_id":"89","style":"流行","del_status":"0","relate_status":"0","toneid":"0","all_rate":"flac,320,256,128,64","file_duration":200,"has_mv_mobile":0,"versions":"","song_id":"276867440","title":"刚好遇见你","ting_uid":"1078","author":"李玉刚","album_id":"276867491","album_title":"刚好遇见你","is_first_publish":0,"havehigh":2,"charge":0,"has_mv":0,"learn":0,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"0000000000","artist_name":"李玉刚"},{"artist_id":"29","language":"国语","pic_big":"http://musicdata.baidu.com/data2/pic/ed58ab93ec08650f765bc40500ba47b1/273945524/273945524.jpg@s_0,w_150","pic_small":"http://musicdata.baidu.com/data2/pic/ed58ab93ec08650f765bc40500ba47b1/273945524/273945524.jpg@s_0,w_90","country":"港台","area":"1","publishtime":"2016-06-24","album_no":"8","lrclink":"http://musicdata.baidu.com/data2/lrc/c767fcba9328404a920ec6522f5b3b9e/273945508/273945508.lrc","copy_type":"1","hot":"636981","all_artist_ting_uid":"7994","resource_type":"0","is_new":"0","rank_change":"1","rank":"2","all_artist_id":"29","style":"R&B","del_status":"0","relate_status":"0","toneid":"0","all_rate":"64,128,256,320,flac","file_duration":215,"has_mv_mobile":0,"versions":"","song_id":"266322598","title":"告白气球","ting_uid":"7994","author":"周杰伦","album_id":"266322553","album_title":"周杰伦的床边故事","is_first_publish":0,"havehigh":2,"charge":0,"has_mv":0,"learn":1,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"0000000000","artist_name":"周杰伦"}]
     * billboard : {"billboard_type":"2","billboard_no":"2105","update_date":"2017-02-15","billboard_songnum":"588","havemore":1,"name":"热歌榜","comment":"该榜单是根据百度音乐平台歌曲每周播放量自动生成的数据榜单，统计范围为百度音乐平台上的全部歌曲，每日更新一次","pic_s640":"http://b.hiphotos.baidu.com/ting/pic/item/5d6034a85edf8db1194683910b23dd54574e74df.jpg","pic_s444":"http://d.hiphotos.baidu.com/ting/pic/item/c83d70cf3bc79f3d98ca8e36b8a1cd11728b2988.jpg","pic_s260":"http://a.hiphotos.baidu.com/ting/pic/item/838ba61ea8d3fd1f1326c83c324e251f95ca5f8c.jpg","pic_s210":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_58c1700bf56062108d8d622a95708032.jpg","web_url":"http://music.baidu.com/top/dayhot"}
     * error_code : 22000
     */

    private List<SongListBean> song_list;

    public List<SongListBean> getSong_list() {
        return song_list;
    }

    public void setSong_list(List<SongListBean> song_list) {
        this.song_list = song_list;
    }

    public class SongListBean{
        private String artist_id;
        private String pic_big;
        private String pic_small;
        private String lrclink;
        private String song_id;
        private String title;
        private String author;

        public String getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(String artist_id) {
            this.artist_id = artist_id;
        }

        public String getPic_big() {
            return pic_big;
        }

        public void setPic_big(String pic_big) {
            this.pic_big = pic_big;
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

        public String getSong_id() {
            return song_id;
        }

        public void setSong_id(String song_id) {
            this.song_id = song_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}
