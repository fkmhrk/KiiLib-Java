package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory for KiiBucket
 */
public interface KiiBucketFactory<BUCKET extends KiiBucket> {
    BUCKET create();
}
