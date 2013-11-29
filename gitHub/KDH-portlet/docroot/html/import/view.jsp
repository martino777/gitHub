<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>



<%@ page import="java.util.List" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.portal.kernel.util.ListUtil" %>
<%@ page import="com.liferay.portal.kernel.util.Validator" %>
<%@ page import="com.liferay.portal.kernel.dao.search.ResultRow" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="javax.portlet.PortletURL"%>

<portlet:defineObjects />
<liferay-theme:defineObjects />
<%@page  import="com.liferay.portal.kernel.servlet.SessionMessages" %>



<script type="text/javascript">
			function <portlet:namespace />addImport(form) {
				form.action = '<portlet:actionURL name="addKlubs" />';

				if(form.<portlet:namespace />csvFile.value != ''){
					form.submit();
				}
			}
</script>
<%
String export = (String)request.getAttribute("export");


boolean feedback = Validator.isNotNull(export) ? true : false;


%>

<h1>Import KDH clubs</h1>
<liferay-ui:error key="error" message="Sorry, uploadovany subor nebol v poriadku" />

	<c:if test="<%=feedback %>">
		<div class="portlet-msg-info">
			<liferay-ui:message key="import prebehol v poriadku" />
		</div>
		<h2>Vysledok</h2>
		<p>
		Vysledok importu skontrolujte v <a href="/group/control_panel/manage?p_p_id=125&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&doAsGroupId=10180&refererPlid=10183&_125_struts_action=%2Fusers_admin%2Fview_users&_125_usersListView=flat-organizations&_125_keywords=&_125_advancedSearch=false&_125_andOperator=true&_125_city=&_125_countryId=0&_125_name=&_125_parentOrganizationId=0&_125_regionId=0&_125_street=&_125_zip=&_125_orderByCol=name&_125_orderByType=asc&_125_resetCur=false&_125_delta=200">Control Paneli</a>
		</p>
		<p>
			<%= export %>
			</p>
	</c:if>

<form method="post" name="<portlet:namespace />fm" onSubmit="<portlet:namespace />addImport(this); return false;" enctype="multipart/form-data">
		
		<label for="<portlet:namespace />csvFile">CSV subor: </label>
		<input type="file" name="<portlet:namespace />csvFile" id="<portlet:namespace />csvFile"  />
		
		<input type="submit" value="send" />
	
</form>
