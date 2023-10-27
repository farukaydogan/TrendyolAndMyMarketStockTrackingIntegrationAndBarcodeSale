package com.fta.stock.tracking.helper;


import java.util.Date;

public class GetNowAndYesterdayTimestamp {

    public long getNow() {
        return System.currentTimeMillis();
    }

    public long getYesterday() {
        return System.currentTimeMillis() - 86400000; // 24 * 60 * 60 * 1000
    }

    public Date returnThreeHourBefore(long timestamp){

        long ucSaatMilisaniye = 3 * 3600 * 1000;
        long yeniZamanDamgasi = timestamp - ucSaatMilisaniye;

        return  new Date(yeniZamanDamgasi);
    }
}
