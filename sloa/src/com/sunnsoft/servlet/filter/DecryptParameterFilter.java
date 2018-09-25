package com.sunnsoft.servlet.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sunnsoft.util.encrypt.Base64Utils;
import com.sunnsoft.util.encrypt.RSAUtils;

/**
 * 
 * @author llade
 * 针对部分参数进行解密的过滤器，需要配合js前端加密工具JSEncrypt使用。可以规避运营商需要强制使用Https的检查。
 */
public class DecryptParameterFilter implements Filter {
	
	private static final Log logger = LogFactory.getLog(DecryptParameterFilter.class); 

	private class RequestWrapper extends HttpServletRequestWrapper {
		
		HttpServletRequest request;
		
		public RequestWrapper(HttpServletRequest request) {
			super(request);
			this.request = request;
		}

		@Override
		public String getParameter(String name) {
			String returnValue = this.request.getParameter(name);
			if(name != null && ArrayUtils.contains(params, name)){
				try {
					returnValue = decrypt(returnValue);
				} catch (UnsupportedEncodingException e) {
					if(logger.isErrorEnabled()){
						logger.error(e);
					}
				} catch (Exception e) {
					if(logger.isErrorEnabled()){
						logger.error(e);
					}
				}
			}
			return returnValue;
		}
		
	}
	
	protected String[] params;
	
	protected String privateKey;
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		chain.doFilter(new RequestWrapper(request), resp);
	}

	private String decrypt(String string) throws UnsupportedEncodingException, Exception {
		String result = new String(RSAUtils.decryptByPrivateKey(Base64Utils.decode(string), this.privateKey),"utf-8");
		if(logger.isDebugEnabled()){
		 logger.debug("decrypted result:" + result);
		}
		return result;
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		String names = cfg.getInitParameter("decryptParamNames");
		privateKey = cfg.getInitParameter("privateKey");
		if(StringUtils.isEmpty(privateKey)){
			throw new ServletException("privateKey　不能为空");
		}
		privateKey = privateKey.trim();
		if(StringUtils.isEmpty(names)){
			throw new ServletException("decryptParamNames　不能为空");
		}
		names =  names.trim();
		this.params = names.split(",");
		
	}
	
	

}
