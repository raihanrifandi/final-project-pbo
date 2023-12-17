package model;

import java.util.List;
import sejahterainformationsystem.Produk;

public class DetailPesanan {
    private int idProduk;
    private String namaProduk;
    private int harga;
    private int qty;
    private int subTotal;

    public DetailPesanan() {
    }

    public DetailPesanan(int idProduk, String namaProduk, int harga, int qty, int subTotal) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.qty = qty;
        this.subTotal = subTotal;
    }
    
    
    public DetailPesanan(String namaProduk, int harga, int qty, int subTotal) {
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.qty = qty;
        this.subTotal = subTotal;
    }

    public int getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(int idDetailPesanan) {
        this.idProduk = idDetailPesanan;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }
    
    
}
