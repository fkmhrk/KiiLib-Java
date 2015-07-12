package jp.fkmsoft.libs.kiilib.apis;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.KiiObjectDTO;

/**
 * Provides bucket API.
 */
public interface BucketAPI {
    /**
     * Queries objects.
     * @param bucket Target bucket.
     * @param params Parameters.
     * @param dto DTO for Kii Object.
     * @param callback Callback object.
     * @param <T> Type of Kii Object.
     */
    <T extends KiiObject> void query(KiiBucket bucket, QueryParams params, KiiObjectDTO<T> dto, QueryCallback<T> callback);

    /**
     * Deletes bucket
     * @param bucket Target bucket
     * @param callback Callback object.
     */
    void delete(KiiBucket bucket, KiiItemCallback<Void> callback);

    interface QueryCallback<T extends KiiObject> extends KiiItemCallback<QueryResult<T>> { }
}
