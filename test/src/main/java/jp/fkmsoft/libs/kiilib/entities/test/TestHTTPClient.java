package jp.fkmsoft.libs.kiilib.entities.test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import jp.fkmsoft.libs.kiilib.client.KiiHTTPClient;
import jp.fkmsoft.libs.kiilib.entities.KiiContext;

/**
 * Test HTTP Client
 */
public class TestHTTPClient implements KiiHTTPClient {

    private final KiiContext mContext;

    public TestHTTPClient(KiiContext context) {
        mContext = context;
    }

    @Override
    public void sendJsonRequest(int method, String urlString, String token, String contentType, Map<String, String> headers, JSONObject body, ResponseHandler responseHandler) {
        System.out.println(toMethod(method) + " " + urlString);
        System.out.println("token=" + token);
        HttpURLConnection connection;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(toMethod(method));
            setHeaders(connection, token, contentType, headers);
            if (method == Method.POST || method == Method.PUT) {
                if (body != null) {
                    connection.setDoOutput(true);
                    writeBody(connection, body.toString());
                }
            }

            int responseCode = connection.getResponseCode();
            String etag = connection.getHeaderField("etag");
            String respBody;
            if (200 <= responseCode && responseCode < 300) {
                if (responseCode == 204) {
                    respBody = "{}";
                } else {
                    respBody = readStream(connection.getInputStream());
                }
            } else {
                respBody = readStream(connection.getErrorStream());
            }
            try {
                responseHandler.onResponse(responseCode, new JSONObject(respBody), etag);
            } catch (JSONException e) {
                responseHandler.onException(e);
            }
        } catch (MalformedURLException e) {
            responseHandler.onException(e);
        } catch (IOException e) {
            responseHandler.onException(e);
        }
    }

    @Override
    public void sendPlainTextRequest(int method, String url, String s1, Map<String, String> map, String s2, ResponseHandler responseHandler) {

    }

    @Override
    public void sendStreamRequest(int i, String s, String s1, String s2, Map<String, String> map, InputStream inputStream, ResponseHandler responseHandler) {

    }

    private void setHeaders(HttpURLConnection connection, String token, String contentType, Map<String, String> headers) {
        connection.setRequestProperty("x-kii-appid", mContext.getAppId());
        connection.setRequestProperty("x-kii-appkey", mContext.getAppKey());
        if (token != null) {
            connection.setRequestProperty("authorization", "bearer " + token);
        }
        if (contentType != null) {
            connection.setRequestProperty("Content-Type", contentType);
        }
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    private void writeBody(HttpURLConnection connection, String body) throws IOException {
        OutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter writer = null;
        try {
            out = connection.getOutputStream();
            osw = new OutputStreamWriter(out);
            writer = new BufferedWriter(osw);

            writer.write(body);
        } finally {
            try {
                if (writer != null) { writer.close(); }
                if (osw != null) { osw.close(); }
                if (out != null) { out.close(); }
            } catch (IOException e) {
                // nop
            }
        }
    }

    private String readStream(InputStream in) throws IOException {
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(in);
            br = new BufferedReader(isr);

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } finally {
            try {
                if (br != null) { br.close(); }
                if (isr != null) { isr.close(); }
                if (in != null) { in.close(); }
            } catch (IOException e) {
                // nop
            }
        }
    }

    private String toMethod(int method) {
        switch (method) {
        case Method.GET: return "GET";
        case Method.POST: return "POST";
        case Method.PUT: return "PUT";
        case Method.DELETE: return "DELETE";
        default: return "GET";
        }
    }
}
