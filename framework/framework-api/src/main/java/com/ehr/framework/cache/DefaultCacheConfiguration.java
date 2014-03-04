package com.ehr.framework.cache;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.CacheConfiguration.TransactionalMode;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

/**
 * ehcache default config
 * @author zoe
 */
public class DefaultCacheConfiguration {

    private final String name = "";
    private final int maxElementsInMemory = 10000;
    private final MemoryStoreEvictionPolicy memoryStoreEvictionPolicy = MemoryStoreEvictionPolicy.LRU;
    private final boolean eternal = false;
    private final int timeToIdleSeconds = 300;
    private final int timeToLiveSeconds = 3600;
    private final boolean overflowToDisk = false;
    private final boolean overflowToOffHeap = false;
    private final boolean diskPersistent = false;
    private final boolean statistics = false;
    private final TransactionalMode transactionalMode = TransactionalMode.OFF;

    public CacheConfiguration getDefault() {
        CacheConfiguration cacheConfig = new CacheConfiguration();
        cacheConfig.name(this.name).maxElementsInMemory(this.maxElementsInMemory).memoryStoreEvictionPolicy(this.memoryStoreEvictionPolicy).eternal(this.eternal).timeToIdleSeconds(this.timeToIdleSeconds).timeToLiveSeconds(this.timeToLiveSeconds).overflowToDisk(this.overflowToDisk).overflowToOffHeap(this.overflowToOffHeap).diskPersistent(this.diskPersistent).statistics(this.statistics).transactionalMode(this.transactionalMode);
        return cacheConfig;
    }
}
