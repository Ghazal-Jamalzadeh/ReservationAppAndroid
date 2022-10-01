package ir.tamuk.reservation.enums;

public enum StatusReserve {

    DONE("done" , "انجام شده") ,
    RESERVED("reserved" , "ثبت شده") ,
    CANCELED("canceled" , "لغو شده");

    public final String label;
    public final String labelFa;

    private StatusReserve(String label , String labelFa) {
        this.label = label;
        this.labelFa = labelFa ;
    }


}
