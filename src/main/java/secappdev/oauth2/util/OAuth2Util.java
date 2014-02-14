package secappdev.oauth2.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OAuth2Util {

    private static final String AUTHORIZATION_ENDPOINT = "https://accounts.google.com/o/oauth2/auth";
    private static final String TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token";
    private static final String REDIRECT_URI = "http://localhost:8080/oauth2-authorization-code/callback.jsp";
    private static final String SCOPE = "https://www.google.com/m8/feeds";
    private static final String CLIENT_ID = "CLIENT_ID";
    private static final String CLIENT_SECRET = "CLIENT_SECRET";

    private static final String SESSION_OAUTH_2_STATE = "oauth2_state";

    public static String getLoginUrl(HttpServletRequest request) {
        String state = RandomStringUtils.randomAlphanumeric(32);
        request.getSession().setAttribute(SESSION_OAUTH_2_STATE, state);

        return AUTHORIZATION_ENDPOINT +
                "?response_type=code" +
                "&client_id=" + encode(CLIENT_ID) +
                "&redirect_uri=" + encode(REDIRECT_URI) +
                "&scope=" + encode(SCOPE) +
                "&state=" + encode(state);
    }

    public static String retrieveAccessToken(HttpServletRequest request) {
        String state = request.getParameter("state");
        String originalState = (String) request.getSession().getAttribute(SESSION_OAUTH_2_STATE);
        if (!StringUtils.equals(state, originalState)) {
            // handle invalid state
            return null;
        }

        if (request.getParameter("error") != null) {
            // handle error
            return null;
        }

        String authorizationCode = request.getParameter("code");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", CLIENT_ID);
        parameters.put("client_secret", CLIENT_SECRET);
        parameters.put("redirect_uri", REDIRECT_URI);
        parameters.put("grant_type", "authorization_code");
        parameters.put("code", authorizationCode);
        String response = sendHttpPost(TOKEN_ENDPOINT, parameters);

        Map<String, String> responseMap = parseJson(response);
        return responseMap.get("access_token");
    }

    private static String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private static String sendHttpPost(String url, Map<String, String> parameters) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        List<NameValuePair> nameValueParameters = new ArrayList<>();
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            nameValueParameters.add(new BasicNameValuePair(parameter.getKey(), parameter.getValue()));
        }

        try {
            post.setEntity(new UrlEncodedFormEntity(nameValueParameters));
            HttpResponse httpResponse = httpClient.execute(post);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            httpResponse.getEntity().writeTo(byteArrayOutputStream);
            return byteArrayOutputStream.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> parseJson(String json) {
        Map<String, String> result = new HashMap<>();

        if (StringUtils.isBlank(json)) {
            return result;
        }

        JsonObject jsonObject = (JsonObject) new JsonParser().parse(json);
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getAsString());
        }

        return result;
    }
}
