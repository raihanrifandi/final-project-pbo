package model;

public class Pegawai extends Pengguna {
    private String jabatan;

    public Pegawai(String username, String password) {
        super(username, password, null, null);
    }

    public Pegawai(String username, String password, String jabatan) {
        super(username, password, null, null);
        this.jabatan = jabatan;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }
}