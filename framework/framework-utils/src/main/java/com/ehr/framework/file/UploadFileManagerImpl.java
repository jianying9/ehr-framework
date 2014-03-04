package com.ehr.framework.file;

import com.ehr.framework.logger.LogFactory;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

/**
 * 保存上传文件,图片管理类
 * @author zoe
 */
public final class UploadFileManagerImpl implements UploadFileManager {

    public UploadFileManagerImpl(final String localDirectory, final String aliasFileUrl) {
        this.localDirectory = localDirectory;
        if (!localDirectory.isEmpty()) {
            StringBuilder tempDirctoryBuilder = new StringBuilder(localDirectory.length() + 5);
            tempDirctoryBuilder.append(localDirectory).append(this.tempName).append('/');
            this.localTempDirectory = tempDirctoryBuilder.toString();
        } else {
            throw new RuntimeException("There was an error instancing UploadFileManager. Cause: localDirectory is empty.");
        }
        //判断本地存储临时文件路径是否存在,如果不存在,就新建
        File dirFile = new File(this.localTempDirectory);
        if (!dirFile.exists()) {
            boolean creadok = dirFile.mkdirs();
            if (creadok) {
                this.logger.debug("create directory {}", localTempDirectory);
            } else {
                throw new RuntimeException("There was an error instancing UploadFileManager. Cause: can not create dir ".concat(this.localTempDirectory));
            }
        }
        //http访问上传文件别名
        this.aliasFileUrl = aliasFileUrl;
        if (aliasFileUrl.isEmpty()) {
            throw new RuntimeException("There was an error instancing UploadFileManager. Cause: aliasFileUrl is empty.");
        }
    }
    //本地存储文件路径
    private final String localDirectory;
    private final String localTempDirectory;
    private final String tempName = "temp";
    private final String htmlExtend = ".html";
    private final String jpgExtend = ".jpg";
    private final String gifExtend = ".gif";
    //上传文件访问别名
    private final String aliasFileUrl;
    //日志对象
    private final Logger logger = LogFactory.getFrameworkLogger();

    /**
     * 验证是否有效的文件
     * @param fileUrl
     * @return 
     */
    @Override
    public boolean isAvailableFile(final String fileUrl, final long companyId) {
        String tempAliasFileUrl = this.aliasFileUrl.concat(Long.toString(companyId));
        return fileUrl.indexOf(tempAliasFileUrl) == 0 ? true : false;
    }

    /**
     * 验证是否是有效的临时文件
     * @param fileUrl
     * @return 
     */
    @Override
    public boolean isAvailableTempFile(final String fileUrl) {
        String tempAliasFileUrl = this.aliasFileUrl.concat(this.tempName);
        return fileUrl.indexOf(tempAliasFileUrl) == 0 ? true : false;
    }

    /**
     * 保存文件
     * @param fileItem
     * @return 
     */
    @Override
    public String saveFile(final FileItem fileItem) throws Exception {
        String requestUrl = "";
        String oldFileName = fileItem.getName();
        String fileExtend = oldFileName.substring(oldFileName.lastIndexOf('.'));
        String newFileName = this.createFileName(fileExtend);
        File uploadedFile = new File(this.localTempDirectory.concat(newFileName));
        fileItem.write(uploadedFile);
        StringBuilder requestUrlBuilder = new StringBuilder(this.aliasFileUrl.length() + newFileName.length() + 5);
        requestUrlBuilder.append(this.aliasFileUrl).append(this.tempName).append('/').append(newFileName);
        requestUrl = requestUrlBuilder.toString();
        return requestUrl;
    }

    /**
     * 创建临时.html文件,并返回访问URL
     * @return String
     */
    @Override
    public String createHtmlTempFile() throws IOException {
        String requestUrl = "";
        String newFileName = this.createFileName(this.htmlExtend);
        requestUrl = this.createTempFile(newFileName);
        return requestUrl;
    }

    /**
     * 创建临时.html文件,并返回访问URL
     * @return String
     */
    @Override
    public String createHtmlFile(final long companyId) throws IOException {
        String requestUrl = "";
        String newFileName = this.createFileName(this.htmlExtend);
        requestUrl = this.createFile(companyId, newFileName);
        return requestUrl;
    }

    /**
     * 创建临时.jpg文件,并返回访问URL
     * @return String
     */
    @Override
    public String createJpgTempFile() throws IOException {
        String requestUrl = "";
        String newFileName = this.createFileName(this.jpgExtend);
        requestUrl = this.createTempFile(newFileName);
        return requestUrl;
    }

    /**
     * 创建临时.gif文件,并返回访问URL
     * @return
     * @throws IOException
     */
    @Override
    public String createGifTempFile() throws IOException {
        String requestUrl = "";
        String newFileName = this.createFileName(this.gifExtend);
        requestUrl = this.createTempFile(newFileName);
        return requestUrl;
    }

    private String createTempFile(final String fileName) throws IOException {
        String requestUrl = "";//return
        File newFile = new File(this.localTempDirectory.concat(fileName));
        newFile.createNewFile();
        StringBuilder requestUrlBuilder = new StringBuilder(this.aliasFileUrl.length() + fileName.length() + 5);
        requestUrlBuilder.append(aliasFileUrl).append(tempName).append('/').append(fileName);
        requestUrl = requestUrlBuilder.toString();
        return requestUrl;
    }

    private String createFile(final long companyId, final String fileName) throws IOException {
        String requestUrl = "";//return
        String companyIdStr = Long.toString(companyId);
        StringBuilder newFileBuilder = new StringBuilder(this.localDirectory.length() + companyIdStr.length() + fileName.length() + 1);
        newFileBuilder.append(this.localDirectory).append(companyIdStr);
        File dirFile = new File(newFileBuilder.toString());
        if (!dirFile.exists()) {
            boolean creadok = dirFile.mkdirs();
            if (!creadok) {
                this.logger.error("There was an error creating file. Cause: can not create dir {}", newFileBuilder.toString());
                throw new RuntimeException("There was an error creating file. Cause: can not create dir ".concat(newFileBuilder.toString()));
            }
        }
        newFileBuilder.append('/').append(fileName);
        File newFile = new File(newFileBuilder.toString());
        newFile.createNewFile();
        StringBuilder requestUrlBuilder = new StringBuilder(this.aliasFileUrl.length() + companyIdStr.length() + fileName.length() + 1);
        requestUrlBuilder.append(this.aliasFileUrl).append(companyIdStr).append('/').append(fileName);
        requestUrl = requestUrlBuilder.toString();
        return requestUrl;
    }

    /**
     * 根据url获取文件
     * @param fileUrl
     * @return File 找不到返回null
     */
    @Override
    public File getFileByUrl(final String fileUrl) {
        File file = null;
        if(fileUrl.equals("")|| fileUrl == null){
        	return file;
        }else{
	        File tempFile = new File(this.localDirectory.concat(fileUrl.substring(this.aliasFileUrl.length())));
	        if (tempFile.exists()) {
	            file = tempFile;
	        }
        }
        return file;
    }

    /**
     * 将URL对应的文件移动到新的目录
     * @param companyId
     * @param filePath
     * @return
     * @throws IOException
     */
    @Override
    public String moveTempFileByUrl(final long companyId, final String fileUrl)
            throws IOException {
        String newFilePath = null;
        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        File tempFile = new File(this.localTempDirectory.concat(fileName));
        if (tempFile.exists()) {
            String companyIdStr = Long.toString(companyId);
            StringBuilder newFileDirBuilder = new StringBuilder(this.localDirectory.length() + companyIdStr.length() + 1);
            newFileDirBuilder.append(this.localDirectory).append(companyIdStr).append('/');
            File newFileDir = new File(newFileDirBuilder.toString());
            FileUtils.copyFileToDirectory(tempFile, newFileDir, false);
            StringBuilder newFileBuilder = new StringBuilder(this.aliasFileUrl.length() + companyIdStr.length() + fileName.length() + 1);
            newFileBuilder.append(this.aliasFileUrl).append(companyIdStr).append('/').append(fileName);
            newFilePath = newFileBuilder.toString();
            tempFile.delete();
        } else {
            String filePath = this.localTempDirectory.concat(fileName);
            this.logger.error("There was an error creating file. Cause: can not create dir {}", filePath);
            throw new RuntimeException("There was an error creating file. Cause: can not create dir".concat(filePath));
        }
        return newFilePath;
    }

    /**
     * 生成一个唯一的文件名
     * @param fileName
     * @return 
     */
    private String createFileName(final String fileExtend) {
        String newFileName = "";//return
        UUID uuid = UUID.randomUUID();
        newFileName = uuid.toString().concat(fileExtend);
        return newFileName;
    }

    /**
     * 创建临时图片
     * @param images
     * @return 
     */
    @Override
    public String createTempImage(byte[] images) throws Exception {
        String newFileName = this.createFileName(this.gifExtend);
        File newFile = new File(this.localTempDirectory.concat(newFileName));
        newFile.createNewFile();
        FileUtils.writeByteArrayToFile(newFile, images);
        StringBuilder requestUrlBuilder = new StringBuilder(this.aliasFileUrl.length() + newFileName.length() + 5);
        requestUrlBuilder.append(aliasFileUrl).append(tempName).append('/').append(newFileName);
        String requestUrl = requestUrlBuilder.toString();
        return requestUrl;
    }
}
