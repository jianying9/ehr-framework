package com.ehr.framework.dictionary;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.entity.EntityConfig;
import com.ehr.framework.util.TimeUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据字典实体
 * @author zoe
 */
@EntityConfig(entityName = "Dictionary", keyField = "dicId", dataSourceName = "EhrDic")
public final class DictionaryEntity extends Entity {

    //字典ID
    private long dicId;
    //字典名称
    private String dicName;
    //缺省值
    private String dicDefault;
    //字典类型:0列表类型、1树类型
    private byte dicType;
    //当前eleValue索引
    private int dicIndex;
    //字典描述
    private String dicDesc;
    //是否启用：1启用、0未启用
    private byte inUse;
    //创建时间
    private String createTime;

    @Override
    public long getKeyValue() {
        return this.dicId;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> entityMap = new HashMap<String, String>(16, 1);
        entityMap.put("dicId", Long.toString(this.dicId));
        entityMap.put("dicName", this.dicName);
        entityMap.put("dicDefault", this.dicDefault);
        entityMap.put("dicType", Byte.toString(this.dicType));
        entityMap.put("dicIndex", Integer.toString(this.dicIndex));
        entityMap.put("dicDesc", this.dicDesc);
        entityMap.put("inUse", Byte.toString(this.inUse));
        entityMap.put("createTime", this.createTime);
        return entityMap;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.dicId = Long.parseLong(entityMap.get("dicId"));
        this.dicName = entityMap.get("dicName");
        this.dicDefault = entityMap.get("dicDefault");
        this.dicType = Byte.parseByte(entityMap.get("dicType"));
        this.dicIndex = Integer.parseInt(entityMap.get("dicIndex"));
        this.dicDesc = entityMap.get("dicDesc");
        this.inUse = Byte.parseByte(entityMap.get("inUse"));
        this.createTime = entityMap.get("createTime");
        this.createTime = TimeUtils.formatYYYYMMDDHHmmSS(createTime);
    }

    public long getDicId() {
        return dicId;
    }

    public String getDicName() {
        return dicName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getDicDefault() {
        return dicDefault;
    }

    public String getDicDesc() {
        return dicDesc;
    }

    public int getDicIndex() {
        return dicIndex;
    }

    public byte getDicType() {
        return dicType;
    }

    public byte getInUse() {
        return inUse;
    }
}
