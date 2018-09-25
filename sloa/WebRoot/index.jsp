<%@page import="org.springframework.security.web.util.UrlUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%
response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
String redirectUrl =  UrlUtils.buildFullRequestUrl(request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath() + "/web/index.htm", request.getQueryString());
response.setHeader("Location",redirectUrl);   
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setHeader("expires", "0");
%>
