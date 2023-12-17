package sejahterainformationsystem;

import db.DBHelper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DetailPesanan;

public class DashboardPelangganController implements Initializable {

    @FXML
    private TableView<DetailPesanan> detailPesananTv;
    
    @FXML
    private TableColumn<DetailPesanan, Integer> colSubTotal;

    @FXML
    private TableColumn<DetailPesanan, String> colNamaProduk;

    @FXML
    private TableColumn<DetailPesanan, String> colQty;
    
    private ObservableList<DetailPesanan> detailPesananList;
    
    @FXML
    private GridPane produkContainer;
    
    private List<Produk> daftarProduk;
    
    private ObservableList<VBox> displayedProdukContainers;
    
    @FXML
    private TextField tfSearch;
    
    @FXML
    private Button bayarPesananBtn;
    
    @FXML
    private Button pesananSayaBtn;
    
    @FXML
    private TextField tambahKuantitastf;

    @FXML
    private TextField tambahProduktf;

    @FXML
    private Button tambahkKePesananBtn;
    
    @FXML
    private Button hapusPesananBtn;
    
    @FXML
    private Label totalBayarLabel;
    
    @FXML
    private RadioButton rbMetodeNonTunai;

    @FXML
    private RadioButton rbMetodeTunai;
    
    @FXML
    private Button logoutBtn;

    @FXML
    void cariProduk() {
        performSearch();
    }
    
    @FXML
    void toLoginPage(ActionEvent event) throws IOException {
        SessionManager.getInstance().logout();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    
    @FXML
    void toPesananSayaPage(ActionEvent event) {

    }
    
    @FXML
    void BayarPesanan(ActionEvent event) {
        
        if (showConfirmationDialog()) {
            String metodePembayaran = getSelectedMetodePembayaran();

            // menyimpan informasi pembayaran dan pemesanan
            int idPemesanan = simpanPemesanan();
            int idPembayaran = simpanPembayaran(idPemesanan, metodePembayaran);

            // menghubungkan tabel pembayaran, tabel pemesanan, dan tabel metode pembayaran
            hubungkanPembayaranPemesanan(idPembayaran, idPemesanan, getIdMetodePembayaran(metodePembayaran));
            updateIdPemesananDetailPesanan(idPemesanan);
            
            detailPesananList.clear();
            detailPesananTv.setItems(detailPesananList);

            // Update total keseluruhan setelah reset
            updateTotalKeseluruhan();
        }
    }
    
    @FXML
    void hapusDetailPesanan(ActionEvent event) {
        hapusDetailPesananDariTabel();
    }
    
    private void hapusDetailPesanann(int idProduk) {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "DELETE FROM detail_pesanan WHERE id_produk = ? AND id_pemesanan IS NULL";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idProduk);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Detail pesanan berhasil dihapus");
                } else {
                    System.out.println("Gagal menghapus detail pesanan");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void hapusDetailPesananDariTabel() {
        DetailPesanan selectedDetailPesanan = detailPesananTv.getSelectionModel().getSelectedItem();
        if (selectedDetailPesanan != null) {
            hapusDetailPesanann(selectedDetailPesanan.getIdProduk());
            detailPesananList.remove(selectedDetailPesanan);
            // Update total keseluruhan setelah menghapus
            updateTotalKeseluruhan();
        }
    }

    private boolean showConfirmationDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Pembayaran");
        alert.setHeaderText("Anda yakin ingin melanjutkan pembayaran?");
        alert.setContentText("Pastikan pesanan Anda sudah benar.");

        // Menambahkan tombol OK dan Cancel pada pop-up konfirmasi
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Menunggu hasil dari pop-up konfirmasi
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
    
    private String getSelectedMetodePembayaran() {
        if (rbMetodeTunai.isSelected()) {
            return "Tunai";
        } else if (rbMetodeNonTunai.isSelected()) {
            return "Non-Tunai";
        }
        // Jika tidak ada yang dipilih, maka sistem akan otomatis mengisi Tunai
        return "Tunai";
    }
    
    private int simpanPemesanan() {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "INSERT INTO pemesanan (tanggal_pemesanan, status_pemesanan, username) VALUES (CURRENT_DATE, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setBoolean(1, false); // Status pesanan, misalnya sedang di proses
                statement.setString(2, SessionManager.getInstance().getLoggedInUsername());

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
    
    private int simpanPembayaran(int idPemesanan, String metodePembayaran) {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "INSERT INTO pembayaran (id_pemesanan, tanggal_pembayaran, jumlah_yang_dibayarkan, id_metode) VALUES (?, CURRENT_DATE, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, idPemesanan);
                statement.setInt(2, Integer.parseInt(totalBayarLabel.getText())); // Ambil jumlah total dari label
                statement.setInt(3, getIdMetodePembayaran(metodePembayaran));

                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Gagal menyimpan pembayaran
    }
    
    // Jika user memilih Tunai, maka akan return 1
    // Jika user memilih Non-Tunai, maka akan return 2
    // Hal tersebut dikarenakan pada tabel metode_pembayaran 1 merupakan tunai dan 2 merupakan non tunai
    private int getIdMetodePembayaran(String metodePembayaran) {
        if (metodePembayaran.equals("Tunai")) {
            return 1;
        } else if (metodePembayaran.equals("Non-Tunai")) {
            return 2;
        }
        
        return -1;
    }
    
    private void hubungkanPembayaranPemesanan(int idPembayaran, int idPemesanan, int idMetode) {
        try (Connection connection = DBHelper.getConnection()) {
            // Berfungsi untuk menambahkan id pemesanan pada tabel pembayaran
            String query = "UPDATE pembayaran SET id_pemesanan = ? WHERE id_pembayaran = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idPemesanan);
                statement.setInt(2, idPembayaran);
                statement.executeUpdate();
            }

            // Berfungsi untuk menambahkan id metode pada tabel pembayaran
            query = "UPDATE pembayaran SET id_metode = ? WHERE id_pembayaran = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idMetode);
                statement.setInt(2, idPembayaran);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void updateIdPemesananDetailPesanan(int idPemesananBaru) {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "UPDATE detail_pesanan SET id_pemesanan = ? WHERE id_pemesanan IS NULL";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idPemesananBaru);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("ID pemesanan di detail_pesanan berhasil diperbarui");
                } else {
                    System.out.println("Gagal memperbarui ID pemesanan di detail_pesanan");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void performSearch() {
        String searchText = tfSearch.getText().toLowerCase();

        List<Produk> hasilPencarian = daftarProduk.stream()
                .filter(produk -> produk.getNama_produk().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        displaySearchResults(hasilPencarian);
    }
    
    private void displaySearchResults(List<Produk> hasilPencarian) {
        produkContainer.getChildren().clear();

        int kolom = 0;
        int baris = 1;

        try {
            for (Produk produk : hasilPencarian) {
                FXMLLoader container = new FXMLLoader();
                container.setLocation(getClass().getResource("produkDisplay.fxml"));
                VBox produkBox = container.load();
                ProdukController produkcontroller = container.getController();
                produkcontroller.setData(produk);

                if (kolom == 4) {
                    kolom = 0;
                    ++baris;
                }

                produkContainer.add(produkBox, kolom++, baris);
                GridPane.setMargin(produkBox, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        daftarProduk = new ArrayList<>(daftarProduk());
        displayedProdukContainers = FXCollections.observableArrayList();

        tfSearch.setOnKeyPressed(this::handleSearchKeyPress);

        displayAllProducts();
        
        detailPesananList = FXCollections.observableArrayList();

        colNamaProduk.setCellValueFactory(new PropertyValueFactory<>("namaProduk"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        detailPesananTv.setItems(detailPesananList);
        tambahkKePesananBtn.setOnAction(event -> tambahDetailPesanan());
        
        ToggleGroup metodeToggleGroup = new ToggleGroup();
        rbMetodeTunai.setToggleGroup(metodeToggleGroup);
        rbMetodeNonTunai.setToggleGroup(metodeToggleGroup);
        
        // Jika user memilih spesifik row, sistem akan otomatis mengisi textfield agar memudahkan pengguna dalam melakukan delete atau update pada records.
        detailPesananTv.setOnMouseClicked(event -> {
        DetailPesanan selectedDetailPesanan = detailPesananTv.getSelectionModel().getSelectedItem();
        if (selectedDetailPesanan != null) {
            tambahProduktf.clear();
            tambahProduktf.setText(selectedDetailPesanan.getNamaProduk());
            tambahKuantitastf.clear();
            tambahKuantitastf.setText(String.valueOf(selectedDetailPesanan.getQty()));
    }
});

    }

    private void handleSearchKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            performSearch();
        }
    }

    private void displayAllProducts() {
        produkContainer.getChildren().clear();

        int kolom = 0;
        int baris = 1;

        try {
            for (Produk produk : daftarProduk) {
                FXMLLoader container = new FXMLLoader();
                container.setLocation(getClass().getResource("produkDisplay.fxml"));
                VBox produkBox = container.load();
                ProdukController produkcontroller = container.getController();
                produkcontroller.setData(produk);

                if (kolom == 4) {
                    kolom = 0;
                    ++baris;
                }

                produkContainer.add(produkBox, kolom++, baris);
                GridPane.setMargin(produkBox, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test gridPane dan ScrollPane
    
    private List<Produk> daftarProduk() {
        List<Produk> ls = new ArrayList<>();

        try (Connection connection = DBHelper.getConnection()) {
            String query = "SELECT id_produk, nama_produk, deskripsi, harga, gambar FROM produk";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String id_produk = resultSet.getString("id_produk");
                    String nama_produk = resultSet.getString("nama_produk");
                    String deskripsi = resultSet.getString("deskripsi");
                    int harga = resultSet.getInt("harga");

                    // Retrieve BLOB data only if the column is not null
                    Blob blob = resultSet.getBlob("gambar");
                    byte[] gambarBytes = null;

                    if (blob != null) {
                        try (InputStream inputStream = blob.getBinaryStream()) {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            gambarBytes = outputStream.toByteArray();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    Produk produk = new Produk(id_produk, nama_produk, deskripsi, harga, gambarBytes);
                    ls.add(produk);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ls;
    }
    
    private Produk getProdukById(String idProduk) {
        for (Produk produk : daftarProduk) {
            if (produk.getId_produk().equals(idProduk)) {
                return produk;
            }
        }
        return null; // Produk tidak ditemukan
    }
    
    private void tambahDetailPesanan() {
        try (Connection connection = DBHelper.getConnection()) {
            String kuantitas = tambahKuantitastf.getText();
            String idProduk = tambahProduktf.getText();


            String query = "INSERT INTO detail_pesanan (kuantitas_produk, id_pemesanan, id_produk) VALUES (?, NULL, ?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, kuantitas);
                statement.setString(2, idProduk);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Detail pesanan berhasil ditambahkan");
                    DetailPesanan detailPesanan = new DetailPesanan();
                detailPesanan.setQty(Integer.parseInt(kuantitas));

                // Mendapatkan informasi produk berdasarkan id_produk
                Produk produk = getProdukById(idProduk);

                if (produk != null) {
                    detailPesanan.setNamaProduk(produk.getNama_produk());
                    detailPesanan.setHarga(produk.getHarga()); // Harga diambil dari informasi produk
                } else {
                    // Produk tidak ditemukan
                    System.out.println("Produk tidak ditemukan");
                    return;
                }

                // Menghitung subtotal
                int subtotal = detailPesanan.getHarga() * detailPesanan.getQty();
                detailPesanan.setSubTotal(subtotal);


                detailPesananList.add(detailPesanan);

                // Update total keseluruhan
                updateTotalKeseluruhan();
                    } else {
                        System.out.println("Gagal menambahkan detail pesanan");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    
    private void updateTotalKeseluruhan() {
        int totalKeseluruhan = 0;

        for (DetailPesanan detailPesanan : detailPesananList) {
            totalKeseluruhan += detailPesanan.getSubTotal();
        }

        totalBayarLabel.setText(String.valueOf(totalKeseluruhan));
    }
    
    public String getTotalBayarLabel() {
        return totalBayarLabel.getText();
    }

}
