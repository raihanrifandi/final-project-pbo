package sejahterainformationsystem;

import db.DBHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Pemesanan;

public class KelolaPesananPageController implements Initializable {

    @FXML
    private TableView<TableBean> daftarPesanantv;

    @FXML
    private TableColumn<TableBean, Integer> colJumlah;

    @FXML
    private TableColumn<TableBean, String> colNamaPemesan;

    @FXML
    private TableColumn<TableBean, String> colNamaProduk;

    @FXML
    private TableColumn<TableBean, String> colStatusPemesanan;

    @FXML
    private TableColumn<TableBean, Date> colTanggalPemesanan;

    @FXML
    private TableColumn<TableBean, Integer> colTotal;

    @FXML
    private TableColumn<TableBean, Integer> idPemesananCol;

    @FXML
    private TableColumn<TableBean, String> idMetodeCol;

    @FXML
    private TableColumn<TableBean, Integer> idPembayaranCol;

    @FXML
    private Button logOutbtn;

    ObservableList<TableBean> list = FXCollections.observableArrayList();

    @FXML
    private Button kelolaMenuBtn;

    @FXML
    private TextField idPemesanantf;

    @FXML
    private Button tandaiSelesaiBtn;

    @FXML
    void toKelolaMenuPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardPegawai.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) kelolaMenuBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    
    @FXML
    void setPesananSelesai(ActionEvent event) {
        try {
            // Mendapatkan id_pemesanan dari TextField yang diinputkan oleh Pegawai
            int idPemesanan = Integer.parseInt(idPemesanantf.getText());

            try (Connection connection = DBHelper.getConnection()) {
                String updateQuery = "UPDATE pemesanan SET status_pemesanan=1 WHERE id_pemesanan=?";

                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, idPemesanan);

                    updateStatement.executeUpdate();

                    // Refresh tabel setelah pembaruan status yang dilakukan oleh Pegawai
                    refreshTable();

                    System.out.println("Status pemesanan dengan ID " + idPemesanan + " berhasil diubah menjadi 'Selesai'.");
                }
            }
        } catch (NumberFormatException e) {
            // Handle jika input id_pemesanan tidak valid (bukan angka)
            System.out.println("Mohon masukkan ID Pemesanan yang valid.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal mengubah status pemesanan.");
        }
    }

    // Metode untuk refresh tabel setelah pembaruan status
    private void refreshTable() {
        list.clear();
        initialize(null, null);
    }

    @FXML
    void toLoginPage(ActionEvent event) throws IOException {
        SessionManager.getInstance().logout();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) kelolaMenuBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mengatur koneksi ke database dan menjalankan query
        try (Connection connection = DBHelper.getConnection()) {
            String query = "SELECT p.id_pemesanan AS 'ID Pemesanan', pb.id_pembayaran AS 'ID Pembayaran', "
                    + "mp.nama_metode AS 'Dibayar Melalui', pl.nama AS 'Nama Pemesan', p.tanggal_pemesanan AS 'Tanggal', "
                    + "pr.nama_produk AS 'Produk', dp.kuantitas_produk AS 'Jumlah', pr.harga * dp.kuantitas_produk AS 'Sub Total', "
                    + "p.status_pemesanan AS 'Status' "
                    + "FROM pemesanan p, pembayaran pb, pelanggan pl, produk pr, detail_pesanan dp, metode_pembayaran mp "
                    + "WHERE p.id_pemesanan = pb.id_pemesanan AND p.username = pl.username "
                    + "AND dp.id_pemesanan = p.id_pemesanan AND dp.id_produk = pr.id_produk AND pb.id_metode = mp.id_metode";

            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    // Mengambil data dari hasil query dan tambahkan ke ObservableList
                    int idPemesanan = resultSet.getInt("ID Pemesanan");
                    int idPembayaran = resultSet.getInt("ID Pembayaran");
                    String metodePembayaran = resultSet.getString("Dibayar Melalui");
                    String namaPemesan = resultSet.getString("Nama Pemesan");
                    Date tanggalPemesanan = resultSet.getDate("Tanggal");
                    String namaProduk = resultSet.getString("Produk");
                    int kuantitas = resultSet.getInt("Jumlah");
                    int subTotal = resultSet.getInt("Sub Total");
                    String statusPemesanan = resultSet.getBoolean("Status") ? "Selesai" : "Diproses";

                    // Menambahkan data ke ObservableList
                    list.add(new TableBean(idPemesanan, idPembayaran, metodePembayaran, namaPemesan, tanggalPemesanan, namaProduk, kuantitas, subTotal, statusPemesanan));
                }

                // Set ObservableList ke TableView
                daftarPesanantv.setItems(list);

                // Set PropertyValueFactory untuk setiap kolom
                idPemesananCol.setCellValueFactory(new PropertyValueFactory<>("id_pemesanan"));
                idPembayaranCol.setCellValueFactory(new PropertyValueFactory<>("id_pembayaran"));
                idMetodeCol.setCellValueFactory(new PropertyValueFactory<>("metode_pembayaran"));
                colNamaPemesan.setCellValueFactory(new PropertyValueFactory<>("nama_pemesan"));
                colTanggalPemesanan.setCellValueFactory(new PropertyValueFactory<>("tanggal_pemesanan"));
                colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
                colNamaProduk.setCellValueFactory(new PropertyValueFactory<>("nama_roduk"));
                colStatusPemesanan.setCellValueFactory(new PropertyValueFactory<>("status_pemesanan"));
                colTotal.setCellValueFactory(new PropertyValueFactory<>("totalDibayarkan"));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        daftarPesanantv.setOnMouseClicked(event -> {
        TableBean selectedPemesanan = daftarPesanantv.getSelectionModel().getSelectedItem();
        if (selectedPemesanan != null) {
            idPemesanantf.clear();
            idPemesanantf.setText(String.valueOf(selectedPemesanan.getId_pemesanan()));
        }
        });

        
        
    }
}
