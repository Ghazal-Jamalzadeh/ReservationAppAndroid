package ir.tamuk.reservation.utils;

import android.text.TextWatcher;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PriceFormat {
    public static String getNumberFromFormatPrice(String price) {
        String p1 = price.replaceAll("٬", "");
        String p2 = p1.replaceAll(",", "");
        String p3 = p2.replaceAll(" ", "");
        String p4 = p3.replaceAll("ریال", "");

        return p4;
    }

    public static void formatPrice(TextView edt, TextWatcher textWatcher, CharSequence charSequence) {
        try {
            String text = String.valueOf(charSequence);

            text = text.replaceAll(" ", "");
            text = text.replaceAll(",", "");
            text = text.replaceAll("٬", "");

            if (text.contains("ریال")) {
                text = text.replaceAll("ریال", "");
                long number = Long.parseLong(text);
                edt.removeTextChangedListener(textWatcher);
                edt.setText(PriceFormat.decimalFormat(number) + " ریال");
            } else if (text.contains("ریا")) {
                text = text.replaceAll("ریا", "");
                if (text.length() == 1) {
                    edt.removeTextChangedListener(textWatcher);
                    edt.setText("");
                } else {
                    long number = Long.parseLong(text.substring(0, text.length() - 1));
                    edt.removeTextChangedListener(textWatcher);
                    edt.setText(PriceFormat.decimalFormat(number) + " ریال");
                }
            } else if (text.contains("ریل") || text.contains("رال") || text.contains("یال")) {
                text = text.replaceAll("یال", "");
                text = text.replaceAll("رال", "");
                text = text.replaceAll("ریل", "");
                long number = Long.parseLong(text);
                edt.removeTextChangedListener(textWatcher);
                edt.setText(PriceFormat.decimalFormat(number) + " ریال");
            } else {
                long number = Long.parseLong(text);
                edt.removeTextChangedListener(textWatcher);
                edt.setText(PriceFormat.decimalFormat(number) + " ریال");
            }
            edt.addTextChangedListener(textWatcher);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String decimalFormat(double digit) {
        DecimalFormat df = new DecimalFormat("#,###,###,###,###");
        df.setRoundingMode(RoundingMode.CEILING);
        Number n = Math.abs(digit);
        Double d = n.doubleValue();
        return String.valueOf(df.format(d));
    }


}
