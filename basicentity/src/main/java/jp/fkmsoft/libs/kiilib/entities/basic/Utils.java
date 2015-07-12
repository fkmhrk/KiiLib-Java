package jp.fkmsoft.libs.kiilib.entities.basic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Utility class
 */
class Utils {
    static void copyJson(JSONObject source, JSONObject to) {
        if (source == null) {
            return;
        }
        Iterator keys = source.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                to.put(key, source.get(key));
            } catch (JSONException e) {
                // nop
            }
        }
    }
}
