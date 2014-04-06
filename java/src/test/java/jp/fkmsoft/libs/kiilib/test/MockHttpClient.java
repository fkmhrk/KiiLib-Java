package jp.fkmsoft.libs.kiilib.test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import jp.fkmsoft.libs.kiilib.http.KiiHTTPClient;

/**
 * Mock class for HttpClient
 */
public class MockHttpClient implements KiiHTTPClient {

    public Queue<MockResponse> mSendJsonQueue = new LinkedList<MockResponse>();

    @Override
    public void sendJsonRequest(int method, String url, String token, String contentType, Map<String, String> headers, JSONObject body, ResponseHandler handler) {
        MockResponse response = mSendJsonQueue.poll();
        if (response != null) {
            handler.onResponse(response.mStatus, response.mBody, response.mEtag);
        }
    }

    @Override
    public void sendPlainTextRequest(int method, String url, String token, Map<String, String> headers, String body, ResponseHandler handler) {

    }

    @Override
    public void sendStreamRequest(int method, String url, String token, String contentType, Map<String, String> headers, InputStream body, ResponseHandler handler) {

    }

    public void addToSendJson(int status, String body, String etag) {
        try {
            mSendJsonQueue.add(new MockResponse(status, new JSONObject(body), etag));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
