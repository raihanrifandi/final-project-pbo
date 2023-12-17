package model;

import java.sql.Date;

public class Pembayaran {
    private int id_pembayaran;
    private Date tanggal_pembayaran;
    private int jumlah_yang_dibayarkan;

    public Pembayaran() {
    }
    
    public Pembayaran(int id_pembayaran, Date tanggal_pembayaran, int jumlah_yang_dibayarkan) {
        this.id_pembayaran = id_pembayaran;
        this.tanggal_pembayaran = tanggal_pembayaran;
        this.jumlah_yang_dibayarkan = jumlah_yang_dibayarkan;
    }

    public int getId_pembayaran() {
        return id_pembayaran;
    }

    public void setId_pembayaran(int id_pembayaran) {
        this.id_pembayaran = id_pembayaran;
    }

    public Date getTanggal_pembayaran() {
        return tanggal_pembayaran;
    }

    public void setTanggal_pembayaran(Date tanggal_pembayaran) {
        this.tanggal_pembayaran = tanggal_pembayaran;
    }

    public int getJumlah_yang_dibayarkan() {
        return jumlah_yang_dibayarkan;
    }

    public void setJumlah_yang_dibayarkan(int jumlah_yang_dibayarkan) {
        this.jumlah_yang_dibayarkan = jumlah_yang_dibayarkan;
    }
   
}
