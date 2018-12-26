package com.sunnsoft.sloa.actions.web.received;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @ClassName findMailContent
 * @Description: 根据mailId单独查询传阅内容返回给前端
 * @Auther: chenjian
 * @Date: 2018/12/26 15:16
 * @Version: 1.0
 **/
public class findMailContent extends BaseParameter {

    private Long userId; // 用户ID
    private Long mailId; // 传阅ID

    @Override
    public String execute() throws Exception {
        // 校验参数
        Assert.notNull(userId, "用户的ID不能为空");
        Assert.notNull(mailId, "传阅的ID不能为空");

        json = Services.getMailService().createHelper().getUserId().Eq(userId).getMailId().Eq(mailId).json().uniqueJson(new EachEntity2Map<Mail>() {
            @Override
            public void each(Mail mail, Map<String, Object> map) {
                map.clear();

                map.put("mailContent", mail.getMailContent());
            }
        });

        success = true;
        code = "200";
        msg = "查询成功";

        return Results.GLOBAL_FORM_JSON;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMailId() {
        return mailId;
    }

    public void setMailId(Long mailId) {
        this.mailId = mailId;
    }
}
