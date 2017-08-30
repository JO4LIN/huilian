package com.shengrui.huilian.base_mvp.Global;

/**
 * Created by zhrx on 2016/4/9.
 */
public class Config {
    public static boolean isLogin=false;
    public final static String KEY_STATUS = "CAllBACK_STATUS";
    public final static String KEY_TOTAL_COUNT = "TOTAL_COUNT";
    public final static String KEY_DATA = "GET_ARRAY_DATA";

    public final static String KEY_PAGE_NOW = "KEY_PAGE_NOW";
    public final static int PAGE_SAME_SIZE = 6;
    public final static String KEY_PAGE_SIZE = "KEY_PAGE_SIZE";

    public final static String PAGE_NUMNER = "PAGE_NUMNER";
    public final static String PHONE = "PHONE";
    public final static String USERNAME = "USERNAME";
    public final static String PASSWORD = "PASSWORD";
    public final static String CITY = "CITY";
    public final static String KEY_SHOP_TYPE = "KEY_SHOP_TYPE";
    public final static String SERVER_GET_ACTIVITY_LIST_URL = "http://192.168.191.11/active/onActive";

    public final static int STATUS_SUCCESS = 100;
    public static final int STATUS_NO_FAILURE = -100;
    public final static int STATUS_NO_LOGIN = -500;
    public static final int STATUS_NO_STORE = -100;

    public final static int SHOP_TYPE_SUPERMARKET = 3;

    //推荐
    public final static String lin = "http://120.25.153.22/mediaInfo/mediaIdAscJson";
    //搜索
    public final static String search = "http://120.25.153.22/mediaInfo/search";

    public final static String mediaType = "http://120.25.153.22/mediaInfo/chooseMediaType";
    public final static String mediaTypeFans = "http://120.25.153.22/mediaClass/fansNumClassListJson";
    public final static String mediaTyoeRead = "http://120.25.153.22/mediaClass/readNumClassListJson";
    public final static String mediaTypePrice = "http://120.25.153.22/mediaClass/priceClassListJson";

    public final static String mediaSchool = "http://120.25.153.22/mediaInfo/chooseMediaSchool";
    public final static String mediaSchoolFans = "http://120.25.153.22/schoolMedia/fansNumSchoolListJson";
    public final static String mediaSchoolRead = "http://120.25.153.22/schoolMedia/readNumSchoolListJson";
    public final static String mediaSchoolPrice = "http://120.25.153.22/schoolMedia/priceSchoolListJson";
//    public final static String lin = "http://192.168.253.5/indent/onActive";
    //
    public final static String areaSchool ="http://120.25.153.22/schoolMedia/schoolNameJson";
    public final static String MediaInfo="http://120.25.153.22/mediaInfo/refresh";
    public final static String RefreshIndentURL="http://120.25.153.22/indentInfo/refresh";
    public final static String SendIndentURL="http://120.25.153.22/indentInfo/send";

    public final static String CheckIndentURL="http://120.25.153.22/indentInfo/proIndent";
    public final static String CheckTaskURL="http://120.25.153.22/indentInfo/proTask";
    public final static String FindTaskInfoURL="http://120.25.153.22/indentInfo/checkTask";
    public final static String FindIndentInfoURL="http://120.25.153.22/indentInfo/checkIndent";
    public final static String UpdateProgressURL="http://120.25.153.22/indentInfo/progress";
    public final static String UserInfoURL="http://120.25.153.22/userInfoChange/refresh";
    public final static String UpdateUserInfoURL="http://120.25.153.22/userInfoChange/update";
    public final static String UpdateUserPhotoURL="http://120.25.153.22/userInfoChange/loadphoto";

    public final static String UpdateMediaInfoURL="http://120.25.153.22/mediaInfo/update";
    public final static String UpdateMediaPhotoURL="http://120.25.153.22/mediaInfo/loadphoto";
    public final static String UpdateMediaCodeURL="http://120.25.153.22/mediaInfo/loadcode";
    public final static String GetSchoolURL="http://120.25.153.22/schoolMedia/AreaSchoolName";

    public final static String CollectUserURL="http://120.25.153.22/userInfoChange/collectUser";
    public final static String CollectMediaURL="http://120.25.153.22/mediaInfo/collectMedia";
    public final static String CancelcollectUserURL="http://120.25.153.22/userInfoChange/delCollectUser";
    public final static String CancelcollectMediaURL="http://120.25.153.22/mediaInfo/delCollectMedia";
    public final static String CollectUserListURL="http://120.25.153.22/userInfoChange/collectUserList";
    public final static String CollectMediaListURL="http://120.25.153.22/mediaInfo/collectMediaList";
    public final static String ScreenMediaURL="http://120.25.153.22/mediaClass/Screen";

    public final static String NewsListURL="http://120.25.153.22/newsClass/activeNews";
    public final static String SendFeedBackURL="http://120.25.153.22/feedBack/saveFeedBack";

    public final static String EnterMediaURL="http://120.25.153.22/mediaEnter/enterMedia";
    public final static String CheckEnterMediaURL="http://120.25.153.22/mediaEnter/initMedia";

    public final static String EnterMediaListURL="http://120.25.153.22/mediaEnter/mediaList";

    public final static String LoginURL="http://120.25.153.22/userEnter/login";
    public final static String RegisterURL="http://120.25.153.22/userEnter/register";
    public final static String ForgetPasswordURL="http://120.25.153.22/userEnter/forgetPassword";
    public final static String GetCodeURL="http://120.25.153.22/userEnter/sendCode";
    public final static String CheckMobileRegURL="http://120.25.153.22/userEnter/checkMobileRegister";
    public final static String CheckMobileForURL="http://120.25.153.22/userEnter/checkMobileForget";

    public final static String AutoLoginURL="http://120.25.153.22/userInfoChange/autoLogin";

    public final static String ReportUserURL="http://120.25.153.22/report/saveReportUser";
    public final static String ReportMediaURL="http://120.25.153.22/report/saveReportMedia";

    public final static String HotCityMediaURL="http://120.25.153.22/cityMedia/hostMediaList";
    public final static String ScreenCityListURL="http://120.25.153.22/cityMedia/screenCityList";
    //OSS的Bucket
    public static final String OSS_BUCKET = "cxqtest";
    //设置OSS数据中心域名或者cname域名
    public static final String OSS_BUCKET_HOST_ID = "oss-cn-shenzhen.aliyuncs.com";
    //Key
    public final static String accessKey = "S9Ls3bj0CrzBd1HT";
    public final static String screctKey = "ig1Q0WzzBRT1diKND71cqGQkB08XUm";
}
