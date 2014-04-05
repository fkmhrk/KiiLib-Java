package jp.fkmsoft.libs.kiilib.entities;

class KiiApp implements BucketOwnable {

    KiiApp() {
    }
    
    @Override
    public String getResourcePath() {
        return "";
    }

    @Override
    public int getType() {
        return BucketOwnable.TYPE_APP;
    }
}
