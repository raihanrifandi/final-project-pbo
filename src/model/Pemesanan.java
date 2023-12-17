package model;

import java.sql.Date;
import sejahterainformationsystem.DashboardPelangganController;

public class Pemesanan {
    private int id_pesanan;
    private Date tanggal_pemesanan;
    private Boolean status_pesanan;
    private int jumlah;
    DashboardPelangganController dashboardController = new DashboardPelangganController();

    public Pemesanan() {
    }

    public Pemesanan(int id_pesanan, Date tanggal_pemesanan, Boolean status_pesanan, int jumlah) {
        this.id_pesanan = id_pesanan;
        this.tanggal_pemesanan = tanggal_pemesanan;
        this.status_pesanan = status_pesanan;
        this.jumlah = jumlah;
    }
    
    // id pesanan otomatis terbuat setiap kali pengguna menekan button Bayar
    public int getId_pesanan() {
        return id_pesanan;
    }

    public void setId_pesanan(int id_pesanan) {
        this.id_pesanan = id_pesanan;
    }
    
    // tanggal pemesanan otomatis terisi saat sesudah pengguna berhasil membayar pemesanannya
    public Date getTanggal_pemesanan() {
        return tanggal_pemesanan;
    }

    public void setTanggal_pemesanan(Date tanggal_pemesanan) {
        this.tanggal_pemesanan = tanggal_pemesanan;
    }
    
    // Pelanggan dapat melihat status dari pesanannya sudah selesai atau masih di proses
    // 0 = diproses, 1 = selesai;
    public Boolean getStatus_pesanan() {
        return status_pesanan;
    }
    
    // Hanya pegawai yang dapat mengubah status pesanan
    // Tampilan pengubahannya berupa dropdown menu yang terdiri dari diproses/selesai (0 atau 1)
    public void setStatus_pesanan(Boolean status_pesanan) {
        this.status_pesanan = status_pesanan;
    }
    
    // retrieve data dari tabel detail_pesanan untuk setiap produk yang ditambahkan kuantitasnya
    public int getJumlah() {
        return jumlah;
    }
    
    // membuat trigger pada mySQL jika pengguna ingin menambahkan produk dengan Primary key yang sama, maka menu akan 
    // UPDATE detail_pesanan SET kuantitas = 2 (atau bertambah sebanyak n jika user ingin menambahkan sebanyak n
    // batasan untuk menambahkan menu hanya sampai 99, tidak dapat lebih
    public void setJumlah() {
        String totalBayarStr = dashboardController.getTotalBayarLabel();

        try {
            // Konversi nilai String ke int (atau tipe numerik yang sesuai)
            this.jumlah = Integer.parseInt(totalBayarStr);
        } catch (NumberFormatException e) {
            // Handle kesalahan konversi jika diperlukan
            e.printStackTrace();
        }
    }
    
}
