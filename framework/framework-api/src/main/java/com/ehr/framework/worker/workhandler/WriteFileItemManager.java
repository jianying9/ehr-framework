package com.ehr.framework.worker.workhandler;

import com.ehr.framework.context.FileItemManager;
import java.util.List;
import org.apache.commons.fileupload.FileItem;

/**
 * 在线程中存放,读取,清除上传文件管理类
 * @author zoe
 */
public interface WriteFileItemManager extends FileItemManager{
    
    public void openThreadLocal(List<FileItem> fileItemList);

    public void closeThreadLocal();
}
