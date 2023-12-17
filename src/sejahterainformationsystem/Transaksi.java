package sejahterainformationsystem;

import db.DBHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import model.Pembayaran;
import model.Pemesanan;

public class Transaksi {

    // Metode untuk menangani proses pembayaran dan pemesanan
    public static void bayarPesanan(String metodePembayaran) {
        // Mengambil informasi yang diperlukan
        SessionManager sessionManager = SessionManager.getInstance();
        String loggedInUsername = sessionManager.getLoggedInUsername();

        // Menyimpan informasi pembayaran
        Pembayaran pembayaran = new Pembayaran();
        pembayaran.setTanggal_pembayaran(new Date(System.currentTimeMillis()));
        pembayaran.setJumlah_yang_dibayarkan(getTotalPembayaran());

        int idPembayaran = simpanPembayaran(pembayaran);

        // Menyimpan informasi pemesanan
        Pemesanan pemesanan = new Pemesanan();
        pemesanan.setTanggal_pemesanan(new Date(System.currentTimeMillis()));
        pemesanan.setStatus_pesanan(false);
        pemesanan.getJumlah();

        int idPemesanan = simpanPemesanan(pemesanan);

        // Menyimpan informasi metode pembayaran
        int idMetode = (metodePembayaran.equals("Tunai")) ? 1 : 2;

        // Hubungkan pembayaran, pemesanan, dan metode pembayaran
        hubungkanPembayaranPemesanan(idPembayaran, idPemesanan, idMetode);    
    }

    private static int simpanPembayaran(Pembayaran pembayaran) {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "INSERT INTO pembayaran (tanggal_pembayaran, jumlah_yang_dibayarkan) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setDate(1, new java.sql.Date(pembayaran.getTanggal_pembayaran().getTime()));
                statement.setInt(2, pembayaran.getJumlah_yang_dibayarkan());

                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static int simpanPemesanan(Pemesanan pemesanan) {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "INSERT INTO pemesanan (tanggal_pemesanan, status_pesanan, jumlah) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setDate(1, new java.sql.Date(pemesanan.getTanggal_pemesanan().getTime()));
                statement.setBoolean(2, pemesanan.getStatus_pesanan());
                statement.setInt(3, pemesanan.getJumlah());

                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Gagal menyimpan pemesanan
    }

    private static void hubungkanPembayaranPemesanan(int idPembayaran, int idPemesanan, int idMetode) {
        try (Connection connection = DBHelper.getConnection()) {
            // Menghubungkan tabel pembayaran dengan tabel pemesanan
            String query = "UPDATE pembayaran SET id_pemesanan = ? WHERE id_pembayaran = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idPemesanan);
                statement.setInt(2, idPembayaran);
                statement.executeUpdate();
            }

            // Menghubungkan tabel metode pembayaran dengan tabel pemesanan
            query = "UPDATE pemesanan SET id_metode = ? WHERE id_pemesanan = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idMetode);
                statement.setInt(2, idPemesanan);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getTotalPembayaran() {
        return 0;
    }
}

