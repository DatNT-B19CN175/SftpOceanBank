package com.example.javaswing;

import com.jcraft.jsch.*;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Log4j2
public class SftpDAO {
    public static KetNoi getInfo(){
        KetNoi ketNoi = null;
        try {
            Properties dbConfig = new Properties();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            dbConfig.load(classLoader.getResourceAsStream("sftpmoca.properties"));
            String host = dbConfig.getProperty("sftp.host");
            int port = Integer.parseInt(dbConfig.getProperty("sftp.port"));
            String username = dbConfig.getProperty("sftp.username");
            String password = dbConfig.getProperty("sftp.password");
            String directory = dbConfig.getProperty("sftp.directory");
            String destination = dbConfig.getProperty("sftp.destination");
            ketNoi = new KetNoi();
            ketNoi.setHost(host);
            ketNoi.setPort(port);
            ketNoi.setUsername(username);
            ketNoi.setPassword(password);
            ketNoi.setDirectory(directory);
            ketNoi.setDestination(destination);
        } catch (IOException e) {
            log.error("Cannot connect to spft.moca.vn. {}", e.getMessage());
//            System.out.println("Exception: " + e.getMessage());
//            e.printStackTrace();
        }
        return ketNoi;
    }

    public static ChannelSftp connect(String host, int port, String username, String password){
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(username, host, port);
            log.info("Session created.");
            //System.out.println("Session created. ");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            log.info("Session connected.");
            log.info("Opening channel.");
//            System.out.println("Session connected.");
//            System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            log.info("Connected to {}.", host);
            //System.out.println("Connected to " + host + ".");
        }catch (Exception e){
//            System.out.println("Exception: " + e.getMessage());
//            e.printStackTrace();
            log.error("Exception occurred while connecting SFTP. {}", e.getMessage());
        }
        return sftp;
    }

    public static void upload(String directory, String uploadFile, ChannelSftp sftp){
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            FileInputStream fileInputStream = new FileInputStream(file);
            sftp.put(fileInputStream, file.getName());
            fileInputStream.close();
            log.info("Upload file '{}' to directory '{}' on SFTP successfully", file.getName(), directory);
            //System.out.println("file da duoc tai len sftp");
            fileInputStream = null;
        }catch (SftpException e){
            //System.out.println("Exception: " + e.getMessage());
            //e.printStackTrace();
            log.error("SFTP error occurred while uploading file '{}' to directory '{}': {}", uploadFile, directory, e.getMessage());
        }catch (IOException e){
            log.error("IO error occurred while reading file '{}': {}", uploadFile, e.getMessage());
        }
    }
}
