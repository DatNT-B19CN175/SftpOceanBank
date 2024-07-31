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
@Table(name = "GDMOCA")
public class GDMOCA {
    @Id
    @Column(name = "TRANSID")
    private String TRANSID;
    @Column(name = "MTI")
    private String MTI;
    @Column(name = "BANKID")
    private String BANKID;
    @Column(name = "CUSTID")
    private String CUSTID;
    @Column(name = "AMT")
    private String AMT;
    @Column(name = "CCY")
    private String CCY;
    @Column(name = "TOKEN")
    private String TOKEN;
    @Column(name = "TDATE")
    private String TDATE;
    @Column(name = "RRC")
    private String RRC;
    @Column(name = "RRCMOCA")
    private String RRCMOCA;
    @Column(name = "RRCOJB")
    private String RRCOJB;
    @Column(name = "RDESC")
    private String RDESC;
    @Column(name = "TRANTIME")
    private Date TRANTIME;

    public GDMOCA(String TRANSID, String MTI, String BANKID, String CUSTID, String AMT, String CCY, String TOKEN, String TDATE, String RRC, Date TRANTIME) {
        this.TRANSID = TRANSID;
        this.MTI = MTI;
        this.BANKID = BANKID;
        this.CUSTID = CUSTID;
        this.AMT = AMT;
        this.CCY = CCY;
        this.TOKEN = TOKEN;
        this.TDATE = TDATE;
        this.RRC = RRC;
        this.TRANTIME = TRANTIME;
    }
}
