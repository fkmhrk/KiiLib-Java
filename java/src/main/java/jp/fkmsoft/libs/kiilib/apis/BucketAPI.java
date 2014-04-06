package jp.fkmsoft.libs.kiilib.apis;

import jp.fkmsoft.libs.kiilib.entities.KiiBaseBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseObject;
import jp.fkmsoft.libs.kiilib.entities.QueryParams;

/**
 * Provides bucket API. To get this instance, Please call {@link AppAPI#bucketAPI()}
 * @author fkm
 *
 */
public interface BucketAPI<BUCKET extends KiiBaseBucket, OBJECT extends KiiBaseObject<BUCKET>> {
    public interface QueryCallback<T extends KiiBaseBucket, U extends KiiBaseObject<T>> extends KiiCallback {
        void onSuccess(QueryResult<T, U> result);
    }
    void query(BUCKET bucket, QueryParams params, QueryCallback<BUCKET, OBJECT> callback);
    
    public interface BucketCallback<U extends KiiBaseBucket> extends KiiCallback {
        void onSuccess(U bucket);
    }
    
    void delete(BUCKET bucket, BucketCallback<BUCKET> callback);
}
