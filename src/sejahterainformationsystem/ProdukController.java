package sejahterainformationsystem;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

public class ProdukController {

    // Menambahkan komponen button agar user dapat menambahkan produk ke dalam keranjang (dalam konteks ini tabel detail_pesanan)
    
    @FXML
    private ImageView imageGambarProduk;

    @FXML
    private Label labelDeskripsi;

    @FXML
    private Label labelHarga;
    
    @FXML
    private Label labelIdProduk;

    @FXML
    private Label labelNamaProduk;
    
    public void setData(Produk produk){
        if (produk.getGambarData() != null) {
            try {
                // Convert the byte array to javafx.scene.image.Image
                Image image = convertToImage(produk.getGambarData());

                // Set the image to ImageView
                imageGambarProduk.setImage(image);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception properly in your application
            }
        }
        
        
        labelNamaProduk.setText(produk.getNama_produk());
        labelDeskripsi.setText(produk.getDeskripsi());
        labelHarga.setText(String.valueOf(produk.getHarga()));
        labelIdProduk.setText(produk.getId_produk());
    }
    
    private Image convertToImage(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(bis);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
    
    
}
