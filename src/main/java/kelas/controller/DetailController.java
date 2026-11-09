package kelas.controller;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kelas.Navigator;
import kelas.model.Produk;

public class DetailController {

    @FXML
    private Label kodeLabel, namaLabel, kategoriLabel, hargaLabel, pesanLabel;
    @FXML
    private ImageView gambarView;
    private Navigator navigator;

    public void setData(Navigator navigator, Produk p) {
        this.navigator = navigator;
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        formatRupiah.setMaximumFractionDigits(0);

        kodeLabel.setText(p.getKode());
        namaLabel.setText(p.getNama());
        kategoriLabel.setText(p.getKategori());
        hargaLabel.setText(formatRupiah.format(p.getHarga()));

        if (p.getGambarUri() != null) {
            Image image = new Image(p.getGambarUri(), 640, 480, true, true);
            if (image.isError()) {
                pesanLabel.getStyleClass().remove("muted");
                pesanLabel.getStyleClass().add("error");
                pesanLabel.setText("Gambar tidak ditemukan atau tidak dapat dibaca.");
            } else {
                gambarView.setImage(image);
            }
        } else {
            pesanLabel.setText("Produk ini tidak memiliki gambar.");
        }
    }

    @FXML
    private void tambahLagi() {
        try {
            navigator.bukaForm();
        } catch (IOException e) {
            pesanLabel.setText("Gagal membuka form.");
            e.printStackTrace();
        }
    }

    @FXML
    private void bukaDaftar() {
        try {
            navigator.bukaDaftar();
        } catch (IOException e) {
            pesanLabel.setText("Gagal membuka daftar.");
            e.printStackTrace();
        }
    }
}
