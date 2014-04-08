package jp.fkmsoft.libs.kiilib.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class KiiBaseObject<BUCKET extends KiiBaseBucket> extends JSONObject implements AccessControllable {
    private static final String FIELD_ID = "_id";
    private static final String FIELD_VERSION = "_version";
    private static final String FIELD_MODIFIED = "_modified";
    private static final String FIELD_CREATED = "_created";
    
    private static final String FIELD_OBJECT_ID = "objectID";
    
    
    private final BUCKET bucket;
    private String id;
    private String version;
    
    protected KiiBaseObject(BUCKET bucket) {
        this.bucket = bucket;
        id = "";
        version = "";
    }
    
    protected KiiBaseObject(BUCKET bucket, String id) {
        this.bucket = bucket;
        this.id = id;
        version = "";
    }
    
    protected KiiBaseObject(BUCKET bucket, JSONObject from) throws JSONException {
        this.bucket = bucket;
        replace(from);
        id = from.optString(FIELD_ID, null);
        if (id == null) {
            id = from.optString(FIELD_OBJECT_ID, "");
        }
        version = from.optString(FIELD_VERSION, "");
    }
    
    public void replace(JSONObject source) throws JSONException {
        clear();
        @SuppressWarnings("unchecked")
        Iterator<String> it = source.keys();
        while (it.hasNext()) {
            String name = it.next();
            put(name, source.get(name));
        }
    }
    
    public void clear() {
        List<String> keyList = new ArrayList<String>(this.length());
        @SuppressWarnings("unchecked")
        Iterator<String> it = this.keys();
        while (it.hasNext()) {
            keyList.add(it.next());
        }
        for (String key : keyList) {
            remove(key);
        }
    }
    
    public BUCKET getBucket() {
        return bucket;
    }

    public String getId() {
        return id;
    }
    
    public String getResourcePath() {
        return bucket.getResourcePath() + "/objects/" + id;
    }
    
    public void setVersion(String etag) {
        version = etag;
    }

    public String getVersion() {
        return version;
    }

    public void setModifiedTime(long value) {
        try {
            put(FIELD_MODIFIED, value);
        } catch (JSONException e) {/* */ }
    }

    public void setCreatedTime(long value) {
        try {
            put(FIELD_CREATED, value);
        } catch (JSONException e) {/* */ }
    }
    
    public long getCreatedTime() {
        return optLong(FIELD_CREATED, 0);
    }
    
    public long getModifiedTime() {
        return optLong(FIELD_MODIFIED, 0);
    }
}
