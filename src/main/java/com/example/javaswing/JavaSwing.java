package com.example.javaswing;


import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

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
    private JDateChooser dateChooser;
    private final SFTPService sftpService;

    public JavaSwing() {
        this.sftpService = new SFTPService();
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
        btnControlData = new JButton("Control Data");
        btnReadFile = new JButton("Read File");
        btnGetFile = new JButton("Get File");
        btnUploadSftp = new JButton("Upload Sftp");
        lbl1 = new JLabel("Ngày chạy đối soát manual: (Click chức năng chọn NO)");
        lbl2 = new JLabel("Ngân Hàng TNHH MTV Đại Dương");
        lbl3 = new JLabel("Phần mềm tra soát tự động");
        dateChooser = new JDateChooser();

        // Khởi tạo các thành phần giao diện
        init1();
        init2();
        init3();
    }


    private void init1() {
        // Khởi tạo và thêm nút
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        btnControlData.setPreferredSize(new Dimension(120, 100));
        btnReadFile.setPreferredSize(new Dimension(120, 100));
        btnGetFile.setPreferredSize(new Dimension(120, 100));
        btnUploadSftp.setPreferredSize(new Dimension(120, 100));
        panel1.add(btnControlData);
        panel1.add(btnReadFile);
        panel1.add(btnGetFile);
        panel1.add(btnUploadSftp);

        // Thêm hành động cho nút "Get File"
        btnGetFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFile();
            }
        });

        panelMain.add(panel1, BorderLayout.SOUTH);
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

    private void getFile() {
        //todo code here
        Date selectedDate = dateChooser.getDate();
        sftpService.performSFTPTask(selectedDate);
        JOptionPane.showMessageDialog(this, "hello swing");
    }

//    private void createUIComponents() {
//        // TODO: place custom component creation code here
//    }
}
