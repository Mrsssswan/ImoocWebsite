package mrsssswan.mall.util;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * sftp上传文件到服务器上
 */
public class SFTPUtils {
    private static transient Logger log = LoggerFactory.getLogger(SFTPUtils.class);
    private static ChannelSftp sftp;
    private static Session session;
    /** SFTP 登录用户名*/
    private static String username = PropertiesUtil.getProperty("sftp.user");
    /** SFTP 登录密码*/
    private static String password = PropertiesUtil.getProperty("sftp.pass");
    /** SFTP 服务器域名*/
    private static String domian = PropertiesUtil.getProperty("sftp.server.prefix");
    /** SFTP 端口*/
    private static int port = Integer.parseInt(PropertiesUtil.getProperty("sftp.user.port"));

    /**
     * 构造基于用户名和密码认证的sftp对象
     * @param username
     * @param password
     * @param domian
     * @param port
     */
    public SFTPUtils(String username, String password, String domian, int port) {
        this.username = username;
        this.password = password;
        this.domian = domian;
        this.port = port;
    }

    public SFTPUtils() {
    }

    public static void login(){
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, domian, port);
            log.info("Session is build");
            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictdomianKeyChecking", "no");

            session.setConfig(config);
            session.connect();
            log.info("Session is connected");

            Channel channel = session.openChannel("sftp");
            channel.connect();
            log.info("channel is connected");

            sftp = (ChannelSftp) channel;
            log.info(String.format("sftp server domian:[%s] port:[%s] is connect successfull", domian, port));
        } catch (JSchException e) {
            log.error("Cannot connect to specified sftp server : {}:{} \n Exception message is: {}", new Object[]{domian, port, e.getMessage()});
        }
    }

    /**
     * 关闭连接 server
     */
    public static void logout(){
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                log.info("sftp is closed already");
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                log.info("sshSession is closed already");
            }
        }
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     * @param directory
     * @param sftpFileName
     * @param input
     * @throws SftpException
     */
    public static void upload(String directory, String sftpFileName, InputStream input) throws SftpException{
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            log.warn("directory is not exist");
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        sftp.put(input, sftpFileName);
        log.info("file:{} is upload successful" , sftpFileName);
    }

    /**
     * 上传单个文件
     * @param directory
     * @param uploadFile
     * @throws FileNotFoundException
     * @throws SftpException
     */
    public static void uploadFile(String directory, File uploadFile) throws FileNotFoundException, SftpException{
        upload(directory, uploadFile.getName(), new FileInputStream(uploadFile));
    }

}
