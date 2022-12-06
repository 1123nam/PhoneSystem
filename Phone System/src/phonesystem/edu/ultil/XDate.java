/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonesystem.edu.ultil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author HienTran
 */
public class XDate {

    static SimpleDateFormat formater = new SimpleDateFormat();

    public static Date toDate(String date, String pattern) {
        try {
            formater.applyPattern(pattern);
            return formater.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(Date date, String... pattern) {
        try {
            if (pattern.length > 0) {
                formater.applyPattern(pattern[0]);
            }
            if (date == null) {
                return "";
            }

        } catch (Exception e) {
            throw new RuntimeException();
        }
        return formater.format(date);
    }

    public static Date addDays(Date date, long days) {
        date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
        return date;
    }

    public static Date now() {
        return new Date();
    }

    public static boolean isAgeEnough(Date dob) {
        int yearDOB = Integer.parseInt(XDate.toString(dob, "dd/MM/yyyy").substring(6, XDate.toString(dob, "dd/MM/yyyy").length()));
        int yearNow = Integer.parseInt(XDate.toString(XDate.now(), "dd/MM/yyyy").substring(6, XDate.toString(XDate.now(), "dd/MM/yyyy").length()));
        return yearNow - yearDOB >= 18;
    }

    public static void main(String[] args) {
        Date now = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aa");
//        String dateSt = sdf.format(now);
//        
//        String hSt = dateSt.substring(0, 2);
//        String mSt = dateSt.substring(3, 5);
//        
//        int hInt = Integer.parseInt(hSt);
//        int mInt = Integer.parseInt(mSt);
//        
//        boolean isAM = dateSt.endsWith("AM");
//        
//        System.out.println(now);
//        System.out.println(dateSt);
//        System.out.println(hSt);
//        System.out.println(mSt);
//        System.out.println(hInt);
//        System.out.println(mInt);

        XDate.isAgeEnough(XDate.toDate("01/01/2005", "dd/MM/yyyy"));
    }
}
