package com.example.javaswing.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DOISOAT")
public class DoiSoat {
    @Id
    @Column
    private String TRANSID;
    @Column
    private String MTI;
    @Column
    private String BANKID;
    @Column
    private String CUSTID;
    @Column
    private String AMT;
    @Column
    private String CCY;
    @Column
    private String TOKEN;
    @Column
    private String TDATE;
    @Column
    private String RRCMOCA;
    @Column
    private String RRCOJB;
    @Column
    private String RRC;
    @Column
    private String RDESC;
    @Column
    private String TRANTIME;
    @Column
    private String RFAMT;
}
