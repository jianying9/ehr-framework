package com.ehr.sendmail.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.activation.FileDataSource;

/**
 * 邮件附件实例类
 *
 * @author chancelin
 */
public class MailAttachmentEntity {

    private List<FileDataSource> fileDataSourceList = new ArrayList<FileDataSource>(4);

    public MailAttachmentEntity() {
    }
    
    @Deprecated
    public MailAttachmentEntity(String a, String b) {
    }

    /**
     * 获取文件资源
     *
     * @param fds 文件资源
     */
    public List<FileDataSource> getFileDataSourceList() {
        return this.fileDataSourceList;
    }

    /**
     * 根据文件路径添加文件源
     *
     * @param filePath
     */
    public void addFileDataSource(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                FileDataSource fileDataSource = new FileDataSource(file);
                this.fileDataSourceList.add(fileDataSource);
            }
        }
    }

    /**
     * 根据文件对象添加文件源
     *
     * @param file
     */
    public void addFileDataSource(File file) {
        if (file != null && file.exists()) {
            FileDataSource fileDataSource = new FileDataSource(file);
            this.fileDataSourceList.add(fileDataSource);
        }
    }
}