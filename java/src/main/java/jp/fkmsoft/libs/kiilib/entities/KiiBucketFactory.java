package jp.fkmsoft.libs.kiilib.entities;

/**
 * Factory for KiiBaseBucket
 */
public interface KiiBucketFactory<BUCKET extends KiiBaseBucket> {
    BUCKET create();
}
