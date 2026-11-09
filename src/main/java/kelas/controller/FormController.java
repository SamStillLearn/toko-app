package kelas.controller;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import kelas.Navigator;
import kelas.data.DataProduk;
import kelas.model.Produk;

public class FormController {

    @FXML
    private TextField namaField, hargaField;
    @FXML
    private ComboBox<String> kategoriBox;
    @FXML
    private Label pesanLabel;
    @FXML
    private ImageView gambarView;
    private Navigator navigator;
    private DataProduk data;
    private String gambarUri;

    @FXML
    private void initialize() {
        kategoriBox.getItems().addAll("Makanan", "Minuman");
        kategoriBox.setValue("Makanan");
    }

    public void setKonteks(Navigator navigator, DataProduk data) {
        this.navigator = navigator;
        this.data = data;
    }

    private void tampilPesan(String teks, boolean error) {
        pesanLabel.getStyleClass().removeAll("error", "muted");
        pesanLabel.getStyleClass().add(error ? "error" : "muted");
        pesanLabel.setText(teks);
    }

    @FXML
    private void pilihGambar() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih gambar produk");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Gambar PNG/JPEG", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(namaField.getScene().getWindow());
        if (file == null) {
            return; // Batal: pertahankan pilihan sebelumnya.
        }
        if (file.length() > 5L * 1024 * 1024) {
            tampilPesan("Maksimum gambar 5 MB.", true);
            return;
        }
        Image image = new Image(file.toURI().toString(), 640, 480, true, true);
        if (image.isError()) {
            tampilPesan("Gambar tidak dapat dibaca.", true);
            return;
        }
        gambarUri = file.toURI().toString();
        gambarView.setImage(image);
        tampilPesan("Gambar dipilih (file lokal, tidak diunggah ke server).", false);
    }

    @FXML
    private void simpan() {
        try {
            long harga = Long.parseLong(hargaField.getText().trim());
            Produk p = data.tambah(namaField.getText(), kategoriBox.getValue(), harga, gambarUri);

            namaField.clear();
            hargaField.clear();
            kategoriBox.setValue("Makanan");
            gambarUri = null;
            gambarView.setImage(null);
            namaField.requestFocus();
            tampilPesan("Produk " + p.getKode() + " (" + p.getNama() + ") berhasil disimpan.", false);
        } catch (NumberFormatException e) {
            tampilPesan("Harga harus berupa bilangan bulat tanpa titik atau huruf, contoh: 15000.", true);
        } catch (IllegalArgumentException e) {
            tampilPesan(e.getMessage(), true);
        }
    }

    @FXML
    private void bukaDaftar() {
        try {
            navigator.bukaDaftar();
        } catch (IOException e) {
            tampilPesan("Gagal membuka daftar.", true);
            e.printStackTrace();
        }
    }
}
