package com.sunnsoft;

import com.sunnsoft.util.SpringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author huangkaibin
 * @date 2018-09-29
 **/
public class ThirdPartyConfiguration {

    public static class Configuration {

        @Value("${oa.emessage.url}")
        private String oaEmobileUrl;
        @Value("${oa.emessage.key}")
        private String oaEmobileKey;
        @Value("${oa.emessage.typeid}")
        private String oaEmobileTypeid;

        @Value("${oa.hrm.push.url}")
        private String oaHrmPushUrl;
        @Value("${oa.hrm.ws.url}")
        private String oaHrmWsUrl;
        @Value("${oa.hrm.scheduler.url}")
        private String oaHrmSchedulerUrl;

        @Value("${oa.oss.url}")
        private String oaOssUrl;

        public String getOaEmobileUrl() {
            return oaEmobileUrl;
        }

        public void setOaEmobileUrl(String oaEmobileUrl) {
            this.oaEmobileUrl = oaEmobileUrl;
        }

        public String getOaEmobileKey() {
            return oaEmobileKey;
        }

        public void setOaEmobileKey(String oaEmobileKey) {
            this.oaEmobileKey = oaEmobileKey;
        }

        public String getOaEmobileTypeid() {
            return oaEmobileTypeid;
        }

        public void setOaEmobileTypeid(String oaEmobileTypeid) {
            this.oaEmobileTypeid = oaEmobileTypeid;
        }

        public String getOaHrmPushUrl() {
            return oaHrmPushUrl;
        }

        public void setOaHrmPushUrl(String oaHrmPushUrl) {
            this.oaHrmPushUrl = oaHrmPushUrl;
        }

        public String getOaHrmWsUrl() {
            return oaHrmWsUrl;
        }

        public void setOaHrmWsUrl(String oaHrmWsUrl) {
            this.oaHrmWsUrl = oaHrmWsUrl;
        }

        public String getOaHrmSchedulerUrl() {
            return oaHrmSchedulerUrl;
        }

        public void setOaHrmSchedulerUrl(String oaHrmSchedulerUrl) {
            this.oaHrmSchedulerUrl = oaHrmSchedulerUrl;
        }

        public String getOaOssUrl() {
            return oaOssUrl;
        }

        public void setOaOssUrl(String oaOssUrl) {
            this.oaOssUrl = oaOssUrl;
        }

        @Override
        public String toString() {
            return "Configuration{" +
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

    public static String getOaEmobileUrl() {
        return SpringUtils.getBean(Configuration.class).getOaEmobileUrl();
    }

    public static String getOaEmobileKey() {
        return SpringUtils.getBean(Configuration.class).getOaEmobileKey();
    }

    public static String getOaEmobileTypeid() {
        return SpringUtils.getBean(Configuration.class).getOaEmobileTypeid();
    }

    public static String getOaHrmPushUrl() {
        return SpringUtils.getBean(Configuration.class).getOaHrmPushUrl();
    }

    public static String getOaHrmWsUrl() {
        return SpringUtils.getBean(Configuration.class).getOaHrmWsUrl();
    }

    public static String getOaHrmSchedulerUrl() {
        return SpringUtils.getBean(Configuration.class).getOaHrmSchedulerUrl();
    }

    public static String getOaOssUrl() {
        Configuration thirdPartyConfiguration = SpringUtils.getBean(Configuration.class);
        return thirdPartyConfiguration.getOaOssUrl();
    }
}
