
package sejahterainformationsystem;

import java.sql.Date;
import javafx.scene.control.ComboBox;


public class TableBean {
    private int id_pemesanan;
    private int id_pembayaran;
    private String metode_pembayaran;
    private Date tanggal_pemesanan;
    private String nama_pemesan;
    private String nama_produk;
    private int jumlah;
    private int totalDibayarkan;
    private String status_pemesanan;

    public TableBean(int id_pemesanan, int id_pembayaran, String metode_pembayaran, String nama_pemesan, Date tanggal_pemesanan,  String nama_produk, int jumlah, int totalDibayarkan, String status_pemesanan) {
        this.id_pemesanan = id_pemesanan;
        this.id_pembayaran = id_pembayaran;
        this.metode_pembayaran = metode_pembayaran;
        this.tanggal_pemesanan = tanggal_pemesanan;
        this.nama_pemesan = nama_pemesan;
        this.nama_produk = nama_produk;
        this.jumlah = jumlah;
        this.totalDibayarkan = totalDibayarkan;
        this.status_pemesanan = status_pemesanan;
    }
   

    public int getId_pemesanan() {
        return id_pemesanan;
    }

    public void setId_pemesanan(int id_pemesanan) {
        this.id_pemesanan = id_pemesanan;
    }

    public int getId_pembayaran() {
        return id_pembayaran;
    }

    public void setId_pembayaran(int id_pembayaran) {
        this.id_pembayaran = id_pembayaran;
    }

    public String getMetode_pembayaran() {
        return metode_pembayaran;
    }

    public void setMetode_pembayaran(String metode_pembayaran) {
        this.metode_pembayaran = metode_pembayaran;
    }

    public Date getTanggal_pemesanan() {
        return tanggal_pemesanan;
    }

    public void setTanggal_pemesanan(Date tanggal_pemesanan) {
        this.tanggal_pemesanan = tanggal_pemesanan;
    }

    public String getNama_pemesan() {
        return nama_pemesan;
    }

    public void setNama_pemesan(String nama_pemesan) {
        this.nama_pemesan = nama_pemesan;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getTotalDibayarkan() {
        return totalDibayarkan;
    }

    public void setTotalDibayarkan(int totalDibayarkan) {
        this.totalDibayarkan = totalDibayarkan;
    }

    public String getStatus_pemesanan() {
        return status_pemesanan;
    }

    public void setStatus_pemesanan(String status_pemesanan) {
        this.status_pemesanan = status_pemesanan;
    }
    
}
