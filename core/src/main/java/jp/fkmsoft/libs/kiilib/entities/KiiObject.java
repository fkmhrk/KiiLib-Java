package jp.fkmsoft.libs.kiilib.entities;

import org.json.JSONObject;

/**
 * Describes object in Kii Cloud
 */
public interface KiiObject extends KiiEntity, AccessControllable {
    /**
     * Gets object version
     * @return Object version
     */
    String getVersion();

    /**
     * Sets object version
     * @param version Object version
     */
    void setVersion(String version);

    /**
     * Updates fields
     * @param fields Fields
     */
    void updateFields(JSONObject fields);

    /**
     * Sets modified time
     * @param value Modified time
     */
    void setModifiedTime(long value);

    /**
     * @return JSONObject
     */
    JSONObject toJson();
}
