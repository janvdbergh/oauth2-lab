<%@ page import="secappdev.oauth2.util.OAuth2Util" %>
<!doctype html>
<html>
<head>
    <title>OAuth 2.0 Authorization Code Flow</title>
</head>
<body>
    <h1>OAuth 2.0 Authorization Code Flow</h1>
    <a href="<%= OAuth2Util.getLoginUrl(request) %>">Log in</a>.
</body>

</html>