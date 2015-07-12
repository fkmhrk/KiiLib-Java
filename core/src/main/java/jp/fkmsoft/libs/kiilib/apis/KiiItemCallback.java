package jp.fkmsoft.libs.kiilib.apis;

/**
 * Base callback
 */
public interface KiiItemCallback<T> extends KiiCallback {
    void onSuccess(T item);
}
