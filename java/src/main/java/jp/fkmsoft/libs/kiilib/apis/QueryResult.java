package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;

import jp.fkmsoft.libs.kiilib.entities.KiiBaseBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiBaseObject;

public interface QueryResult<BUCKET extends KiiBaseBucket, OBJECT extends KiiBaseObject<BUCKET>> extends List<OBJECT> {
    String getPaginationKey();
    boolean hasNext();
}
