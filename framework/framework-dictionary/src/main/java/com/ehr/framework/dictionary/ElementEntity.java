package com.ehr.framework.dictionary;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.entity.EntityConfig;
import com.ehr.framework.util.TimeUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据元素实体
 * @author zoe
 */
@EntityConfig(entityName = "Element", keyField = "eleId", dataSourceName = "EhrDic")
public final class ElementEntity extends Entity {

    //字典元素ID
    private long eleId;
    //元素值
    private int eleValue;
    //元素名称
    private String eleName;
    //父元素ID
    private long parentEleId;
    //路径
    private String treePath;
    //排序号
    private long sortId;
    //是否启用：1启用、0未启用
    private byte inUse;
    //创建时间
    private String createTime;
    //字典ID
    private long dicId;

    @Override
    public Map<String, String> toMap() {
        Map<String, String> entityMap = new HashMap<String, String>(16, 1);
        entityMap.put("eleId", Long.toString(this.eleId));
        entityMap.put("eleName", this.eleName);
        entityMap.put("eleValue", Integer.toString(this.eleValue));
        entityMap.put("parentEleId", Long.toString(this.parentEleId));
        entityMap.put("treePath", this.treePath);
        entityMap.put("sortId", Long.toString(this.sortId));
        entityMap.put("inUse", Byte.toString(this.inUse));
        entityMap.put("dicId", Long.toString(this.dicId));
        entityMap.put("createTime", this.createTime);
        return entityMap;
    }

    @Override
    protected void parseMap(Map<String, String> entityMap) {
        this.eleId = Long.parseLong(entityMap.get("eleId"));
        this.eleName = entityMap.get("eleName");
        this.eleValue = Integer.parseInt(entityMap.get("eleValue"));
        this.parentEleId = Integer.parseInt(entityMap.get("parentEleId"));
        this.treePath = entityMap.get("treePath");
        this.sortId = Long.parseLong(entityMap.get("sortId"));
        this.inUse = Byte.parseByte(entityMap.get("inUse"));
        this.createTime = entityMap.get("createTime");
        this.createTime = TimeUtils.formatYYYYMMDDHHmmSS(createTime);
        this.dicId = Long.parseLong(entityMap.get("dicId"));
    }

    @Override
    public long getKeyValue() {
        return this.eleId;
    }

    public long getDicId() {
        return dicId;
    }

    public String getEleName() {
        return eleName;
    }

    public int getEleValue() {
        return eleValue;
    }

    public String getCreateTime() {
        return createTime;
    }

    public long getEleId() {
        return eleId;
    }

    public byte getInUse() {
        return inUse;
    }

    public long getParentEleId() {
        return parentEleId;
    }

    public long getSortId() {
        return sortId;
    }

    public String getTreePath() {
        return treePath;
    }
}
