package jp.fkmsoft.libs.kiilib.apis.impl;

import java.util.ArrayList;

import jp.fkmsoft.libs.kiilib.apis.QueryResult;
import jp.fkmsoft.libs.kiilib.entities.KiiObject;

class KiiObjectQueryResult<T extends KiiObject> extends ArrayList<T> implements QueryResult<T> {

    private String paginationKey;
    
    KiiObjectQueryResult(int capacity) {
        super(capacity);
    }
    
    @Override
    public String getPaginationKey() {
        return paginationKey;
    }
    
    void setPaginationKey(String value) {
        paginationKey = value;
    }

    @Override
    public boolean hasNext() {
        return (paginationKey != null);
    }

}
