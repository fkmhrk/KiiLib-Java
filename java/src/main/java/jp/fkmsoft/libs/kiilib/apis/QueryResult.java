package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;

import jp.fkmsoft.libs.kiilib.entities.KiiBucket;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;

public interface QueryResult<BUCKET extends KiiBucket, OBJECT extends KiiObject<BUCKET>> extends List<OBJECT> {
    String getPaginationKey();
    boolean hasNext();
}
