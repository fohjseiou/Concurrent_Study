//package com.ykdz.caffeine;
//
//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;
//
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//public class CaffeineCache {
//    private Cache cache;
//
//    public CaffeineCache() {
//        this.cache = generator();
//    }
//
//    public CaffeineCache(Cache cache) {
//        this.cache = cache;
//    }
//
//    private Cache generator(){
//        Cache<String, Map> cache = Caffeine.newBuilder()
//                //初始数量
//                .initialCapacity(10)
//                //最大条数
//                .maximumSize(10)
//                //expireAfterWrite和expireAfterAccess同时存在时，以expireAfterWrite为准
//                //最后一次写操作后经过指定时间过期
//                .expireAfterWrite(1, TimeUnit.SECONDS) //1s
//                //最后一次读或写操作后经过指定时间过期
//                .expireAfterAccess(1, TimeUnit.SECONDS)
//                //监听缓存被移除
//                .removalListener((key, val, removalCause) -> {
//                })
//                //记录命中
//                .recordStats()
//                .build();
//        return cache;
//    }
//}
