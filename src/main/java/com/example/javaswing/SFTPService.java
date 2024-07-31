package com.example.javaswing;

import com.example.javaswing.DTO.DoiSoatDTO;
import com.example.javaswing.Entity.ControlDataTransfer;
import com.example.javaswing.Entity.DoiSoat;
import com.example.javaswing.Entity.GDMOCA;
import com.example.javaswing.Repository.ControlDataTransferRepo;
import com.example.javaswing.Repository.DoiSoatRepo;
import com.example.javaswing.Repository.GDMOCARepo;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class SFTPService {
    @Autowired
    private GDMOCARepo gdmocaRepo;
    @Autowired
    private ControlDataTransferRepo controlDataTransferRepo;
    @Autowired
    private DoiSoatRepo doiSoatRepo;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SchedulerToggle schedulerToggle;

    //GET FILE
    public void getFile(Date date) {
        if (date == null) {
            System.out.println("Date is null, cannot proceed with SFTP task.");
            return;
        }
        if (!schedulerToggle.isAutoRunEnabled()){
            LocalDateTime now = LocalDateTime.now();
            System.out.println("Manual task perform at " + now);
            schedulerToggle.setLastRunTime(now);
        }
        try {
            ChannelSftp channelSftp = connectSftp();

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

    //READ FILE
    @Transactional
    public void readFile(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyy");
        String formattedDate = dateFormat.format(date);
        String filename = "C:/test/hello/" + formattedDate + "_OCEANBANK_MOCA.dat";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<GDMOCA> listGDMOCA = new ArrayList<>();
        String line;
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line.startsWith("HR[PRO]") || line.startsWith("TR[NOT]")) {
                System.out.println("qk" + line);
                continue;
            }
            GDMOCA gd = new GDMOCA();
            try {
                System.out.println("lay:" + line);
                String[] dt = line.split(Pattern.quote("["));
                for (int i = 0; i < dt.length; i++) {
                    System.out.println("dt[" + i + "]=" + dt[i].trim());
                    String[] detail = dt[i].split(Pattern.quote("]"));
                    for (int j = 0; j < detail.length; j++) {
                        if (i == 1)
                            gd.setMTI(detail[1].trim());
                        if (i == 2)
                            gd.setTRANSID(detail[1].trim());
                        if (i == 3)
                            gd.setBANKID(detail[1].trim());
                        if (i == 4)
                            gd.setCUSTID(detail[1].trim());
                        if (i == 5)
                            gd.setAMT(detail[1].trim());
                        if (i == 6)
                            gd.setCCY(detail[1].trim());
                        if (i == 7)
                            gd.setTOKEN(detail[1].trim());
                        if (i == 8)
                            gd.setTDATE(detail[1].trim());
                        if (i == 9)
                            gd.setRRC(detail[1].trim());
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
                e.printStackTrace();
            }
            listGDMOCA.add(gd);
        }
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
        saveGDMOCA(listGDMOCA);
    }

    private void saveGDMOCA(List<GDMOCA> gdmocaList){
        try {
            for(GDMOCA gdmoca : gdmocaList){
                if(gdmoca != null){
                    SimpleDateFormat formatted = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = formatted.parse(gdmoca.getTDATE().trim());
                    SimpleDateFormat formatted1 = new SimpleDateFormat("dd/MM/yyyy");
                    String txtDate = formatted1.format(date);
                    Date date1 = formatted1.parse(txtDate);
//                    Date tranTime = new Date(parsed.getTime());
                    gdmoca.setTRANTIME(date1);
                    gdmocaRepo.saveGDMOCA(gdmoca.getTRANSID(),gdmoca.getMTI(),gdmoca.getBANKID(),
                            gdmoca.getCUSTID(),gdmoca.getAMT(),gdmoca.getCCY(),gdmoca.getTOKEN(),
                            gdmoca.getTDATE(),gdmoca.getRRC(),gdmoca.getTRANTIME());
                }
                else {
                    System.out.println("There is no data");
                }
            }
        }catch (ParseException e){
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //CONTROL DATA
    @Transactional
    public void controlData(Date dtRun){
        List<GDMOCA> gdmocaList = null;
        List<ControlDataTransfer> controlDataTransferList = controlDataTransferRepo.getListCDT(dtRun);
        SimpleDateFormat formatter12 = new SimpleDateFormat("dd-MM-yyyy");
        String txtDate = formatter12.format(dtRun);
        gdmocaList = new ArrayList<>();
        for(ControlDataTransfer controlDataTransfer : controlDataTransferList){
            GDMOCA gdmoca = new GDMOCA();
            String strTransId = controlDataTransfer.getTRANSID();
            String token = controlDataTransfer.getTOKEN();
            int transtype = controlDataTransfer.getTRANSTYPE();
            String refno = controlDataTransfer.getREFNO();
            String amount = controlDataTransfer.getAMOUNT();
            Date createDate = controlDataTransfer.getCREATEDATE();
            String rrcojb = controlDataTransfer.getSTATUS();
            String description = controlDataTransfer.getDESCRIPTION();
            String rdesc = controlDataTransfer.getRESDESC();
            GDMOCA gdmoca1 = gdmocaRepo.getGDMOCA(strTransId,token,txtDate);
            if(gdmoca1 != null){
                String mti = gdmoca1.getMTI();
                String bankId = gdmoca1.getBANKID();
                String custId = gdmoca1.getCUSTID();
                String amt = gdmoca1.getAMT();
                String ccy = gdmoca1.getCCY();
                String tdate = gdmoca1.getTDATE();
                String rrcmoca = gdmoca1.getRRC();
                Date tranTime = gdmoca1.getTRANTIME();
                gdmoca.setMTI(mti);
                gdmoca.setTRANSID(strTransId);
                gdmoca.setBANKID(bankId);
                gdmoca.setCUSTID(custId);
                gdmoca.setAMT(amt);
                gdmoca.setCCY(ccy);
                gdmoca.setTOKEN(token);
                gdmoca.setTDATE(tdate);
                gdmoca.setRRCMOCA(rrcmoca);
                gdmoca.setRRCOJB(rrcojb);
                if(rrcmoca.trim().equals("0000") && rrcojb.trim().equals("00")){
                    gdmoca.setRRC("0000");
                }
                gdmoca.setRDESC(description);
                gdmoca.setTRANTIME(tranTime);
            } else {
                String mti;
                if(transtype == 1){
                    mti = "1210";
                } else if (transtype == 0) {
                    mti = "1211";
                } else {
                    mti = "1212";
                }
                Date trant = new Date(createDate.getTime());
                System.out.println("kq:" + trant);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                System.out.println(formatter.format(trant));
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                gdmoca.setMTI(mti);
                gdmoca.setTRANSID(strTransId);
                gdmoca.setBANKID(valueRegister(refno));
                gdmoca.setCUSTID("unknow");
                gdmoca.setAMT(amount);
                gdmoca.setCCY("VND");
                gdmoca.setTOKEN(token);
                gdmoca.setTDATE(formatter.format(trant));
                gdmoca.setRRCMOCA("unknow");
                gdmoca.setRRCOJB(rrcojb);
                if (rrcojb.trim().equals("00")){
                    gdmoca.setRRC("0116");
                    gdmoca.setRDESC(description);
                } else {
                    gdmoca.setRRC("0117");
                    gdmoca.setRDESC(rdesc);
                }
                gdmoca.setTRANTIME(createDate);
            }
            System.out.println("strTransId:" + strTransId);
            System.out.println("token:" + token);
            gdmocaList.add(gdmoca);
        }
        insertDoiSoat(gdmocaList);
    }

    public static String valueRegister(String e) {
        String ev = (e != null && !e.equals("")) ? e : "unknow";
        return ev;
    }

    private void insertDoiSoat(List<GDMOCA> gdmocaList){
        for(GDMOCA gdmoca : gdmocaList){
            DoiSoat doiSoat = new DoiSoat();
            doiSoat.setMTI(gdmoca.getMTI());
            doiSoat.setTRANSID(gdmoca.getTRANSID());
            doiSoat.setBANKID(gdmoca.getBANKID());
            doiSoat.setCUSTID(gdmoca.getCUSTID());
            doiSoat.setAMT(gdmoca.getAMT());
            doiSoat.setCCY(gdmoca.getCCY());
            doiSoat.setTOKEN(gdmoca.getTOKEN());
            doiSoat.setTDATE(gdmoca.getTDATE());
            doiSoat.setRRCMOCA(gdmoca.getRRCMOCA());
            doiSoat.setRRCOJB(gdmoca.getRRCOJB());
            doiSoat.setRRC(gdmoca.getRRC());
            if(gdmoca.getRDESC() != null){
                doiSoat.setRDESC(gdmoca.getRDESC());
            }else {
                doiSoat.setRDESC("");
            }
            doiSoat.setTRANTIME(String.valueOf(gdmoca.getTRANTIME()));
            String rfamt ="0";
            if (gdmoca.getMTI().trim().equals("1212") && gdmoca.getRRCMOCA().trim().equals("unknown") && gdmoca.getRRCOJB().trim().equals("00")){
                rfamt = "-" + gdmoca.getAMT();
            }
            doiSoat.setRFAMT(rfamt);
            doiSoatRepo.save(doiSoat);
            System.out.println("Ok insert xong");
        }
    }

    //UPLOAD SFTP
    public void uploadSftp(Date date){
        List<String> data = new ArrayList<>();
        int i=0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //ngay hien tai trong thang tru di 1
        calendar.add(5, -1);
        SimpleDateFormat formatted1 = new SimpleDateFormat("yyyy-MM-dd");
        String strDTRun = formatted1.format(calendar.getTime());
        String sql = "SELECT mti, transid, bankid, custid, amt, ccy, token, tdate, rrcmoca, rrcojb, rrc, rdesc, trantime,\n" +
                "       DBMS_OBFUSCATION_TOOLKIT.md5(input => UTL_I18N.STRING_TO_RAW(transid ||'T' ||bankid ||'T' ||token,'AL32UTF8')) AS ky \n" +
                "FROM doisoat \n" +
                "WHERE SUBSTR(trantime,1,10)= '"+strDTRun+"' AND rrc not in('0117') \n" +
                "ORDER BY transid ASC";
        Query query = entityManager.createNativeQuery(sql);
        SimpleDateFormat formattedDTHeader = new SimpleDateFormat("yyyyMMdd");
        data.add("HR[PRO]     112[DATE]" + formattedDTHeader.format(date));
        List<Object[]> results = query.getResultList();
        for (Object[] result : results){
            i++;
            DoiSoatDTO doiSoatDTO = new DoiSoatDTO();
            doiSoatDTO.setMTI((String) result[0]);
            doiSoatDTO.setTRANSID((String) result[1]);
            doiSoatDTO.setBANKID((String) result[2]);
            doiSoatDTO.setCUSTID((String) result[3]);
            doiSoatDTO.setAMT((String) result[4]);
            doiSoatDTO.setCCY((String) result[5]);
            doiSoatDTO.setTOKEN((String) result[6]);
            doiSoatDTO.setTDATE((String) result[7]);
            doiSoatDTO.setRRCMOCA((String) result[8]);
            doiSoatDTO.setRRCOJB((String) result[9]);
            doiSoatDTO.setRRC((String) result[10]);
            doiSoatDTO.setRDESC((String) result[11]);
            doiSoatDTO.setTRANTIME((String) result[12]);
            byte[] byteArray = (byte[]) result[13];
            String strKy = bytesToHex(byteArray);
            doiSoatDTO.setKy(strKy);
            String mti = doiSoatDTO.getMTI();
            String transid = doiSoatDTO.getTRANSID();
            String bankid = doiSoatDTO.getBANKID();
            String custid = doiSoatDTO.getCUSTID();
            String amt = doiSoatDTO.getAMT();
            String tdate = doiSoatDTO.getTDATE();
            String rrc = doiSoatDTO.getRRC();
            String CSR = doiSoatDTO.getKy();
            data.add("DR[MTI]" + mti + "[TRANSID]" + transid + "[BANKID]" + padLeft(bankid, 20, " ") +
                    "[CUSTID]" + padLeft(custid, 20, " ") + "[AMT]" + padLeft(amt, 12, " ") +
                    "[TDATE]" + tdate + "[RRC]" + padLeft(rrc, 4, " ") + "[CSR]" + CSR);
        }
        SimpleDateFormat formattedDTFile = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        data.add("TR[NOT]       " + i + "[DATE]" + formattedDTFile.format(date) + "[CSF]" + maHoa("DS_" + i + "_" + getCurrentDatev1()));
        SimpleDateFormat formatted2 = new SimpleDateFormat("MMddyy");
        writeToFile(data, "C:\\ctroldt\\" + formatted2.format(date) + "_MOCA_OCEANBANK_TEST.dat");
        KetNoi ketNoi = SftpDAO.getInfo();
        ChannelSftp channelSftp = SftpDAO.connect(ketNoi.getHost(), ketNoi.getPort(), ketNoi.getUsername(), ketNoi.getPassword());
        SftpDAO.upload("/UPLOAD/", "C:\\ctroldt\\" + formatted2.format(date) + "_MOCA_OCEANBANK_TEST.dat", channelSftp);
    }

    //chuyen tu dang byte[] sang thap luc phan (hexadecimal)
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private static String padLeft(String str, int length, String padChar){
        String pad = "";
        for (int i = 0; i < length; i++){
            pad = pad + padChar;
        }
        return pad.substring(str.length()) + str;
    }

    private static String maHoa(String password) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            for (byte b : hashInBytes){
                sb.append(String.format("%02x", new Object[] { Byte.valueOf(b) }));
            }
            System.out.println(sb.toString() + " do dai:" + sb.toString().length());
        }catch (NoSuchAlgorithmException e){
            System.out.println("Exception:" + e.getMessage());
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static String getCurrentDatev1(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        System.out.println(formatter.format(date));
        return formatter.format(date);
    }

    private static void writeToFile(List list, String path){
        BufferedWriter out = null;
        try {
            File file = new File(path);
            out = new BufferedWriter(new FileWriter(file, false));
            for (Object s : list){
                out.write((String) s);
                out.newLine();
            }
            out.close();
        }catch (IOException e){
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Kich hoat chay tu dong
    @Scheduled(cron = "0 0 14 * * ?")
    @Transactional
    public void enableAuto() {
        if (schedulerToggle.isAutoRunEnabled()){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastRunTime = schedulerToggle.getLastRunTime();
            if (lastRunTime == null || lastRunTime.toLocalDate().isBefore(now.toLocalDate())){
                System.out.println("Scheduled task performed at 9:00 AM");
                Calendar calendar1 = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();
                calendar2.add(Calendar.DAY_OF_YEAR,-1);
                Date currentDate = calendar1.getTime();
                Date previousDate = calendar2.getTime();
                getFile(currentDate);
                readFile(currentDate);
                controlData(previousDate);
                uploadSftp(currentDate);
                schedulerToggle.setLastRunTime(now);
            }
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
