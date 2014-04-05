package jp.fkmsoft.libs.kiilib.apis;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;
import jp.fkmsoft.libs.kiilib.entities.QueryParams;

/**
 * Provides bucket API. To get this instance, Please call {@link AppAPI#bucketAPI()}
 * @author fkm
 *
 */
public interface BucketAPI<BUCKET extends KiiBucket, OBJECT extends KiiObject<BUCKET>> {
    public interface QueryCallback<T extends KiiBucket, U extends KiiObject<T>> extends KiiCallback {
        void onSuccess(QueryResult<T, U> result);
    }
    void query(BUCKET bucket, QueryParams params, QueryCallback<BUCKET, OBJECT> callback);
    
    public interface BucketCallback<U extends KiiBucket> extends KiiCallback {
        void onSuccess(U bucket);
    }
    
    void delete(BUCKET bucket, BucketCallback<BUCKET> callback);
}
