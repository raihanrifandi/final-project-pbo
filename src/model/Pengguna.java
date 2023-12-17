package model;

abstract class Pengguna {
    private String username;
    private String password;
    private String nama;
    private String alamat;

    public Pengguna(String username, String password, String nama, String alamat) {
        this.username = username;
        this.password = password;
        this.nama = nama;
        this.alamat = alamat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }   
    
}
