package sejahterainformationsystem;

import db.DBHelper;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpPageController {

    @FXML
    private Button backBtn;
    
    @FXML
    private Button buatAkunBtn;

    @FXML
    private TextField tfAlamat;

    @FXML
    private TextField tfNamaLengkap;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfUsername;
    
    
     @FXML
    private Label lblPeringatan;

    @FXML
    void menambahkanAkun(ActionEvent event) throws IOException, Exception {
        String username = tfUsername.getText();
        String namaLengkap = tfNamaLengkap.getText();
        String alamat = tfAlamat.getText();
        String password = tfPassword.getText();

        if (isValidInput(username, namaLengkap, alamat, password)) {
            DBHelper dbHelper = new DBHelper();

            try {
                dbHelper.getConnection();
                
                String query = "INSERT INTO pelanggan(username, nama, alamat, password) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = dbHelper.getConnection().prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, namaLengkap);
                preparedStatement.setString(3, alamat);
                preparedStatement.setString(4, password);

                // Execute Query
                preparedStatement.executeUpdate();

                // Close resources
                preparedStatement.close();

                // Clear fields after successful registration
                tfUsername.clear();
                tfNamaLengkap.clear();
                tfAlamat.clear();
                tfPassword.clear();

                lblPeringatan.setText(""); // Menghapus pesan peringatan jika sebelumnya ada
                showSuccessNotification("Pemberitahuan", "Akun berhasil dibuat!");
                // Arahkan pengguna ke halaman login
                redirectToLoginPage(event);
                System.out.println("Akun berhasil dibuat!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Gagal membuat akun.");
            }
        } else {
            lblPeringatan.setText("Semua field wajib diisi!"); // Menampilkan pesan peringatan jika user tidak mengisi semua field
            System.out.println("ERROR GA SESUAI PARAMETER");
        }
    }

    // Validasi input
    private boolean isValidInput(String username, String namaLengkap, String alamat, String password) {
        return !username.isEmpty() && !namaLengkap.isEmpty() && !alamat.isEmpty() && !password.isEmpty();
    }
    
    @FXML
    void toLoginPage(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
       Parent root = loader.load();
       
       Stage stage = (Stage) backBtn.getScene().getWindow();
       stage.setScene(new Scene(root));  
    }
    
    private void showSuccessNotification(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Metode untuk mengarahkan pengguna ke halaman login
    private void redirectToLoginPage(ActionEvent event) throws Exception {
        Stage stage = (Stage) tfUsername.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml")); // Ganti "Login.fxml" dengan nama file FXML login page
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
