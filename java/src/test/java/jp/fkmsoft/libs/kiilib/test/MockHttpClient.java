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

    public final Queue<MockResponse> mSendJsonQueue = new LinkedList<MockResponse>();
    public final Queue<MockResponse> mSendPlainQueue = new LinkedList<MockResponse>();
    public final Queue<MockResponse> mSendStreamQueue = new LinkedList<MockResponse>();

    @Override
    public void sendJsonRequest(int method, String url, String token, String contentType, Map<String, String> headers, JSONObject body, ResponseHandler handler) {
        MockResponse response = mSendJsonQueue.poll();
        if (response != null) {
            handler.onResponse(response.mStatus, response.mBody, response.mEtag);
        }
    }

    @Override
    public void sendPlainTextRequest(int method, String url, String token, Map<String, String> headers, String body, ResponseHandler handler) {
        MockResponse response = mSendPlainQueue.poll();
        if (response != null) {
            handler.onResponse(response.mStatus, response.mBody, response.mEtag);
        }
    }

    @Override
    public void sendStreamRequest(int method, String url, String token, String contentType, Map<String, String> headers, InputStream body, ResponseHandler handler) {
        MockResponse response = mSendStreamQueue.poll();
        if (response != null) {
            handler.onResponse(response.mStatus, response.mBody, response.mEtag);
        }
    }

    public void addToSendJson(int status, String body, String etag) {
        addToQueue(mSendJsonQueue, status, body, etag);
    }

    public void addToSendPlain(int status, String body, String etag) {
        addToQueue(mSendPlainQueue, status, body, etag);
    }

    public void addToSendStream(int status, String body, String etag) {
        addToQueue(mSendStreamQueue, status, body, etag);
    }

    private void addToQueue(Queue<MockResponse> queue, int status, String body, String etag) {
        if (body == null) {
            queue.add(new MockResponse(status, null, etag));
            return;
        }
        try {
            queue.add(new MockResponse(status, new JSONObject(body), etag));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
