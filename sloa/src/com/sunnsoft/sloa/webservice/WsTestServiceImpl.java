package com.sunnsoft.sloa.webservice;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import com.sunnsoft.servlet.filter.BrowserIpExtractorFilter;

/**
 * webservice 示例 ，详细配置请参考web.xml配置和cxf-servlet.xml配置。反向代理后的IP地址获取请使用{@link BrowserIpExtractorFilter}
 * @author llade
 *
 */
@WebService(endpointInterface = "com.sunnsoft.easyui2.webservice.WsTestService")
public class WsTestServiceImpl implements WsTestService {
	
	private static final Log logger = LogFactory.getLog(WsTestServiceImpl.class);
	
	@Resource(name = "org.apache.cxf.jaxws.context.WebServiceContextImpl")
    private WebServiceContext context;
 
	public String getIp() {
	       try {
	           MessageContext ctx = context.getMessageContext();
	           HttpServletRequest request = (HttpServletRequest) ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
	           String ip = request.getRemoteAddr();
	           return ip;
	       } catch (Exception e) {
	    	   logger.error("获取 发送短信客户端的Ip地址 失败：" + e);
	           return null;
	       }
	}

	@Override
	public String test(String pass, String param1) {
		if(logger.isDebugEnabled()){
			logger.debug("客户端IP : " + this.getIp());
		}
		System.out.println("客户端IP : " + this.getIp());
		return "测试";
	}

}
