<%@ page import="secappdev.oauth2.util.OAuth2Util" %>
<!doctype html>
<%
    String accessToken = OAuth2Util.retrieveAccessToken(request);
%>
<html>
<head>
    <title>OAuth 2.0 Authorization Code Flow</title>
</head>
<body>
    <h1>OAuth 2.0 Authorization Code Grant Flow</h1>
    Access token: <%= accessToken %>.
</body>
</html>