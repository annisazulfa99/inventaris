package com.inventaris.controller;

import com.inventaris.Main;
import com.inventaris.util.SessionManager;
import com.inventaris.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


public class HomeController {

    @FXML private TextField searchField;
    @FXML private Button logoutBtn;
    @FXML private Button btnDashboard;
    @FXML private Button btnBarang;
    @FXML private Button btnPeminjaman;
    @FXML private Button btnLaporan;
    @FXML private Button btnLogout;
    @FXML private Button btnUser;
    @FXML private BorderPane rootPane;
    @FXML private TextField txtSearch;

    private final SessionManager sessionManager = SessionManager.getInstance();

    
   
    
    
    @FXML
public void initialize() {
    String role = sessionManager.getCurrentUser().getRole();
    
    // ✅ Panggil fungsi untuk mengatur menu berdasarkan role
    configureMenuByRole(role);
    
    System.out.println("Home screen loaded");
}

private void configureMenuByRole(String role) {
    switch (role) {
        case "admin":
            btnUser.setVisible(true);
            btnUser.setManaged(true);
            break;
            
        case "peminjam":
        case "instansi":
            btnUser.setVisible(false);
            btnUser.setManaged(false); // ✅ Penting: juga set managed ke false agar tidak memakan space
            break;
            
        default:
            btnUser.setVisible(false);
            btnUser.setManaged(false);
    }
}
    
    @FXML
private void handleDashboard() {
    Main.showDashboard();
}

   @FXML
private void handleSearch() {
    try {
        String keyword = txtSearch.getText().trim();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Barang.fxml"));
        Parent barangPage = loader.load();

        // Ambil controller DataBarang
        BarangController controller = loader.getController();

        // Kirim keyword ke method pencarian
        controller.searchBarang(keyword);

        // TAMPILKAN di bagian tengah Dashboard tanpa menghilangkan header
        rootPane.setCenter(barangPage);

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    @FXML
    private void handlePeminjaman() {
        Main.loadContent("Peminjaman.fxml");
    }

    @FXML
    private void handleLaporan() {
        Main.loadContent("Laporan.fxml");
    }

    @FXML
    private void handleBarang() {
        Main.loadContent("Barang.fxml");
    }
    
    @FXML
    private void handleUser() {
      
        Main.loadContent("User.fxml");
    }

    @FXML
    private void handleLogout() {
        if (AlertUtil.showLogoutConfirmation()) {
            sessionManager.logout();
            Main.showLoginScreen();
        }
    }
}
