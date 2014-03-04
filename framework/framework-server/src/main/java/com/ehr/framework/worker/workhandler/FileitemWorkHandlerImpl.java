package com.ehr.framework.worker.workhandler;

import com.ehr.framework.logger.LogFactory;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.worker.HttpServletRequestManager;
import com.ehr.framework.worker.HttpServletResponseManager;
import com.ehr.framework.worker.ResponseWriterManager;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;

/**
 * 获取文件取值、验证处理类处理类
 * @author zoe
 */
public class FileitemWorkHandlerImpl implements WorkHandler {

    private final Logger logger = LogFactory.getFrameworkLogger();
    private final WorkHandler nextWorkHandler;
    private final WriteFileItemManager fileItemManager;
    private final HttpServletRequestManager httpServletRequestManager;
    private final ResponseWriterManager responseWriterManager;
    private final HttpServletResponseManager httpServletResponseManager;

    public FileitemWorkHandlerImpl(final WriteFileItemManager fileItemManager, final HttpServletRequestManager httpServletRequestManager, final HttpServletResponseManager httpServletResponseManager, final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.nextWorkHandler = workHandler;
        this.fileItemManager = fileItemManager;
        this.httpServletRequestManager = httpServletRequestManager;
        this.responseWriterManager = responseWriterManager;
        this.httpServletResponseManager = httpServletResponseManager;
    }

    @Override
    public void execute() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> items = null;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException e) {
            this.logger.error("get file failure. ", e);
        }
        if (items != null && !items.isEmpty()) {
            this.fileItemManager.openThreadLocal(items);
            this.nextWorkHandler.execute();
            this.fileItemManager.closeThreadLocal();
        } else {
            ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
            responseWriter.noFile();
            HttpServletResponse response = this.httpServletResponseManager.getThreadLocal();
            responseWriter.writeJson(request, response);
        }
    }
}
