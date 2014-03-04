package com.ehr.framework.dictionary;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.entity.EntityConfig;
import com.ehr.framework.util.TimeUtils;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zoe
 */
@EntityConfig(entityName = "DictionaryLog", keyField = "logId", dataSourceName = "EhrDic")
public final class DictionaryLogEntity extends Entity {

    //日志ID
    private long logId;
    //操作帐号
    private String userName;
    //操作时间
    private String operateTime;
    //操作类型:0新增字典、1修改字典、2新增元素、3修改元素
    private byte operateType;
    //字典ID
    private long dicId;
    //元素ID
    private long eleId;
    //修改前记录详细信息
    private String oldDetail;
    //修改后记录详细信息
    private String newDetail;

    @Override
    public Map<String, String> toMap() {
        Map<String, String> entityMap = new HashMap<String, String>(16, 1);
        entityMap.put("logId", Long.toString(this.logId));
        entityMap.put("userName", this.userName);
        entityMap.put("operateTime", this.operateTime);
        entityMap.put("operateType", Byte.toString(this.operateType));
        entityMap.put("dicId", Long.toString(this.dicId));
        entityMap.put("eleId", Long.toString(this.eleId));
        entityMap.put("oldDetail", this.oldDetail);
        entityMap.put("newDetail", this.newDetail);
        return entityMap;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.logId = Long.parseLong(entityMap.get("logId"));
        this.userName = entityMap.get("userName");
        this.operateTime = entityMap.get("operateTime");
        this.operateTime = TimeUtils.formatYYYYMMDDHHmmSS(operateTime);
        this.operateType = Byte.parseByte(entityMap.get("operateType"));
        this.dicId = Long.parseLong(entityMap.get("dicId"));
        this.eleId = Long.parseLong(entityMap.get("eleId"));
        this.oldDetail = entityMap.get("oldDetail");
        this.newDetail = entityMap.get("newDetail");
    }

    @Override
    public long getKeyValue() {
        return this.logId;
    }

    public long getDicId() {
        return dicId;
    }

    void setDicId(long dicId) {
        this.dicId = dicId;
    }

    public long getEleId() {
        return eleId;
    }

    void setEleId(long eleId) {
        this.eleId = eleId;
    }

    public long getLogId() {
        return logId;
    }

    void setLogId(long logId) {
        this.logId = logId;
    }

    public String getOperateTime() {
        return operateTime;
    }

    void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public byte getOperateType() {
        return operateType;
    }

    void setOperateType(byte operateType) {
        this.operateType = operateType;
    }

    public String getUserName() {
        return userName;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNewDetail() {
        return newDetail;
    }

    void setNewDetail(String newDetail) {
        this.newDetail = newDetail;
    }

    public String getOldDetail() {
        return oldDetail;
    }

    void setOldDetail(String oldDetail) {
        this.oldDetail = oldDetail;
    }
}
