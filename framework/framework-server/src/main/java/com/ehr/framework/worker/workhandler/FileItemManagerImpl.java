package com.ehr.framework.worker.workhandler;

import java.util.List;
import org.apache.commons.fileupload.FileItem;

/**
 * 在线程中存放,读取,清除上传文件管理类
 * @author zoe
 */
public final class FileItemManagerImpl implements WriteFileItemManager {

    private final ThreadLocal<List<FileItem>> threadLocal = new ThreadLocal<List<FileItem>>();

    @Override
    public void openThreadLocal(List<FileItem> fileItemList) {
        threadLocal.set(fileItemList);
    }

    @Override
    public void closeThreadLocal() {
        threadLocal.remove();
    }

    @Override
    public List<FileItem> getThreadLocal() {
        return threadLocal.get();
    }
}
