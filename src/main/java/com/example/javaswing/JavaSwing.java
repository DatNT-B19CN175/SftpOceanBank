package com.example.javaswing;


import com.toedter.calendar.JDateChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

@Component
public class JavaSwing extends JFrame {
    private JPanel panelMain;
    private JPanel panel1;
    private JButton btnControlData;
    private JButton btnReadFile;
    private JButton btnGetFile;
    private JButton btnUploadSftp;
    private JPanel panel2;
    private JLabel lbl1;
    private JLabel lbl2;
    private JLabel lbl3;
    private JPanel panel3;
    private JButton btnEnabledAuto;
    private JButton btnDisabledAuto;
    private JPanel panel4;
    private JPanel southPanel;
    private JPanel panel5;
    private JLabel lbl4;
    private JPanel panel6;
    private JButton btnDeleteData;
    private JDateChooser dateChooser;
    @Autowired
    private SFTPService sftpService;
    @Autowired
    private SchedulerToggle schedulerToggle;

    public JavaSwing() {
//        this.sftpService = new SFTPService();
        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());
        setContentPane(panelMain);
        setTitle("Phan mem tra soat tu dong");
        setSize(750, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        //tao moi cac doi tuong
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        panel5 = new JPanel();
        panel6 = new JPanel();
        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        btnGetFile = new JButton("Get File");
        btnReadFile = new JButton("Read File");
        btnControlData = new JButton("Control Data");
        btnUploadSftp = new JButton("Upload Sftp");
        btnEnabledAuto = new JButton("Enable Auto");
        btnDisabledAuto = new JButton("Disable Auto");
        btnDeleteData = new JButton("Delete Data");
        lbl1 = new JLabel("Ngày chạy đối soát manual: (Click chức năng chọn NO)");
        lbl2 = new JLabel("Ngân Hàng TNHH MTV Đại Dương");
        lbl3 = new JLabel("Phần mềm tra soát tự động");
        lbl4 = new JLabel("Hệ thống đang chạy thủ công");
        dateChooser = new JDateChooser();

        panelMain.add(southPanel, BorderLayout.SOUTH);

        // Khởi tạo các thành phần giao diện
        init1();
        init2();
        init3();
        init4();
        init5();
        init6();
    }


    private void init1() {
        // Khởi tạo và thêm nút
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        btnGetFile.setPreferredSize(new Dimension(120, 80));
        btnReadFile.setPreferredSize(new Dimension(120, 80));
        btnControlData.setPreferredSize(new Dimension(120, 80));
        btnUploadSftp.setPreferredSize(new Dimension(120, 80));
        panel1.add(btnGetFile);
        panel1.add(btnReadFile);
        panel1.add(btnControlData);
        panel1.add(btnUploadSftp);

        // Thêm hành động cho nút "Get File"
        btnGetFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFile();
            }
        });

        // Them hanh dong cho nut "Read File"
        btnReadFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readFile();
            }
        });

        // Them hanh dong cho nut "Control Data"
        btnControlData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlData();
            }
        });

        //Them hanh dong cho nut "Upload Sftp"
        btnUploadSftp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadSftp();
            }
        });

        southPanel.add(panel1, BorderLayout.NORTH);
    }

    private void init2() {
        //Khoi tao va them chuc nang nhap ngay
        panel2.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        lbl1.setPreferredSize(new Dimension(320, 30));
        dateChooser.setPreferredSize(new Dimension(180, 30));
        panel2.add(lbl1);
        panel2.add(dateChooser);

        panelMain.add(panel2, BorderLayout.CENTER);
    }

    private void init3() {
        //Khoi tao va them phan tieu de
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        panel3.add(lbl2);
        panel3.add(lbl3);

        panelMain.add(panel3, BorderLayout.NORTH);
    }

    private void init4() {
        panel4.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        btnEnabledAuto.setPreferredSize(new Dimension(100,30));
        btnDisabledAuto.setPreferredSize(new Dimension(100,30));
        panel4.add(btnEnabledAuto);
        panel4.add(btnDisabledAuto);

        //Them hanh dong cho nut Enabled Auto
        btnEnabledAuto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAuto();
            }
        });

        //Them hanh dong cho nut Disabled Auto
        btnDisabledAuto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableAuto();
            }
        });

        southPanel.add(panel4, BorderLayout.WEST);
    }

    private void init5() {
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        lbl4.setHorizontalAlignment(SwingConstants.CENTER);
        lbl4.setPreferredSize(new Dimension(400,30));
        panel5.add(lbl4);

        southPanel.add(panel5, BorderLayout.CENTER);
    }

    private void init6(){
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 10,10));
        btnDeleteData.setPreferredSize(new Dimension(100,30));
        panel6.add(btnDeleteData);

        //Them hanh dong cho nut Delete Data
        btnDeleteData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        southPanel.add(panel6, BorderLayout.EAST);
    }

    private void controlData(){
        Date selectedDate = dateChooser.getDate();
        if (selectedDate != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            Date previousDate = calendar.getTime();
            sftpService.controlData(previousDate);
            JOptionPane.showMessageDialog(this,"Success!");
        }else {
            JOptionPane.showMessageDialog(this, "Date is null, cannot control data");
        }
    }

    private void readFile(){
        Date selectedDate = dateChooser.getDate();
        if (selectedDate != null){
            sftpService.readFile(selectedDate);
            JOptionPane.showMessageDialog(this,"Success!");
        }else {
            JOptionPane.showMessageDialog(this, "Date is null, cannot read file");
        }
    }

    private void getFile() {
        //todo code here
        Date selectedDate = dateChooser.getDate();
        if (selectedDate != null){
            sftpService.getFile(selectedDate);
            JOptionPane.showMessageDialog(this, "Success!");
        }
        else {
            JOptionPane.showMessageDialog(this, "Date is null, cannot get file");
        }
    }

    private void uploadSftp(){
        Date selectedDate = dateChooser.getDate();
        if (selectedDate != null){
            sftpService.uploadSftp(selectedDate);
            JOptionPane.showMessageDialog(this, "Success!");
        }else {
            JOptionPane.showMessageDialog(this, "Date is null, cannot upload file to SFTP");
        }
    }

    private void enableAuto() {
        schedulerToggle.setAutoRunEnabled(true);
        sftpService.enableAuto();
        lbl4.setText("Hệ thống đang chạy tự động lúc 14h00 mỗi ngày");
    }

    private void disableAuto() {
        schedulerToggle.setAutoRunEnabled(false);
        lbl4.setText("Hệ thống đang chạy thủ công");
    }

    private void deleteData(){
        Date selectedDate = dateChooser.getDate();
        if (selectedDate != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            Date previousDate = calendar.getTime();
            sftpService.deleteData(previousDate);
            JOptionPane.showMessageDialog(this, "Success!");
        }else {
            JOptionPane.showMessageDialog(this, "Date is null, cannot delete data");
        }
    }

//    private void createUIComponents() {
//        // TODO: place custom component creation code here
//    }
}
