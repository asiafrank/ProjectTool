<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false" isErrorPage="true"%>

<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="${basePackageName}.common.util.Config"%>
<%@page import="${basePackageName}.common.web.util.RequestUtils"%>

<%
  Throwable ex = null;
  if (exception != null) {
    ex = exception;
  } else if (request.getAttribute("javax.servlet.error.exception") != null) {
    ex = (Exception) request.getAttribute("javax.servlet.error.exception");
  }

  Logger logger = LoggerFactory.getLogger("500.jsp");
  if (ex != null) {
    logger.error("Request Info: \n" + RequestUtils.getRequestInfo(request) + "\nException: " + ex.getMessage(), ex);
  } else {
    logger.error("Request Info: \n" + RequestUtils.getRequestInfo(request));
  }

  String homeDomain = Config.getConfig().get("home.domain");
  response.sendRedirect("http://" + homeDomain + "/error");
%>
