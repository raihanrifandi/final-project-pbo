package sejahterainformationsystem;

import db.DBHelper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class DashboardPegawaiController implements Initializable {

    @FXML
    private Button addBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private TableColumn<Produk, String> col_Deskripsi;

    @FXML
    private TableColumn<Produk, Integer> col_Harga;

    @FXML
    private TableColumn<Produk, String> col_Nama;

    @FXML
    private TableColumn<Produk, String> col_idProduk;
    
    @FXML
    private TableView<Produk> tvDataProduk;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;
    
    @FXML
    private Button uploadFileBtn; // Komponen button agar pengguna dapat memilih path untuk upload gambar
    
    @FXML
    private ImageView tampilkanGambar;

    private byte[] imageData;

    @FXML
    private TextArea tfDeskripsi;

    @FXML
    private TextField tfHarga;

    @FXML
    private TextField tfIdProduk;

    @FXML
    private TextField tfNama;
    
    @FXML
    private Button logOutbtn;
    
    @FXML
    private Button daftarPesananBtn;
    
    private FileChooser fileChooser;
    
    
    @FXML
    void handleButtonAction(ActionEvent event) throws SQLException {
        if(event.getSource() == addBtn) addRecord();
        else if(event.getSource() == deleteBtn) deleteRecord();
        else if(event.getSource() == editBtn) editRecord();
    }
    
    @FXML
    void toKelolaPesananPage(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("KelolaPesananPage.fxml"));
       Parent root = loader.load();
       
       Stage stage = (Stage) daftarPesananBtn.getScene().getWindow();
       stage.setScene(new Scene(root));  
    }
    
    @FXML
    void uploadFiletoDb(ActionEvent event) {
        File selectedFile = fileChooser.showOpenDialog(new Stage());

    if (selectedFile != null) {
        try {
            // Membaca data biner gambar dari file yang dipilih
            imageData = Files.readAllBytes(selectedFile.toPath());

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception, jika selected file tidak sesuai
        }
        Image image = new Image(new ByteArrayInputStream(imageData));
        tampilkanGambar.setImage(image);
    }
    }
    
    private boolean isIdProdukExists(String idProduk) {
        Connection conn = DBHelper.getConnection();
        String query = "SELECT COUNT(*) FROM `produk` WHERE id_produk = '" + idProduk + "'";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            int count = rs.getInt(1);

            // If count is greater than 0, it means the ID Produk already exists
            return count > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    private void showAlert(String title, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Menjalankan syntax query
    private void go(String query){
        Connection conn = DBHelper.getConnection();
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    // Menambahkan spesifik records ke database Produk
    private void addRecord() throws SQLException{
        String idProduk = tfIdProduk.getText();
        String nama = tfNama.getText();
        String deskripsi = tfDeskripsi.getText();
        // variabel untuk getGambar dari file chooser

        int harga = 0; // Default value or handle the case when no valid integer is provided
        try {
            harga = Integer.parseInt(tfHarga.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        
        if (isIdProdukExists(idProduk)) {
        // Menunjukkan pesan pop up jika pada tabel terdapat ID produk yang sama dengan inputan
            showAlert("Gagal Menambahkan Produk!", "ID Produk harus unik.", AlertType.ERROR);
            return;
        }

        String query = "INSERT INTO `produk` VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, idProduk);
            pstmt.setString(2, nama);
            pstmt.setString(3, deskripsi);
            pstmt.setInt(4, harga);
            pstmt.setBytes(5, imageData); // Mengisi parameter data biner gambar

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        go(query);
        showDaftarProduk();

        tfIdProduk.clear();
        tfNama.clear();
        tfDeskripsi.clear();
        tfHarga.clear();
        
//        String query = "INSERT INTO `produk`(`"+ tfIdProduk.getText() + "','" + tfNama.getText() + "','" + tfDeskripsi.getText() + "','" + tfHarga.getText() + "')";
//        String query2 = "INSERT INTO `produk`(`" + tfIdProduk.getText() + "','" + tfNama.getText() + "','" + tfDeskripsi.getText() + "','" + tfHarga.getText() + "')";
//        
//        go(query);
//        showDaftarProduk();
//        
//        tfIdProduk.clear();
//        tfNama.clear();
//        tfDeskripsi.clear();
//        tfHarga.clear();
        
    }
     
    // Menghapus spesifik records dari tabel produk
    private void deleteRecord(){
        String query = "DELETE FROM `produk` WHERE id_produk ='"+ tfIdProduk.getText() +"'";
        go(query);
        showDaftarProduk();
        
        tfIdProduk.clear();
             
    }
    // Tambahan untuk versi selanjutnya, karena gambar pada produk merupakan opsional, maka quernya dibagi menjadi if(gambar != null) else gambar == null
    // Mengedit spesifik records dari tabel produk
    private void editRecord(){
        String query = "UPDATE `produk` SET `id_produk`='" + tfIdProduk.getText() + "',`nama_produk`='" + tfNama.getText() + "',`deskripsi`='" + tfDeskripsi.getText() + "',`harga`='" + tfHarga.getText() + "' WHERE `id_produk`='" + tfIdProduk.getText() +"'"; 
        go(query);
        showDaftarProduk();
        
        tfIdProduk.clear();
        tfNama.clear();
        tfDeskripsi.clear();
        tfHarga.clear();
 
    }
    
    public ObservableList<Produk> getDataProduk() {
        ObservableList<Produk> produk = FXCollections.observableArrayList();
        Connection conn = DBHelper.getConnection();
        String query = "SELECT * FROM `produk`";
        Statement st;
        ResultSet rs;
        
        try{
            st =  conn.createStatement();
            rs = st.executeQuery(query);
            Produk temp;
            while(rs.next()){
                temp =  new Produk(rs.getString("id_produk"), rs.getString("nama_produk"), rs.getString("deskripsi"), rs.getInt("harga"));
                produk.add(temp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return produk;
    }
    
    public void showDaftarProduk(){
        ObservableList<Produk> list = getDataProduk();
        col_idProduk.setCellValueFactory(new PropertyValueFactory<>("id_produk"));
        col_Nama.setCellValueFactory(new PropertyValueFactory<>("nama_produk"));
        col_Deskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        col_Harga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        tvDataProduk.setItems(list);
    }
    
    @FXML
    void toLoginPage(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
       Parent root = loader.load();
       
       Stage stage = (Stage) logOutbtn.getScene().getWindow();
       stage.setScene(new Scene(root));  
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        
        showDaftarProduk();
        
        clearBtn.setOnAction(event -> {
                tfIdProduk.clear();
                tfNama.clear();
                tfDeskripsi.clear();
                tfHarga.clear();
                tampilkanGambar.setImage(null);
            });
        // Jika user memilih spesifik row, sistem akan otomatis mengisi textfield agar memudahkan pengguna dalam melakukan delete atau update pada records.
        
        tvDataProduk.setOnMouseClicked(event -> {
        Produk selectedProduk = tvDataProduk.getSelectionModel().getSelectedItem();
        if (selectedProduk != null) {
            tfIdProduk.clear();
            tfIdProduk.setText(selectedProduk.getId_produk());
            tfNama.clear();
            tfNama.setText(selectedProduk.getNama_produk());
            tfDeskripsi.clear();
            tfDeskripsi.setText(selectedProduk.getDeskripsi());
            tfHarga.clear();
            tfHarga.setText(String.valueOf(selectedProduk.getHarga()));
            menampilkanGambar(selectedProduk);
            
        }
    });
        
    }
    
    private void menampilkanGambar(Produk produk) {
        if (produk != null) {
            try {
                // Mengambil data gambar dari database
                byte[] imageData = getImageDataFromDatabase(produk.getId_produk());

                if (imageData != null) {
                    // Membuat Image dari data gambar
                    Image image = new Image(new ByteArrayInputStream(imageData));

                    // Menampilkan gambar pada ImageView
                    tampilkanGambar.setImage(image);
                } else {
                    // Jika tidak ada data gambar, set ImageView menjadi null
                    tampilkanGambar.setImage(null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exception, misalnya menampilkan pesan kesalahan kepada pengguna
            }
        }
    }

    private byte[] getImageDataFromDatabase(String idProduk) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String query = "SELECT gambar FROM `produk` WHERE id_produk = '" + idProduk + "'";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            return rs.getBytes("gambar");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

}