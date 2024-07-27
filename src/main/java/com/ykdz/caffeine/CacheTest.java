//package com.ykdz.caffeine;

//import com.github.benmanes.caffeine.cache.Cache;

//public class CacheTest {
//    private static volatile CaffeineCache caffeineCache;
//
//    public static CaffeineCache init(CaffeineCache caffeineCache) {
//        if (caffeineCache == null) {
//            synchronized (CacheTest.class) {
//                if (caffeineCache == null) {
//                    caffeineCache = new CaffeineCache();
//                }
//            }
//        }
//        return caffeineCache;
//    }
//
//
//}
