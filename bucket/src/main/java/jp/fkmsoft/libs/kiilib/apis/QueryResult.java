package jp.fkmsoft.libs.kiilib.apis;

import java.util.List;

import jp.fkmsoft.libs.kiilib.entities.KiiObject;

public interface QueryResult<T extends KiiObject> extends List<T> {
    String getPaginationKey();
    boolean hasNext();
}
