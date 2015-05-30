<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false" isErrorPage="true"%>

<%@page import="${basePackageName}.common.util.Config"%>

<%
  String homeDomain = Config.getConfig().get("home.domain");
  response.sendRedirect("http://" + homeDomain + "/error");
%>
