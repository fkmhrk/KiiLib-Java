package jp.fkmsoft.libs.kiilib.apis;

import org.json.JSONObject;

import java.io.InputStream;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.KiiObjectDTO;


public interface ObjectAPI {

    /**
     * Gets Kii Object by Id.
     * @param bucket Target bucket.
     * @param id Object ID.
     * @param dto DTO for Kii Object.
     * @param callback Callback object.
     * @param <T> Type of Kii Object.
     */
    <T extends KiiObject> void getById(KiiBucket bucket, String id, KiiObjectDTO<T> dto, ObjectCallback<T> callback);

    /**
     * Creates new Object to target bucket.
     * @param bucket Target bucket.
     * @param obj Object.
     * @param dto DTO for Kii Object.
     * @param callback Callback object.
     * @param <T> Type of Kii Object.
     */
    <T extends KiiObject> void create(KiiBucket bucket, JSONObject obj, KiiObjectDTO<T> dto, ObjectCallback<T> callback);

    /**
     * Saves target object. If object ID is in Kii Cloud, updates it. Otherwise creates it with ID.
     * @param obj Target object.
     * @param callback Callback object.
     */
    void save(KiiObject obj, ObjectCallback<KiiObject> callback);

    /**
     * Patch update.
     * @param obj Target object.
     * @param patch Patch.
     * @param callback Callback object.
     */
    void updatePatch(KiiObject obj, JSONObject patch, ObjectCallback<KiiObject> callback);

    /**
     * Patch update if object in Kii Cloud is not modified.
     * @param obj Target object.
     * @param patch Patch.
     * @param callback Callback object.
     */
    void updatePatchIfUnmodified(KiiObject obj, JSONObject patch, ObjectCallback<KiiObject> callback);

    /**
     * Updates object body.
     * @param obj Target object.
     * @param contentType Content type.
     * @param source Data source.
     * @param callback Callback object.
     */
    void updateBody(KiiObject obj, String contentType, InputStream source, ObjectCallback<KiiObject> callback);

    /**
     * Publishes object body.
     * @param obj Target object.
     * @param callback Callback object.
     */
    void publish(KiiObject obj, PublishCallback callback);

    /**
     * Deletes target object.
     * @param obj Target object.
     * @param callback Callback object.
     */
    void delete(KiiObject obj, KiiItemCallback<Void> callback);

    interface ObjectCallback<T extends KiiObject> extends KiiItemCallback<T> { }

    interface PublishCallback extends KiiItemCallback<String> { }
}
