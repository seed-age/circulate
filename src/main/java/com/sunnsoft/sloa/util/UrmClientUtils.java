package com.sunnsoft.sloa.util;

import cc.seedland.sdk.exceptions.ClientException;
import cc.seedland.sdk.urm.UrmClient;
import cc.seedland.sdk.urm.model.SendSmsRequest;
import cc.seedland.sdk.urm.model.SendSmsResponse;
import com.alibaba.fastjson.JSON;
import com.sunnsoft.sloa.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName UrmClientUtils
 * @Description: 实地短信接口相关
 * @Auther: chenjian
 * @Date: 2019/1/3 14:01
 * @Version: 1.0
 **/
public class UrmClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrmClientUtils.class);

    /**
     * @Author chenjian
     * @Description 初始化 UrmClient
     * @Date 2019/1/3 15:02
     * @Param
     * @return
     **/
    public static UrmClient getUrmClient(Config config){
        LOGGER.warn("================================== 进行初始化 短信对象 =========================================");
        String host = config.getUrmUrl(); //urm平台域名
        String key = config.getUrmKey(); //平台分配的MD5字符串
        UrmClient urmClient = new UrmClient(host,key);
        LOGGER.warn("================================== 初始化完成 =========================================");
        return urmClient;
    }

    /**
     * @Author chenjian
     * @Description 调用实地短信SDK 进行发送短信
     * @Date 2019/1/3 14:11
     * @phones 手机号码以英文逗号分隔，不超过50个号码
     * @return
     **/
    public static void getSendSms(UrmClient urmClient, List<String> phoneList, Config config, String param){
        try {
            if (phoneList.size() > 0) {
//                String mobiles = "";
//                int num = 0;
                for (String mobile : phoneList) {
//                    num++;
                    getSendUrm(urmClient, mobile, config, param);

//                    mobiles += mobile + ",";
//                    if (num == 50) {
//                        LOGGER.warn("================================== 调用发送短信的接口  进行批量发送短信 =========================================");
//                        LOGGER.warn("num: " + num);
//                        LOGGER.warn("mobiles: " + mobiles);
//                        mobiles = mobiles.substring(0, mobiles.length() - 1);
//                        getSendUrm(urmClient, mobiles, config, param);
//                        num = 0;
//                        mobiles = "";
//                        LOGGER.warn("================================== 批量发送短信完成 =========================================");
//                        LOGGER.warn("num: " + num);
//                        LOGGER.warn("mobiles: " + mobiles);
//                    }
                }

//                if (num > 0) {
//                    LOGGER.warn("================================== 一次发送短信不足五十个 =========================================");
//                    LOGGER.warn("mobiles: " + mobiles);
//                    mobiles = mobiles.substring(0, mobiles.length() - 1);
//                    getSendUrm(urmClient, mobiles, config, param);
//                    LOGGER.warn("================================== 发送短信完成 =========================================");
//                    LOGGER.warn("mobiles: " + mobiles);
//                }
            }
        } catch (ClientException e) {
            e.printStackTrace();
            LOGGER.warn("================================== 调用短信失败 =========================================");
        }
    }

    private static void getSendUrm(UrmClient urmClient, String phones, Config config, String param) throws ClientException {
        urmClient.setConnectTimeout(15000);  //http连接超时时间（单位：毫秒），默认：15000ms
        urmClient.setReadTimeout(30000);   //http读超时时间（单位：毫秒），默认：30000ms
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhone(phones); // 短信接收号码  必传
        sendSmsRequest.setTplId(config.getUrmTplId()); // 短信模板ID  必传
        sendSmsRequest.setParam(param); // 短信模板变量 可选
        SendSmsResponse resp = urmClient.sendSms(sendSmsRequest);

        LOGGER.warn("SendSmsResponse: " + JSON.toJSONString(resp));

        switch (resp.getCode()) {
            case 0 :  // 发送成功
                LOGGER.warn("================================== 批量发送短信成功 =========================================");
                break;
            case 100 : // IP不在白名单
                LOGGER.warn("================================== IP不在白名单 =========================================");
                break;
            case 110 : // 签名错误
                LOGGER.warn("================================== 签名错误 =========================================");
                break;
            case 120 : // 每日短信总数超过限制
                LOGGER.warn("================================== 每日短信总数超过限制 =========================================");
                break;
            case 130 : // 参数不正确
                LOGGER.warn("==================================  参数不正确 =========================================");
                break;
            case 140 : // 手机号码格式错误
                LOGGER.warn("================================== 手机号码格式错误 =========================================");
                break;
            case 150 : // 手机号码数量超过限制
                LOGGER.warn("================================== 手机号码数量超过限制 =========================================");
                break;
            case 160 : // 短信模板变量不正确
                LOGGER.warn("================================== 短信模板变量不正确 =========================================");
                break;
            case 170 : // 模版不存在
                LOGGER.warn("================================== 模版不存在 =========================================");
                break;
            case 200 : // 短信渠道流控限制
                LOGGER.warn("================================== 短信渠道流控限制 =========================================");
                break;
            case 300 : // 短信渠道错误
                LOGGER.warn("================================== 短信渠道错误 =========================================");
                break;
            case 500 : // 系统错误
                LOGGER.warn("================================== 系统错误 =========================================");
                break;

        }
    }


}
