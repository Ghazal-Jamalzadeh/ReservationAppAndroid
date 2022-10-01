package ir.tamuk.reservation.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTime {

    //region "DateTime"
    private static int jY, jM, jD;
    private static int gY, gM, gD;
    private static int leap, march;

    public static class YearMonthDate {

        public YearMonthDate(int year, int month, int date) {
            this.year = year;
            this.month = month;
            this.date = date;
        }

        private int year;
        private int month;
        private int date;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public String toString() {
            return getYear() + "/" + getMonth() + "/" + getDate();
        }
    }

    public static DateTime.YearMonthDate GregorianToPersian(int year, int month, int day) {
        int jd = JG2JD(year, month, day, 0);
        JD2Jal(jd);
        return new DateTime.YearMonthDate(jY, jM, jD);
    }

    private static void JD2Jal(int JDN) {
        JD2JG(JDN, 0);

        jY = gY - 621;
        JalCal(jY);

        int JDN1F = JG2JD(gY, 3, march, 0);
        int k = JDN - JDN1F;
        if (k >= 0) {
            if (k <= 185) {
                jM = 1 + k / 31;
                jD = (k % 31) + 1;
                return;
            } else {
                k = k - 186;
            }
        } else {
            jY = jY - 1;
            k = k + 179;
            if (leap == 1)
                k = k + 1;
        }

        jM = 7 + k / 30;
        jD = (k % 30) + 1;
    }

    private static void JalCal(int jY) {
        march = 0;
        leap = 0;

        int[] breaks = {-61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
                1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178};

        gY = jY + 621;
        int leapJ = -14;
        int jp = breaks[0];

        int jump = 0;
        for (int j = 1; j <= 19; j++) {
            int jm = breaks[j];
            jump = jm - jp;
            if (jY < jm) {
                int N = jY - jp;
                leapJ = leapJ + N / 33 * 8 + (N % 33 + 3) / 4;

                if ((jump % 33) == 4 && (jump - N) == 4)
                    leapJ = leapJ + 1;

                int leapG = (gY / 4) - (gY / 100 + 1) * 3 / 4 - 150;

                march = 20 + leapJ - leapG;

                if ((jump - N) < 6)
                    N = N - jump + (jump + 4) / 33 * 33;

                leap = ((((N + 1) % 33) - 1) % 4);

                if (leap == -1)
                    leap = 4;
                break;
            }

            leapJ = leapJ + jump / 33 * 8 + (jump % 33) / 4;
            jp = jm;
        }
    }

    private static void JD2JG(int JD, int J1G0) {
        int i, j;

        j = 4 * JD + 139361631;

        if (J1G0 == 0) {
            j = j + (4 * JD + 183187720) / 146097 * 3 / 4 * 4 - 3908;
        }

        i = (j % 1461) / 4 * 5 + 308;
        gD = (i % 153) / 5 + 1;
        gM = ((i / 153) % 12) + 1;
        gY = j / 1461 - 100100 + (8 - gM) / 6;
    }

    private static int JG2JD(int year, int month, int day, int J1G0) {
        int jd = (1461 * (year + 4800 + (month - 14) / 12)) / 4
                + (367 * (month - 2 - 12 * ((month - 14) / 12))) / 12
                - (3 * ((year + 4900 + (month - 14) / 12) / 100)) / 4 + day
                - 32075;

        if (J1G0 == 0)
            jd = jd - (year + 100100 + (month - 8) / 6) / 100 * 3 / 4 + 752;

        return jd;
    }

    /*public static String getDateTime(String newsDateTime, TimeZone timeZone) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newsTime = format.parse(newsDateTime.substring(0, newsDateTime.length()));
        long miliseconds = newsTime.getTime();
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.format(new Date(miliseconds));
        format.setTimeZone(timeZone);
        String time = format.format(new Date(miliseconds));
//        if (timeZone.getID().contains("Tehran")) {
        Tools.YearMonthDate yearMonthDateGregorian = new Tools.YearMonthDate(Integer.parseInt(time.substring(0, 4)), Integer.parseInt(time.substring(5, 7)), Integer.parseInt(time.substring(8, 10)));
        Tools.YearMonthDate yearMonthDateJalali = GregorianToPersian(yearMonthDateGregorian.year, yearMonthDateGregorian.month, yearMonthDateGregorian.date);
        return yearMonthDateJalali.year + "/" + yearMonthDateJalali.month + "/" + yearMonthDateJalali.date + " ";
//        } else {
//            return time.substring(0, 16).replace('-', '/');
//        }
    }*/

    public static String getDate(String newsDateTime, TimeZone timeZone) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        Date newsTime = format.parse(newsDateTime.substring(0, newsDateTime.length() - 8) + "+0000");
        long miliseconds = newsTime.getTime();
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.format(new Date(miliseconds));
        format.setTimeZone(timeZone);
        String time = format.format(new Date(miliseconds));
//        if (timeZone.getID().contains("Tehran")) {
        YearMonthDate yearMonthDateGregorian = new YearMonthDate(Integer.parseInt(time.substring(0, 4)), Integer.parseInt(time.substring(5, 7)), Integer.parseInt(time.substring(8, 10)));
        YearMonthDate yearMonthDateJalali = GregorianToPersian(yearMonthDateGregorian.year, yearMonthDateGregorian.month, yearMonthDateGregorian.date);
        return yearMonthDateJalali.year + "/" + yearMonthDateJalali.month + "/" + yearMonthDateJalali.date;
//        } else {
//            return time.substring(0, 16).replace('-', '/');
//        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    //endregion

    public static String datePickerGeorgianDateConverter(int y , int m , int d){
        StringBuilder date =  new StringBuilder() ;

        date.append(y) ;
        date.append("-") ;

        if (m < 10){
            date.append(0) ;
        }

        date.append(m) ;
        date.append("-") ;

        if (d < 10){
            date.append(0) ;
        }

        date.append(d);

        date.append("T00:00:00.263Z") ;

        return date.toString() ;
    }

    public static String getPersianDate(String date) {
        String persianDate = "";
        try {

            persianDate = DateTime.getDate(date, TimeZone.getTimeZone("Asia/Tehran"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return persianDate;
    }


    public static String getTime(String date) {
        String h = date.substring(date.indexOf('T') + 1, date.indexOf(':'));
        String[] arrOfStrTime = date.split(":");
        String m = arrOfStrTime[1];

        String time = h + ":" + m;

        if (time.trim().startsWith("0")) {
            time = time.substring(1);
        }
        return time;
    }
}
