package model;


public class Pelanggan extends Pengguna {
    private String metode_pembayaran;

    public Pelanggan(String username, String password, String nama, String alamat, String metode_pembayaran) {
        super(username, password, nama, alamat); 
        this.metode_pembayaran = metode_pembayaran;
    }

    public String getMetode_pembayaran() {
        return metode_pembayaran;
    }

    public void setMetode_pembayaran(String metode_pembayaran) {
        this.metode_pembayaran = metode_pembayaran;
    }
}
