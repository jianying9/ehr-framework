package com.ehr.framework.context;

import java.util.List;
import org.apache.commons.fileupload.FileItem;

/**
 * 在线程中存放,读取,清除上传文件管理类
 * @author zoe
 */
public interface FileItemManager {

    public List<FileItem> getThreadLocal();
}
