package com.wang.mymusic.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by wang on 2017/2/15.
 */

public interface HttpApi {

    @Headers("User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64)")

    //获取音乐列表地址
    @GET("v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.billboard.billList")
    Call<RecommentBean> getrecomment(@Query("type") int type,@Query("size") String size,
                                     @Query("offset") int offset);

    @Headers("User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
    //获取音乐播放地址
    @GET("v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.song.playAAC")
    Call<SongBean> getsong(@Query("songid") String id);

    //获取歌词
    @GET
    Call<ResponseBody> downlrc(@Url String lrcurl);
}
