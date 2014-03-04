package com.ehr.framework.login;

import com.ehr.framework.cache.DefaultCacheConfiguration;
import com.ehr.framework.cache.LoginCache;
import com.ehr.framework.cache.LoginCacheManager;
import java.util.UUID;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

/**
 *
 * @author zoe
 */
public class LoginCacheManagerImpl<L extends LoginCache> implements LoginCacheManager<L> {

    //缓存数据对象
    private final Cache loginCacher;

    public LoginCacheManagerImpl(final CacheManager cacheManager) {
        //创建sql cache
        final CacheConfiguration loginCacheConfig = new DefaultCacheConfiguration().getDefault();
        String uuid = UUID.randomUUID().toString();
        String loginCacheName = "login-cache-".concat(uuid);
        loginCacheConfig.name(loginCacheName).maxElementsInMemory(20000);
        final Cache cache = new Cache(loginCacheConfig);
        cacheManager.addCache(cache);
        this.loginCacher = cache;
    }

    @Override
    public boolean assertLogin(String loginId) {
        boolean result = false;
        Element element = this.loginCacher.getQuiet(loginId);
        if (element != null) {
            result = true;
        }
        return result;
    }

    @Override
    public L getLoginCache(String loginId) {
        L l = null;
        Element element = this.loginCacher.getQuiet(loginId);
        if (element != null) {
            l = (L) element.getValue();
        }
        return l;
    }

    @Override
    public String putLoginCache(long userId, LoginCache loginCache) {
        String uuid = UUID.randomUUID().toString();
        StringBuilder loginIdBuilder = new StringBuilder(22 + uuid.length());
        loginIdBuilder.append(Long.toString(userId)).append('-').append(uuid);
        String loginId = loginIdBuilder.toString();
        Element element = new Element(loginId, loginCache);
        this.loginCacher.put(element);
        return loginId;
    }

    @Override
    public void clear() {
        this.loginCacher.removeAll();
    }
}
