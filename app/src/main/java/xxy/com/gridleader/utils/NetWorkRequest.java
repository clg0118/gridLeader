package xxy.com.gridleader.utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Betty Li on 2018/1/9.
 */

public interface NetWorkRequest {

    @POST("do/m/loginversion")
    Call<String> VALID_VERSION();

    //1.登录
    @POST("do/m/login")
    Call<String> LOGIN_CALL(@Query("username") String username, @Query("password") String password, @Query("version") String version);

    //2.修改密码
    @POST("do/m/system/user/updatepsd")
    Call<String> UPDATEPSD_CALL(@Query("id") long id, @Query("oldPassword") String oldPassword, @Query("password") String password);

    //3.问题汇总
    @POST("do/m/alarm/totalalarm/query")
    Call<String> TOTALALARM_CALL(@Query("userId") long userId, @Query("type") int type);

    //3.首页问题百分比统计
    @POST("do/m/alarm/percent/query")
    Call<String> PERCENTQUERY_CALL(@Query("userId") long userId);

    //5.上报问题前获取问题类型
    @POST("do/m/assetscategory/allcategory/query")
    Call<String> QUERYTYPE();

    //5.上报问题前获取问题类型
    @POST("do/m/assetscategory/querycategory/query")
    Call<String> QUERYTYPEBYSYSMODULID(@Query("sysModuleId") long sysModuleId);


    //5.上报问题
    @Multipart
//    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/alarm/doaddalarm")
    Call<String> DOADDALARM_CALL(@Field("userId") long userId,
                                 @Field("monitorPointId") long monitorPointId,
                                 @Field("addContent") String addContent,
                                 @Field("addLocation") String addLocation,
                                 @Field("addCategoryId") long addCategoryId,
                                 @Field("longitude") double longitude,
                                 @Field("latitude") double latitude,
//                                 @Field("alarmImgForAndroid") String[] alarmImgForAndroid
                                 @Part MultipartBody.Part uploadFile1,
                                 @Part MultipartBody.Part uploadFile2,
                                 @Part MultipartBody.Part uploadFile3,
                                 @Part MultipartBody.Part uploadFile4,
                                 @Part MultipartBody.Part uploadFile5
                                );


//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/alarm/doaddalarm")
    Call<String> DOADDALARM_CALL1(@Body RequestBody body);
    //6.问题列表
    @POST("do/m/alarm/alarmlist/query")
    Call<String> ALARMLIST_CALL(@Query("userId") long userId,
                                @Query("status") int status,
                                @Query("start") long start,
                                @Query("count") long count);
    //6.问题列表
    @POST("do/m/alarm/alarmlist/query")
    Call<String> ALARMLIST_CALL1(@Query("userId") long userId,
                                @Query("status") int status,
                                @Query("start") long start,
                                @Query("count") long count,
                                @Query("startTime") String startTime,
                                @Query("endTime") String endTime,
                                @Query("location") String location);
    //7.问题详情
    @POST("do/m/alarm/alarminfo/query")
    Call<String> QUERYPRODETAIL_CALL(@Query("alarmId") long alarmId);

    //11.问题流程
    @POST("do/m/alarm/log/query")
    Call<String> QUERYPROGRESS_CALL(@Query("alarmId") long alarmId);

    //12.查询分配信息
    @POST("do/m/alarm/property/query")
    Call<String> QUERYDISTRUBITE_CALL(@Query("alarmId") long alarmId, @Query("userId") long userId);

    //分配问题
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/alarm/sendmlist")
    Call<String> SENDMLIST_CALL(@Field("userId") long userId, @Field("propertyId") long propertyId, @Field("alarmId") long alarmId, @Field("postil") String postil);

    //确认处理中问题
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/alarm/alarm/confirm")
    Call<String> CONFIRM_CALL(@Field("userId") long userId,
                              @Field("alarmId") long alarmId,
                              @Field("confirmResult") int confirmResult,
                              @Field("confirmContent") String confirmContent,
                              @Field("alarmImgForAndroid") String[] alarmImgForAndroid);

    @POST("do/m/alarm/alarm/confirm")
    Call<String> CONFIRM_CALL1(@Body RequestBody body);
    //二级待处理
    @POST("do/m/mlist/mlist/query")
    Call<String> DEALLIST_CALL(@Query("userId") long userId, @Query("start") long start, @Query("count") long count);

    //二级接单
    @POST("do/m/mlist/receive")
    Call<String> RECEIVE_CALL(@Query("userId") long userId, @Query("mlistId") long mlistId);

    //标注问题
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/alarm/mark")
    Call<String> MARK_CALL(@Field("updateBy") long userId, @Field("alarmId") long alarmId, @Field("mark") String mark);

    //12.1 网格员处理
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/mlist/deal/submit")
    Call<String> SUBMIT_CALL(@Field("updateBy") long updateBy, @Field("id") long id, @Field("dealContent") String dealContent);

    //12.2 网格员上报
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/mlist/report")
    Call<String> REPORT_CALL(@Field("updateBy") long updateBy, @Field("id") long id, @Field("visitContent") String visitContent);

    //13.首页基本信息
    @POST("do/m/alarm/pointBaseInfo")
    Call<String> BASEINFO_CALL(@Query("userId") long userId);

    //20.近一年问题变化数
    @POST("do/m/alarm/linechart/query")
    Call<String> LINECHART_CALL(@Query("userId") long userId);

    //21.各类问题
    @POST("do/m/alarm/categoryalarm/query")
    Call<String> CATEALARM_CALL(@Query("userId") long userId, @Query("type") int type);

    @POST("do/m/resident/toadd")
    Call<String> TOADD_CALL(@Query("userId") long userId);


    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/resident/add")
    Call<String> ADD_RESIDENT_CALL(@Field("userId") long userId,
                                   @Field("name") String name,
                                   @Field("gender") int gender,
                                   @Field("nation") String nation,
                                   @Field("native_place") String native_place,
                                   @Field("cultural_level") String cultural_level,
                                   @Field("political_status") String political_status,
                                   @Field("marriage_status") String marriage_status,
                                   @Field("military_service") String military_service,
                                   @Field("disability_category") String disability_category,
                                   @Field("city_subsistence_allowances") String city_subsistence_allowances,
                                   @Field("volunteer") String volunteer,
                                   @Field("household_registration") String household_registration,
                                   @Field("current_address") String current_address,
                                   @Field("job_status") String job_status,
                                   @Field("working_place") String working_place,
                                   @Field("car_number") String car_number,
                                   @Field("contact_information") String contact_information,
                                   @Field("remarks") String remarks,
                                   @Field("id_type") int id_type,
                                   @Field("id_number") String id_number,
                                   @Field("communityid") long communityid,
                                   @Field("birthday") int birthday,
                                   @Field("relationship") String relationship,
                                   @Field("roomNumber") String roomNumber,
                                   @Field("gardenName") String gardenName,
                                   @Field("building") String building,
                                   @Field("unit") String unit,
                                   @Field("houseNumber") String houseNumber,
                                   @Field("humanType") String humanType);

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/resident/add")
    Call<String> CHANGE_RESIDENT_CALL(@Field("userId") long userId, @Field("id") long id, @Field("name") String name, @Field("gender") int gender, @Field("nation") String nation, @Field("native_place") String native_place, @Field("cultural_level") String cultural_level, @Field("political_status") String political_status, @Field("marriage_status") String marriage_status, @Field("military_service") String military_service, @Field("disability_category") String disability_category, @Field("city_subsistence_allowances") String city_subsistence_allowances, @Field("volunteer") String volunteer, @Field("household_registration") String household_registration, @Field("current_address") String current_address, @Field("job_status") String job_status, @Field("working_place") String working_place, @Field("car_number") String car_number, @Field("contact_information") String contact_information, @Field("remarks") String remarks, @Field("id_type") int id_type, @Field("id_number") String id_number, @Field("communityid") long communityid, @Field("birthday") int birthday, @Field("relationship") String relationship, @Field("roomNumber") String roomNumber, @Field("gardenName") String gardenName, @Field("building") String building, @Field("unit") String unit, @Field("houseNumber") String houseNumber, @Field("humanType") String humanType);


    @POST("do/m/resident/querylist")
    Call<String> QUERY_RESIDENT(@Query("type") int type, @Query("number") String number, @Query("name") String name, @Query("roomNumber") String room, @Query("userId") long userId);

    @POST("do/m/resident/query")
    Call<String> DETAIL_RESIDENT(@Query("id") long id);

    @POST("do/m/check/checkstart")
    Call<String> CHECK_START_CALL(@Query("userId") long userId);

    @POST("do/m/check/position/update")
    Call<String> UPDATE_POSITION_CALL(@Query("userId") long userId, @Query("longitude") double longitude, @Query("latitude") double latitude, @Query("acturalRecordId") long acturalRecordId);

    @POST("do/m/check/checkend")
    Call<String> CHECK_END_CALL(@Query("userId") long userId, @Query("actualCheckId") long actualCheckId);

    @POST("do/m/check/sign")
    Call<String> SIGN_CALL(@Query("userId") long userId, @Query("addressId") long addressId, @Query("actualCheckId") long actualCheckId, @Query("longitude") double longitude, @Query("latitude") double latitude);

    @POST("do/m/check/checkstatus")
    Call<String> CHECK_STATUS_CALL(@Query("userId") long userId);


    @POST("do/m/resident/check/confirm")
    Call<String> CONFIRM_CHECK(@Query("userId") long userId, @Query("checkCode") String checkCode);

    @POST("do/m/check/mycheck/list")
    Call<String> CHECK_LIST(@Query("userId") long userId);

    //查询工单
    @POST("do/m/mlist/mlist/query")
    Call<String> Maintain_LIST(@Query("userId") long userId,
                               @Query("status") int status,
                               @Query("content") String content,
                               @Query("start") int start,
                               @Query("count") int count,
                               @Query("type") int type);
    //查询工单
    @POST("do/m/mlist/mlist/query")
    Call<String> Maintain_LIST1(@Query("userId") long userId,
                               @Query("status") int status,
                               @Query("content") String content,
                               @Query("start") int start,
                               @Query("count") int count,
                               @Query("type") int type,
                                @Query("startTime") String startTime,
                                @Query("endTime") String endTime,
                                @Query("location") String location);

    //申请驳回
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/mlist/back/apply")
    Call<String> MAINTAIN_BACK(@Field("userId") long userId, @Field("mlistId") long mlistId, @Field("content") String content);

    //申请无效
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/mlist/invalid/apply")
    Call<String> MAINTAIN_INVALID(@Field("userId") long userId, @Field("mlistId") long mlistId, @Field("content") String content);

    //处理工单
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/mlist/deal/submit")
    Call<String> MAINTAIN_DEAL(@Field("actUserId") long userId,
                               @Field("id") long mlistId,
                               @Field("dealContent") String content,
                               @Field("visitContentForAndroid") String[] visitContentForAndroid);
    @POST("do/m/mlist/deal/submit")
    Call<String> MAINTAIN_DEAL1(@Body RequestBody body);
    //申请延期
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //添加
    @POST("do/m/mlist/delay/apply")
    Call<String> MAINTAIN_DELAY(@Field("userId") long userId, @Field("mlistId") long mlistId, @Field("content") String content, @Field("num") long num);

    //工单详细
    @POST("do/m/mlist/mlistdetail")
    Call<String> MAINTAIN_DETAIL(@Query("mlistId") long mlistId);

    //告警问题
    @POST("do/m/alarm/warnList")
    Call<String> WARNALARM_LIST(@Query("userId") long userId);

    //查询图片
    @POST("do/m/alarm/queryImg")
    Call<String> QUERYPIC(@Query("picId") long picId);
}
