package ir.tamuk.reservation.utils;


public class NullEmptyCheck {
    // method check if string is null or empty
    public static Boolean isNull(String str) {

        // check if string is null
        return str == null;

    }

    public static Boolean isEmpty(String str){
        // check if string is empty
        return str.isEmpty();
    }

    public static Boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty();
    }
}
