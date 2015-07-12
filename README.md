KiiLib-Java
===========

Kii Cloud Library for Java. This library just provides APIs for your application so please add KiiLib-Java-HTTPClient(for native Java application such as Swing. coming soon) / KiiLib-Android-Volley (for Android application with Volley. coming soon)

How to add this library
=======================
Add the following entry to your `build.gradle`

```
repositories {
    maven {
        url "https://raw.githubusercontent.com/fkmhrk/KiiLib-Java/master/m2repository"
    }
}

dependencies {
    compile 'jp.fkmsoft.libs:KiiLib-Java:3.0.0'
}
```

You can pick only APIs you will use. `'jp.fkmsoft.libs:KiiLib-Java:3.0.0'` contains ALL APIs.

```
dependencies {
    compile 'jp.fkmsoft.libs:KiiLib-Java-AppAPI:3.0.0'
    compile 'jp.fkmsoft.libs:KiiLib-Java-BucketAPI:3.0.0'
    compile 'jp.fkmsoft.libs:KiiLib-Java-ObjectAPI:3.0.0'
}
```


