package com.example.javaswing.Repository;

import com.example.javaswing.Entity.ControlDataTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ControlDataTransferRepo extends JpaRepository<ControlDataTransfer,Integer> {
    @Query(value = "SELECT ctid, transtype, providerid, providercustid, token, amount, description, transid, transtime,\n" +
            "       rescode, resdesc, refno, xref, isotp, otp, idotp, dsid, banktransid, verifytransid, merchantid, \n" +
            "       status, createdate, createby \n" +
            "FROM controldatatransfer \n" +
            "WHERE trunc(createdate) = trunc(:date)\n" +
            "  AND transid NOT IN (SELECT transid FROM controldatatransfer WHERE isotp IN ('1')) \n" +
            "  AND providerid = '112'",nativeQuery = true)
    List<ControlDataTransfer> getListCDT(@Param("date") Date date);

//    @Query("SELECT NEW com.example.javaswing.Entity.CONTROLDATATRANSFER(c.CTID, c.TRANSTYPE, c.PROVIDERID, c.PROVIDERCUSTID, c.TOKEN, c.AMOUNT, c.DESCRIPTION, c.TRANSID, c.TRANSTIME, " +
//            "c.RESCODE, c.RESDESC, c.REFNO, c.XREF, c.ISOTP, c.OTP, c.IDOTP, c.DSID, c.BANKTRANSID, c.VERIFYTRANSID, c.MERCHANTID, " +
//            "c.STATUS, c.CREATEDATE, c.CREATEBY) " +
//            "FROM CONTROLDATATRANSFER c " +
//            "WHERE FUNCTION('TRUNC', c.CREATEDATE) = FUNCTION('TRUNC', CURRENT_DATE - 10) " +
//            "AND c.TRANSID NOT IN (SELECT subc.TRANSID FROM CONTROLDATATRANSFER subc WHERE subc.ISOTP IN ('1')) " +
//            "AND c.PROVIDERID = '112'")
//    List<ControlDataTransfer> getListCDT();
}
