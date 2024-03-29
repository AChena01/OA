<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 刘怡琛
  Date: 2020/5/19
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/pages/top.jsp" %>

<section id="content" class="table-layout animated fadeIn">
    <div class="tray tray-center">
        <div class="content-header">
            <h2> 员工列表 </h2>
            <p class="lead"></p>
        </div>
        <div class="admin-form theme-primary mw1000 center-block" style="padding-bottom: 175px;">
            <div class="panel  heading-border">
                <div class="panel-menu">

                    <div class="row">
                        <div class="hidden-xs hidden-sm col-md-3">
                            <div class="btn-group">
                                <button type="button" class="btn btn-default light">
                                    <i class="fa fa-refresh"></i>
                                </button>
                                <button type="button" class="btn btn-default light">
                                    <i class="fa fa-trash"></i>
                                </button>
                                <button type="button" class="btn btn-default light">
                                    <i class="fa fa-plus"
                                       onclick="javascript:window.location.href='${pageContext.request.contextPath}/employee/to_add';"></i>
                                </button>
                            </div>
                        </div>
                        <div class="col-xs-12 col-md-9 text-right">
                            <div class="btn-group">
                                <button type="button" class="btn btn-default light">
                                    <i class="fa fa-chevron-left"></i>
                                </button>
                                <button type="button" class="btn btn-default light">
                                    <i class="fa fa-chevron-right"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-body pn">
                    <table id="message-table" class="table admin-form theme-warning tc-checkbox-1">
                        <thead>
                        <tr class="">
                            <th class="text-center hidden-xs">选择</th>
                            <th class="hidden-xs">账户名</th>
                            <th class="hidden-xs">姓名</th>
                            <th class="hidden-xs">所属单位及部门</th>
                            <th class="hidden-xs">职务</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${list}" var="emp">
                            <tr class="message-unread">
                                <td class="hidden-xs">
                                    <label class="option block mn">
                                        <input type="checkbox" name="mobileos" value="FR">
                                        <span class="checkbox mn"></span>
                                    </label>
                                </td>
                                <td>${emp.sn}</td>
                                <td>${emp.name}</td>
                                <td class="text-center fw600">${emp.department.sn}-${emp.department.name}</td>
                                <td class="hidden-xs">
                                    <span class="badge badge-warning mr10 fs11">${emp.post}</span>
                                </td>
                                <td>
                                    <c:if test="${sessionScope.employee.post == Contant.POST_ADMIN}">
                                        <a href="${pageContext.request.contextPath}/employee/to_update?sn=${emp.sn}">编辑</a>
                                        <a onclick="if (confirm('是否删除此员工')){
                                                location.href = '${pageContext.request.contextPath}/employee/remove?sn=${emp.sn}';
                                                }else {
                                                location.href = '${pageContext.request.contextPath}/employee/list';
                                                }">删除</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>


<%@ include file="/WEB-INF/pages/bottom.jsp" %>