package secappdev.oauth2.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
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

    public static String getLoginUrl(HttpServletRequest request) {
        return "TBD";
    }

    public static String retrieveAccessToken(HttpServletRequest request) {
        return "TBD";
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
