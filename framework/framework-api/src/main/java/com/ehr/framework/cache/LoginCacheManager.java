package com.ehr.framework.cache;

/**
 * 登录信息管理对象
 * @author zoe
 */
public interface LoginCacheManager<L extends LoginCache> {

    /**
     * 根据登录ID判断该ID是否登录
     * @param loginId 该ID将按照一定规则随机生成
     * @return 
     */
    public boolean assertLogin(String loginId);

    /**
     * 根据loginId获取登录信息
     * @param loginId
     * @return 
     */
    public L getLoginCache(String loginId);

    /**
     * 将登录信息放入缓存，并返回longId
     * @param loginCache
     * @return 
     */
    public String putLoginCache(long userId, LoginCache loginCache);
    
    /**
     * 清除缓存
     */
    public void clear();
}
