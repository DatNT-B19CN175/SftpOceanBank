package com.example.javaswing.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CONTROLDATATRANSFER")
public class ControlDataTransfer {
    @Id
    @Column
    private int CTID;
    @Column
    private int TRANSTYPE;
    @Column
    private String PROVIDERID;
    @Column
    private String PROVIDERCUSTID;
    @Column
    private String TOKEN;
    @Column
    private String AMOUNT;
    @Column
    private String DESCRIPTION;
    @Column
    private String TRANSID;
    @Column
    private String TRANSTIME;
    @Column
    private String RESCODE;
    @Column
    private String RESDESC;
    @Column
    private String REFNO;
    @Column
    private String XREF;
    @Column
    private String ISOTP;
    @Column
    private String OTP;
    @Column
    private String IDOTP;
    @Column
    private String DSID;
    @Column
    private String BANKTRANSID;
    @Column
    private String VERIFYTRANSID;
    @Column
    private String MERCHANTID;
    @Column
    private String STATUS;
    @Column
    private Date CREATEDATE;
    @Column
    private String CREATEBY;
}
