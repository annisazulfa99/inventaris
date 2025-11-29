// ================================================================
// File: src/main/java/com/inventaris/controller/DashboardController.java
// ================================================================
package com.inventaris.controller;

import com.inventaris.Main;
import com.inventaris.dao.BarangDAO;
import com.inventaris.dao.BorrowDAO;
import com.inventaris.model.Barang;
import com.inventaris.model.Borrow;
import com.inventaris.model.User;
import com.inventaris.util.AlertUtil;
import com.inventaris.util.LogActivityUtil;
import com.inventaris.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.stream.Collectors;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * DashboardController - Main dashboard controller
 */
public class DashboardController implements Initializable {
    
    
    @FXML private Label welcomeLabel;
    @FXML private Label roleLabel;
    @FXML private Label totalBarangLabel;
    @FXML private Label tersediaLabel;
    @FXML private Label dipinjamLabel;
    @FXML private Label overdueLabel;
    
    @FXML private BorderPane rootPane;

    
    @FXML private VBox sidebarMenu;
    @FXML private Button btnDashboard;
    @FXML private Button btnBarang;
    @FXML private Button btnPeminjaman;
    @FXML private Button btnLaporan;
    @FXML private Button btnLogout;
    @FXML private Button btnUser;
    @FXML private TextField txtSearch;
    
    @FXML private TableView<Borrow> recentBorrowTable;
    @FXML private TableColumn<Borrow, String> colPeminjam;
    @FXML private TableColumn<Borrow, String> colBarang;
    @FXML private TableColumn<Borrow, String> colTglPinjam;
    @FXML private TableColumn<Borrow, String> colDeadline;
    @FXML private TableColumn<Borrow, String> colStatus;
    
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final BarangDAO barangDAO = new BarangDAO();
    private final BorrowDAO borrowDAO = new BorrowDAO();
    private Parent dashboardRoot; 
    private Timeline refreshTimeline;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = sessionManager.getCurrentUser();
        
        if (currentUser == null) {
            AlertUtil.showError("Error", "Session tidak valid!");
            handleLogout();
            return;
        }
        
        // Set welcome message
        if (welcomeLabel != null) {
            welcomeLabel.setText("Halo, " + currentUser.getNama());
        }
        if (roleLabel != null) {
            roleLabel.setText(getRoleDisplayName(currentUser.getRole()));
        }
        
        // Configure menu based on role
        configureMenuByRole(currentUser.getRole());
        
        // Load dashboard data
        loadDashboardStatistics();
        loadRecentBorrows();
        
        // Set active menu
        setActiveMenu(btnDashboard);
        
        
        // Start auto-refresh
        startAutoRefresh();
        
        System.out.println("✅ Dashboard initialized for: " + currentUser.getUsername());
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

    /**
     * Configure menu visibility based on role
     */
    private void configureMenuByRole(String role) {
        switch (role) {
            case "admin":
                // Admin can access everything
                btnBarang.setVisible(true);
                btnPeminjaman.setVisible(true);
                btnLaporan.setVisible(true);
                btnUser.setVisible(true);
                break;
                
            case "peminjam":
                // Peminjam can only access their own borrowing
                btnBarang.setVisible(true);
                btnPeminjaman.setVisible(true);
                btnLaporan.setVisible(true);
                btnUser.setVisible(false);
              
                break;
                
            case "instansi":
                // Instansi similar to peminjam
                btnBarang.setVisible(true);
                btnPeminjaman.setVisible(true);
                btnLaporan.setVisible(true);
                btnUser.setVisible(false);
                break;
                
            default:
                btnBarang.setVisible(false);
                btnPeminjaman.setVisible(false);
                btnLaporan.setVisible(false);
                btnUser.setVisible(false);
           
        }
    }
    
    /**
     * Load dashboard statistics
     */
    private void loadDashboardStatistics() {
        try {
            List<Barang> allBarang = barangDAO.getAll();
            
            int total = allBarang.size();
            int tersedia = (int) allBarang.stream()
                .filter(b -> b.getJumlahTersedia() > 0 && "tersedia".equals(b.getStatus()))
                .count();
            
            List<Borrow> activeBorrows = borrowDAO.getActiveBorrows();
            int dipinjam = activeBorrows.size();
            
            List<Borrow> overdueBorrows = borrowDAO.getOverdueBorrows();
            int overdue = overdueBorrows.size();
            
            // Update labels
            if (totalBarangLabel != null) totalBarangLabel.setText(String.valueOf(total));
            if (tersediaLabel != null) tersediaLabel.setText(String.valueOf(tersedia));
            if (dipinjamLabel != null) dipinjamLabel.setText(String.valueOf(dipinjam));
            if (overdueLabel != null) overdueLabel.setText(String.valueOf(overdue));
            
        } catch (Exception e) {
            System.err.println("Error loading statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
 * Start auto-refresh timeline
 */
    private void startAutoRefresh() {
        refreshTimeline = new Timeline(new KeyFrame(
            Duration.seconds(5),
            event -> {
                loadDashboardStatistics();
                loadRecentBorrows();
            }
        ));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
        System.out.println("✅ Auto-refresh started (every 5 seconds)");
    }

    /**
     * Stop auto-refresh
     */
    public void stopAutoRefresh() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
            System.out.println("⏹️ Auto-refresh stopped");
        }
    }
    /**
     * Load recent borrows
     */
    private void loadRecentBorrows() {
    try {
        List<Borrow> borrows;
        
        if (sessionManager.isAdmin()) {
            // Admin: SEMUA peminjaman aktif
            borrows = borrowDAO.getActiveBorrows();
            
        } else if (sessionManager.isPeminjam()) {
            // Peminjam: hanya milik dia
            Integer peminjamId = sessionManager.getCurrentRoleId();
            if (peminjamId != null) {
                borrows = borrowDAO.getByPeminjamId(peminjamId);
                borrows = borrows.stream()
                    .filter(b -> "dipinjam".equals(b.getStatusBarang()))
                    .collect(Collectors.toList());
            } else {
                borrows = new ArrayList<>();
            }
            
        } else if (sessionManager.isInstansi()) {
            // Instansi: peminjaman barang milik mereka
            Integer instansiId = sessionManager.getCurrentRoleId();
            List<Borrow> allBorrows = borrowDAO.getAll();
            borrows = allBorrows.stream()
                .filter(b -> {
                    com.inventaris.model.Barang barang = 
                        new com.inventaris.dao.BarangDAO().getByKode(b.getKodeBarang());
                    return barang != null && 
                           barang.getIdInstansi() != null &&
                           barang.getIdInstansi().equals(instansiId) &&
                           "dipinjam".equals(b.getStatusBarang());
                })
                .collect(Collectors.toList());
                
        } else {
            borrows = new ArrayList<>();
        }
        
        // Limit to 10 recent
        if (borrows.size() > 10) {
            borrows = borrows.subList(0, 10);
        }
        
        ObservableList<Borrow> observableList = FXCollections.observableArrayList(borrows);
        
        if (recentBorrowTable != null) {
            // Update UI on JavaFX thread
            javafx.application.Platform.runLater(() -> {
                recentBorrowTable.setItems(observableList);
            });
        }
        
    } catch (Exception e) {
        System.err.println("Error loading recent borrows: " + e.getMessage());
    }
}
    
    /**
     * Handle menu navigation
     */
    @FXML
    private void handleDashboard() {
        Main.loadContent("Home.fxml");
    }
    
     @FXML
    private void handleUser() {
      
        Main.loadContent("User.fxml");
    }
    
    
    
    @FXML
    private void handleBarang() {
      
        Main.loadContent("Barang.fxml");
    }
    
    @FXML
    private void handlePeminjaman() {
        System.out.println("➡️ Tombol Peminjaman ditekan");
        Main.loadContent("Peminjaman.fxml");
    }

    
    @FXML
    private void handleLaporan() {
        
        Main.loadContent("Laporan.fxml");
    }
    
   
    
    @FXML
    private void handleLogout() {
        if (AlertUtil.showLogoutConfirmation()) {
            User currentUser = sessionManager.getCurrentUser();
            if (currentUser != null) {
                LogActivityUtil.logLogout(currentUser.getUsername(), currentUser.getRole());
            }
            
            sessionManager.logout();
            Main.showLoginScreen();
        }
    }
    
    /**
     * Load screen into content area
     */
    private void loadScreen(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Parent screen = loader.load();
            
            
            
        } catch (Exception e) {
            System.err.println("Error loading screen: " + fxmlFile);
            e.printStackTrace();
            AlertUtil.showError("Error", "Gagal memuat halaman: " + fxmlFile);
        }
    }
    
    /**
     * Set active menu button
     */
    private void setActiveMenu(Button activeButton) {
        // Remove active class from all buttons
        btnDashboard.getStyleClass().remove("menu-button-active");
        btnBarang.getStyleClass().remove("menu-button-active");
        btnPeminjaman.getStyleClass().remove("menu-button-active");
        btnLaporan.getStyleClass().remove("menu-button-active");
      
        
        // Add active class to current button
        if (!activeButton.getStyleClass().contains("menu-button-active")) {
            activeButton.getStyleClass().add("menu-button-active");
        }
    }
    
    /**
     * Get role display name
     */
    private String getRoleDisplayName(String role) {
        switch (role) {
            case "admin": return "Administrator";
            case "peminjam": return "Peminjam";
            case "instansi": return "Instansi";
            default: return role;
        }
    }
}