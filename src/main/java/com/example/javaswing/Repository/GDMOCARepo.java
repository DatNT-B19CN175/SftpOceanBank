package com.example.javaswing.Repository;

import com.example.javaswing.Entity.GDMOCA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface GDMOCARepo extends JpaRepository<GDMOCA,String> {
    //JPQL Query
    @Query("SELECT NEW com.example.javaswing.Entity.GDMOCA(g.TRANSID, g.MTI, g.BANKID, g.CUSTID, g.AMT, g.CCY, g.TOKEN, g.TDATE, g.RRC, g.TRANTIME) " +
            "FROM GDMOCA g " +
            "WHERE g.TRANSID = :transid " +
            "      AND g.TOKEN = :token " +
            "      AND FUNCTION('TRUNC', g.TRANTIME) = FUNCTION('TRUNC', FUNCTION('TO_DATE', :txtDate, 'DD-MM-YYYY'))")
    GDMOCA getGDMOCA(@Param("transid") String transId, @Param("token") String token, @Param("txtDate") String txtDate);

    @Modifying
    @Query(value = "INSERT INTO GDMOCA (TRANSID, MTI, BANKID, CUSTID, AMT, CCY, TOKEN, TDATE, RRC, TRANTIME)" +
            "VALUES(:transid, :mti, :bankid, :custid, :amt, :ccy, :token, :tdate, :rrc, :trantime)",nativeQuery = true)
    void saveGDMOCA(@Param("transid") String transid, @Param("mti") String mti, @Param("bankid") String bankid,
                    @Param("custid") String custid, @Param("amt") String amt, @Param("ccy") String ccy,
                    @Param("token") String token, @Param("tdate") String tdate, @Param("rrc") String rrc,
                    @Param("trantime") Date trantime);

//    TRUNC(TO_DATE(TDATE, 'DD/MM/YYYY HH24:MI:SS'))
//    @Query(value = "SELECT g.MTI, g.TRANSID, g.BANKID, g.CUSTID, g.AMT, g.CCY, g.TOKEN, g.TDATE, g.RRC, g.TRANTIME \n" +
//            "FROM GDMOCA g \n" +
//            "WHERE g.TRANSID = :transid \n" +
//            "      AND g.TOKEN = :token\n" +
//            "      AND TRUNC(g.TRANTIME) = TRUNC(TO_DATE(:txtDate,'DD-MM-YYYY'))",nativeQuery = true)

}
