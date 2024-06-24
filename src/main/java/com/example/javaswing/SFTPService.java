package com.example.javaswing;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Service
public class SFTPService {


    public void performSFTPTask(Date date) {
        if (date == null) {
            System.out.println("Date is null, cannot proceed with SFTP task.");
            return;
        }

        try {
            ChannelSftp channelSftp = connectSftp();
//            // Upload a file
//            String localFile = "path/to/local/file.txt";
//            String remoteFile = "/path/on/server/file.txt";
//            channelSftp.put(new FileInputStream(localFile), remoteFile);

            // Format date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyy");
            String formattedDate = dateFormat.format(date);
            String remoteFile = "/DOWNLOAD/RECONCILIATION/" + formattedDate + "_OCEANBANK_MOCA.dat";
            String downloadedFile = "C:/test/hello/" + formattedDate + "_OCEANBANK_MOCA.dat";

            // Download a file
            channelSftp.get(remoteFile, Files.newOutputStream(Paths.get(downloadedFile)));
        } catch (Exception exception) {
            System.out.println("Exception : " + exception.getMessage());
        }
    }

    private ChannelSftp connectSftp() throws JSchException {
        JSch jsch = new JSch();
        String REMOTE_HOST = "sftp.moca.vn";
        String USERNAME = "oceanbank";
        String PASSWORD = "OJBbank123789!@#";
        int PORT = 65535;

        Session jschSession = jsch.getSession(USERNAME, REMOTE_HOST, PORT);

        // Set StrictHostKeyChecking to no to avoid UnknownHostKey issue
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        jschSession.setConfig(config);
        jschSession.setPassword(PASSWORD);
        jschSession.connect();

        ChannelSftp channelSftp = (ChannelSftp) jschSession.openChannel("sftp");
        channelSftp.connect();

        return channelSftp;
    }
}
