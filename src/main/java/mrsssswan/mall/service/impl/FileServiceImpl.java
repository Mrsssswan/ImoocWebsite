package mrsssswan.mall.service.impl;

import com.jcraft.jsch.SftpException;
import mrsssswan.mall.service.IFileService;
import mrsssswan.mall.util.SFTPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileNameExtension = fileName.substring(fileName.indexOf(",")+1);
        //生成上传文件的文件名
        String uplaodFileName = UUID.randomUUID().toString()+fileNameExtension;
        logger.info("开始上传文件，上传的文件名：{},上传的路径：{}，新文件名：{}",fileName,path,uplaodFileName);
        File filedir = new File(path);
        if(!filedir.exists()){
            filedir.setWritable(true);
            filedir.mkdirs();
        }
        File targetFile = new File(path,uplaodFileName);
        try {
            //文件上传
            file.transferTo(targetFile);
            //文件上传到sftp服务器上
            SFTPUtils.uploadFile(path,targetFile);
            //上传之后删除文件
            targetFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return targetFile.getName();
    }
}
