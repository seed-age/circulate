package com.sunnsoft;

import com.sunnsoft.util.SpringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author huangkaibin
 * @date 2018-09-29
 **/
@Service
public class ThirdPartyConfiguration {

    @Value("${oa.emessage.url}")
    private String oaEmobileUrl;
    @Value("${oa.emessage.key}")
    private String oaEmobileKey;
    @Value("${oa.emessage.typeid")
    private String oaEmobileTypeid;

    @Value("${oa.hrm.push.url}")
    private String oaHrmPushUrl;
    @Value("${oa.hrm.ws.url}")
    private String oaHrmWsUrl;
    @Value("oa.hrm.scheduler.url")
    private String oaHrmSchedulerUrl;

    @Value("oa.oss.url")
    private String oaOssUrl;

    public static String getOaEmobileUrl() {
        return SpringUtils.getBean(ThirdPartyConfiguration.class).oaEmobileUrl;
    }

    public void setOaEmobileUrl(String oaEmobileUrl) {
        this.oaEmobileUrl = oaEmobileUrl;
    }

    public static String getOaEmobileKey() {
        return SpringUtils.getBean(ThirdPartyConfiguration.class).oaEmobileKey;
    }

    public void setOaEmobileKey(String oaEmobileKey) {
        this.oaEmobileKey = oaEmobileKey;
    }

    public static String getOaEmobileTypeid() {
        return SpringUtils.getBean(ThirdPartyConfiguration.class).oaEmobileTypeid;
    }

    public void setOaEmobileTypeid(String oaEmobileTypeid) {
        this.oaEmobileTypeid = oaEmobileTypeid;
    }

    public static String getOaHrmPushUrl() {
        return SpringUtils.getBean(ThirdPartyConfiguration.class).oaHrmPushUrl;
    }

    public void setOaHrmPushUrl(String oaHrmPushUrl) {
        this.oaHrmPushUrl = oaHrmPushUrl;
    }

    public static String getOaHrmWsUrl() {
        return SpringUtils.getBean(ThirdPartyConfiguration.class).oaHrmWsUrl;
    }

    public void setOaHrmWsUrl(String oaHrmWsUrl) {
        this.oaHrmWsUrl = oaHrmWsUrl;
    }

    public static String getOaHrmSchedulerUrl() {
        return SpringUtils.getBean(ThirdPartyConfiguration.class).oaHrmSchedulerUrl;
    }

    public void setOaHrmSchedulerUrl(String oaHrmSchedulerUrl) {
        this.oaHrmSchedulerUrl = oaHrmSchedulerUrl;
    }

    public static String getOaOssUrl() {
        return SpringUtils.getBean(ThirdPartyConfiguration.class).oaOssUrl;
    }

    public void setOaOssUrl(String oaOssUrl) {
        this.oaOssUrl = oaOssUrl;
    }

    @Override
    public String toString() {
        return "ThirdPartyConfiguration{" +
                "oaEmobileUrl='" + oaEmobileUrl + '\'' +
                ", oaEmobileKey='" + oaEmobileKey + '\'' +
                ", oaEmobileTypeid='" + oaEmobileTypeid + '\'' +
                ", oaHrmPushUrl='" + oaHrmPushUrl + '\'' +
                ", oaHrmWsUrl='" + oaHrmWsUrl + '\'' +
                ", oaHrmSchedulerUrl='" + oaHrmSchedulerUrl + '\'' +
                ", oaOssUrl='" + oaOssUrl + '\'' +
                '}';
    }
}
