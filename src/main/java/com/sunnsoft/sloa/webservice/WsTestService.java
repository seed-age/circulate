package com.sunnsoft.sloa.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;


/**
 * webservice 示例 ，详细配置请参考web.xml配置和cxf-servlet.xml配置。
 * @author llade
 *
 */
@WebService
public interface WsTestService {

	public String test(@WebParam(name = "pass") String pass, @WebParam(name = "param1") String param1);
}
