package sejahterainformationsystem;

public class Produk {
    private String id_produk;
    private String nama_produk;
    private String deskripsi;
    private int harga;
    private String gambar;
    private byte[] gambarData;
    
    Produk() {
    }

    public Produk(String id_produk, String nama_produk, String deskripsi, int harga) {
        this.id_produk = id_produk;
        this.nama_produk = nama_produk;
        this.deskripsi = deskripsi;
        this.harga = harga;
    }

    public Produk(String id_produk, String nama_produk, String deskripsi, int harga, String gambar) {
        this.id_produk = id_produk;
        this.nama_produk = nama_produk;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.gambar = gambar;
    }
    
    public Produk(String id_produk, String nama_produk, String deskripsi, int harga, byte[] gambarData) {
        this.id_produk = id_produk;
        this.nama_produk = nama_produk;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.gambarData = gambarData;
    }
    
    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
    
     public byte[] getGambarData() {
        return gambarData;
    }

    public void setGambarData(byte[] gambarData) {
        this.gambarData = gambarData;
    }
    
}
