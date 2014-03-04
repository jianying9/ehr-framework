package com.ehr.framework.file;

import java.io.File;
import java.io.IOException;
import org.apache.commons.fileupload.FileItem;

/**
 * 保存上传文件,图片管理类
 * @author zoe
 */
public interface UploadFileManager {

    /**
     * 验证是否有效的文件
     * @param fileUrl
     * @return 
     */
    public boolean isAvailableFile(final String fileUrl, final long companyId);

    /**
     * 验证是否是有效的临时文件
     * @param fileUrl
     * @return 
     */
    public boolean isAvailableTempFile(final String fileUrl);

    /**
     * 保存文件
     * @param fileItem
     * @return 
     */
    public String saveFile(final FileItem fileItem) throws Exception;

    /**
     * 创建临时.html文件,并返回访问URL
     * @return String
     */
    public String createHtmlTempFile() throws IOException;
    
    /**
     * 创建临时.html文件,并返回访问URL
     * @return String
     */
    public String createHtmlFile(final long companyId) throws IOException;

    /**
     * 创建临时.jpg文件,并返回访问URL
     * @return String
     */
    public String createJpgTempFile() throws IOException;

    /**
     * 创建临时.gif文件,并返回访问URL
     * @return
     * @throws IOException
     */
    public String createGifTempFile() throws IOException;

    /**
     * 根据url获取文件
     * @param fileUrl
     * @return File 找不到返回null
     */
    public File getFileByUrl(final String fileUrl);

    /**
     * 将URL对应的文件移动到新的目录
     * @param companyId
     * @param filePath
     * @return
     * @throws IOException
     */
    public String moveTempFileByUrl(final long companyId, final String fileUrl) throws IOException;
    
    /**
     * 创建临时图片
     * @param images
     * @return 
     */
    public String createTempImage(byte[] images) throws Exception;
}
