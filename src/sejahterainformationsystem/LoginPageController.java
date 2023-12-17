package sejahterainformationsystem;

import db.DBHelper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginPageController {
    
    @FXML
    private Button btnMasuk;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfUsername;
    
    @FXML
    private Label invalidUserPassBtn;
    
    @FXML
    private Button btnDaftar;
    
    // Menentukan apakah pengguna adalah pegawai atau pelanggan
    @FXML
    void toDashboard(ActionEvent event) throws IOException {
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        
        SessionManager sessionManager = SessionManager.getInstance();

        if (authenticatePegawai(username, password)) {
            sessionManager.setLoggedInUsername(username);
            loadDashboard("DashboardPegawai.fxml");
        } else if (authenticatePelanggan(username, password)) {
            sessionManager.setLoggedInUsername(username);
            loadDashboard("DashboardPelanggan.fxml");
        } else {
            invalidUserPassBtn.setText("Username atau Password yang anda masukkan salah!");
        }
    }
    
    // Mengarahkan pengguna ke halaman Sign Up / Pendaftaran
    @FXML
    void toSignUpPage(ActionEvent event) throws IOException{
       FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUpPage.fxml"));
       Parent root = loader.load();
       
       Stage stage = (Stage) btnDaftar.getScene().getWindow();
       stage.setScene(new Scene(root));  

    }
    
    // Memverifikasi apakah pengguna yang masuk adalah pegawai
    private boolean authenticatePegawai(String username, String password) {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "SELECT * FROM pegawai WHERE username=? AND password=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Memverifikasi apakah pengguna yang masuk adalah pelanggan
    private boolean authenticatePelanggan(String username, String password) {
        try (Connection connection = DBHelper.getConnection()) {
            String query = "SELECT * FROM pelanggan WHERE username=? AND password=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Memuat halaman dashboard masing-masing pengguna
     private void loadDashboard(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Stage stage = (Stage) btnMasuk.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
