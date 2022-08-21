package ir.tamuk.reservation.fragments.ui.reservation.adapter;

public class ReserveModel {
    public String id;
    public String time;
    public int reserved;
    public String service;

    public ReserveModel(String id,String time, int reserved, String service) {
        this.id = id;
        this.time = time;
        this.reserved = reserved;
        this.service = service;

    }

    public ReserveModel(String id) {
        this.id = id;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
