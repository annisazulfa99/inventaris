// ================================================================
// File: src/main/java/com/inventaris/controller/UserController.java
// ADMIN ONLY - Kelola User, Peminjaman, dan Laporan
// ================================================================
package com.inventaris.controller;

import com.inventaris.Main;

import com.inventaris.dao.BorrowDAO;
import com.inventaris.dao.LaporDAO;
import com.inventaris.dao.UserDAO;
import com.inventaris.model.Borrow;
import com.inventaris.model.Lapor;
import com.inventaris.model.User;
import com.inventaris.util.AlertUtil;
import com.inventaris.util.LogActivityUtil;
import com.inventaris.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;

/**
 * UserController - Admin Management Panel
 * Manage users, borrowings, and reports (Admin Only)
 */
public class UserController implements Initializable {
    
    // ============================================================
    // TAB 1: KELOLA USER
    // ============================================================
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colUserId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colNama;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colStatus;
    @FXML private TableColumn<User, Void> colUserAction;
    
    @FXML private TextField searchUserField;
    
    
    @FXML private VBox sidebarMenu;
    @FXML private Button btnDashboard;
    @FXML private Button btnBarang;
    @FXML private Button btnPeminjaman;
    @FXML private Button btnLaporan;
    @FXML private Button btnLogout;
    @FXML private Button btnUser;

    
    // ============================================================
    // TAB 2: KELOLA PEMINJAMAN
    // ============================================================
    @FXML private TableView<Borrow> allBorrowTable;
    @FXML private TableColumn<Borrow, Integer> colBorrowId;
    @FXML private TableColumn<Borrow, String> colBorrowPeminjam;
    @FXML private TableColumn<Borrow, String> colBorrowBarang;
    @FXML private TableColumn<Borrow, LocalDate> colBorrowTglPinjam;
    @FXML private TableColumn<Borrow, LocalDate> colBorrowDeadline;
    @FXML private TableColumn<Borrow, String> colBorrowStatus;
    @FXML private TableColumn<Borrow, Void> colBorrowAction;
    
    @FXML private ComboBox<String> filterStatusBorrow;
    
    // ============================================================
    // TAB 3: KELOLA LAPORAN
    // ============================================================
    @FXML private TableView<Lapor> allLaporTable;
    @FXML private TableColumn<Lapor, String> colLaporNo;
    @FXML private TableColumn<Lapor, String> colLaporPeminjam;
    @FXML private TableColumn<Lapor, String> colLaporBarang;
    @FXML private TableColumn<Lapor, LocalDate> colLaporTgl;
    @FXML private TableColumn<Lapor, String> colLaporStatus;
    @FXML private TableColumn<Lapor, Void> colLaporAction;
    
    @FXML private ComboBox<String> filterStatusLapor;
    
    // DAOs
    private final UserDAO userDAO = new UserDAO();
    private final BorrowDAO borrowDAO = new BorrowDAO();
    private final LaporDAO laporDAO = new LaporDAO();
    private final SessionManager sessionManager = SessionManager.getInstance();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Check admin access
        if (!sessionManager.isAdmin()) {
            AlertUtil.showError("Access Denied", "Hanya admin yang dapat mengakses halaman ini!");
            return;
        }
        
        // Setup tables
        setupUserTable();
        setupBorrowTable();
        setupLaporTable();
        
        // Setup filters
        setupFilters();
        
        // Load data
        loadAllUsers();
        loadAllBorrows();
        loadAllLaporan();
        
        System.out.println("✅ User Management initialized");
    }
    
    // ============================================================
    // TAB 1: USER MANAGEMENT
    // ============================================================
    
    /**
     * Setup user table
     */
    private void setupUserTable() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Status with color
        colStatus.setCellFactory(col -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("aktif".equals(status)) {
                        setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;");
                    } else {
                        setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                    }
                }
            }
        });
        
        // Action buttons
        colUserAction.setCellFactory(col -> new TableCell<User, Void>() {
            private final Button btnToggle = new Button();
            private final Button btnReset = new Button("🔑 Reset");
            private final HBox buttons = new HBox(5, btnToggle, btnReset);
            
            {
                btnToggle.getStyleClass().add("btn-warning");
                btnReset.getStyleClass().add("btn-secondary");
                
                btnToggle.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    toggleUserStatus(user);
                });
                
                btnReset.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    resetPassword(user);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    btnToggle.setText("aktif".equals(user.getStatus()) ? "❌ Nonaktifkan" : "✅ Aktifkan");
                    setGraphic(buttons);
                }
            }
        });
    }
    
    /**
     * Load all users
     */
    private void loadAllUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            ObservableList<User> observableList = FXCollections.observableArrayList(users);
            userTable.setItems(observableList);
        } catch (Exception e) {
            AlertUtil.showError("Error", "Gagal memuat data user!");
            e.printStackTrace();
        }
    }
    
    /**
     * Toggle user status
     */
    private void toggleUserStatus(User user) {
        String newStatus = "aktif".equals(user.getStatus()) ? "nonaktif" : "aktif";
        String action = "aktif".equals(newStatus) ? "mengaktifkan" : "menonaktifkan";
        
        if (!AlertUtil.showConfirmation("Konfirmasi", "Yakin " + action + " user " + user.getUsername() + "?")) {
            return;
        }
        
        user.setStatus(newStatus);
        if (userDAO.updateUser(user)) {
            AlertUtil.showSuccess("Berhasil", "Status user berhasil diubah!");
            LogActivityUtil.log(
                sessionManager.getCurrentUsername(),
                action + " user: " + user.getUsername(),
                "UPDATE_USER_STATUS",
                sessionManager.getCurrentRole()
            );
            loadAllUsers();
        } else {
            AlertUtil.showError("Gagal", "Gagal mengubah status user!");
        }
    }
    
    /**
     * Reset user password
     */
    private void resetPassword(User user) {
        String newPassword = AlertUtil.showInputDialog(
            "Reset Password",
            "Reset password untuk: " + user.getUsername(),
            "Masukkan password baru:"
        );
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return;
        }
        
        if (userDAO.changePassword(user.getIdUser(), newPassword)) {
            AlertUtil.showSuccess("Berhasil", "Password berhasil direset!");
            LogActivityUtil.log(
                sessionManager.getCurrentUsername(),
                "Reset password user: " + user.getUsername(),
                "RESET_PASSWORD",
                sessionManager.getCurrentRole()
            );
        } else {
            AlertUtil.showError("Gagal", "Gagal reset password!");
        }
    }
    
    /**
     * Search users
     */
    @FXML
    private void handleSearchUser() {
        String keyword = searchUserField.getText().trim();
        if (keyword.isEmpty()) {
            loadAllUsers();
            return;
        }
        
        List<User> allUsers = userDAO.getAllUsers();
        allUsers.removeIf(u -> 
            !u.getUsername().toLowerCase().contains(keyword.toLowerCase()) &&
            !u.getNama().toLowerCase().contains(keyword.toLowerCase())
        );
        
        ObservableList<User> observableList = FXCollections.observableArrayList(allUsers);
        userTable.setItems(observableList);
    }
    
    // ============================================================
    // TAB 2: BORROW MANAGEMENT
    // ============================================================
    
    /**
     * Setup borrow table
     */
    private void setupBorrowTable() {
        colBorrowId.setCellValueFactory(new PropertyValueFactory<>("idPeminjaman"));
        colBorrowPeminjam.setCellValueFactory(new PropertyValueFactory<>("namaPeminjam"));
        colBorrowBarang.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
        colBorrowTglPinjam.setCellValueFactory(new PropertyValueFactory<>("tglPinjam"));
        colBorrowDeadline.setCellValueFactory(new PropertyValueFactory<>("dlKembali"));
        colBorrowStatus.setCellValueFactory(new PropertyValueFactory<>("statusBarang"));
        
        // Status with color
        colBorrowStatus.setCellFactory(col -> new TableCell<Borrow, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "pending":
                            setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404;");
                            break;
                        case "dipinjam":
                            setStyle("-fx-background-color: #cce5ff; -fx-text-fill: #004085;");
                            break;
                        case "dikembalikan":
                            setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;");
                            break;
                        default:
                            setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                    }
                }
            }
        });
        
        // Action buttons
        colBorrowAction.setCellFactory(col -> new TableCell<Borrow, Void>() {
            private final Button btnApprove = new Button("✅");
            private final Button btnReject = new Button("❌");
            private final Button btnDetail = new Button("👁️");
            private final HBox buttons = new HBox(5, btnApprove, btnReject, btnDetail);
            
            {
                btnApprove.getStyleClass().add("btn-success");
                btnReject.getStyleClass().add("btn-danger");
                btnDetail.getStyleClass().add("btn-secondary");
                
                btnApprove.setOnAction(e -> {
                    Borrow borrow = getTableView().getItems().get(getIndex());
                    approveBorrow(borrow);
                });
                
                btnReject.setOnAction(e -> {
                    Borrow borrow = getTableView().getItems().get(getIndex());
                    rejectBorrow(borrow);
                });
                
                btnDetail.setOnAction(e -> {
                    Borrow borrow = getTableView().getItems().get(getIndex());
                    showBorrowDetail(borrow);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Borrow borrow = getTableView().getItems().get(getIndex());
                    if ("pending".equals(borrow.getStatusBarang())) {
                        setGraphic(buttons);
                    } else {
                        setGraphic(new HBox(btnDetail));
                    }
                }
            }
        });
    }
    
    /**
     * Load all borrows
     */
    private void loadAllBorrows() {
        try {
            List<Borrow> borrows = borrowDAO.getAll();
            
            // Filter by status if selected
            if (filterStatusBorrow != null && filterStatusBorrow.getValue() != null && 
                !"Semua".equals(filterStatusBorrow.getValue())) {
                String status = filterStatusBorrow.getValue().toLowerCase();
                borrows.removeIf(b -> !status.equals(b.getStatusBarang()));
            }
            
            ObservableList<Borrow> observableList = FXCollections.observableArrayList(borrows);
            allBorrowTable.setItems(observableList);
        } catch (Exception e) {
            AlertUtil.showError("Error", "Gagal memuat data peminjaman!");
            e.printStackTrace();
        }
    }
    
    /**
     * Approve borrow
     */
    private void approveBorrow(Borrow borrow) {
        if (!AlertUtil.showConfirmation("Konfirmasi", "Setujui peminjaman ini?")) {
            return;
        }
        
        Integer adminId = sessionManager.getCurrentRoleId();
        if (borrowDAO.approve(borrow.getIdPeminjaman(), adminId)) {
            AlertUtil.showSuccess("Berhasil", "Peminjaman disetujui!");
            LogActivityUtil.log(
                sessionManager.getCurrentUsername(),
                "Menyetujui peminjaman ID: " + borrow.getIdPeminjaman(),
                "APPROVE_BORROW",
                sessionManager.getCurrentRole()
            );
            loadAllBorrows();
        } else {
            AlertUtil.showError("Gagal", "Gagal menyetujui peminjaman!");
        }
    }
    
    /**
     * Reject borrow
     */
    private void rejectBorrow(Borrow borrow) {
        if (!AlertUtil.showConfirmation("Konfirmasi", "Tolak peminjaman ini?")) {
            return;
        }
        
        if (borrowDAO.reject(borrow.getIdPeminjaman())) {
            AlertUtil.showSuccess("Berhasil", "Peminjaman ditolak!");
            LogActivityUtil.log(
                sessionManager.getCurrentUsername(),
                "Menolak peminjaman ID: " + borrow.getIdPeminjaman(),
                "REJECT_BORROW",
                sessionManager.getCurrentRole()
            );
            loadAllBorrows();
        } else {
            AlertUtil.showError("Gagal", "Gagal menolak peminjaman!");
        }
    }
    
    /**
     * Show borrow detail
     */
    private void showBorrowDetail(Borrow borrow) {
        String detail = String.format(
            "ID: %d\nPeminjam: %s\nBarang: %s\nJumlah: %d\nTgl Pinjam: %s\nDeadline: %s\nStatus: %s",
            borrow.getIdPeminjaman(),
            borrow.getNamaPeminjam(),
            borrow.getNamaBarang(),
            borrow.getJumlahPinjam(),
            borrow.getTglPinjam(),
            borrow.getDlKembali(),
            borrow.getStatusBarang()
        );
        
        AlertUtil.showInfo("Detail Peminjaman", detail);
    }
    
    // ============================================================
    // TAB 3: LAPORAN MANAGEMENT
    // ============================================================
    
    /**
     * Setup laporan table
     */
    private void setupLaporTable() {
        colLaporNo.setCellValueFactory(new PropertyValueFactory<>("noLaporan"));
        colLaporPeminjam.setCellValueFactory(new PropertyValueFactory<>("namaPeminjam"));
        colLaporBarang.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
        colLaporTgl.setCellValueFactory(new PropertyValueFactory<>("tglLaporan"));
        colLaporStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Status with color
        colLaporStatus.setCellFactory(col -> new TableCell<Lapor, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "diproses":
                            setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404;");
                            break;
                        case "selesai":
                            setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;");
                            break;
                        case "ditolak":
                            setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                            break;
                    }
                }
            }
        });
        
        // Action buttons
        colLaporAction.setCellFactory(col -> new TableCell<Lapor, Void>() {
            private final Button btnSelesai = new Button("✅ Selesai");
            private final Button btnTolak = new Button("❌ Tolak");
            private final HBox buttons = new HBox(5, btnSelesai, btnTolak);
            
            {
                btnSelesai.getStyleClass().add("btn-success");
                btnTolak.getStyleClass().add("btn-danger");
                
                btnSelesai.setOnAction(e -> {
                    Lapor lapor = getTableView().getItems().get(getIndex());
                    processLapor(lapor, "selesai");
                });
                
                btnTolak.setOnAction(e -> {
                    Lapor lapor = getTableView().getItems().get(getIndex());
                    processLapor(lapor, "ditolak");
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Lapor lapor = getTableView().getItems().get(getIndex());
                    if ("diproses".equals(lapor.getStatus())) {
                        setGraphic(buttons);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }
    
    /**
     * Load all laporan
     */
    private void loadAllLaporan() {
        try {
            List<Lapor> laporList = laporDAO.getAll();
            
            // Filter by status if selected
            if (filterStatusLapor != null && filterStatusLapor.getValue() != null && 
                !"Semua".equals(filterStatusLapor.getValue())) {
                String status = filterStatusLapor.getValue().toLowerCase();
                laporList.removeIf(l -> !status.equals(l.getStatus()));
            }
            
            ObservableList<Lapor> observableList = FXCollections.observableArrayList(laporList);
            allLaporTable.setItems(observableList);
        } catch (Exception e) {
            AlertUtil.showError("Error", "Gagal memuat data laporan!");
            e.printStackTrace();
        }
    }
    
    /**
     * Process laporan
     */
    private void processLapor(Lapor lapor, String status) {
        String action = "selesai".equals(status) ? "menyelesaikan" : "menolak";
        
        if (!AlertUtil.showConfirmation("Konfirmasi", "Yakin " + action + " laporan ini?")) {
            return;
        }
        
        if (laporDAO.updateStatus(lapor.getIdLaporan(), status)) {
            AlertUtil.showSuccess("Berhasil", "Laporan berhasil di-" + action + "!");
            LogActivityUtil.log(
                sessionManager.getCurrentUsername(),
                action + " laporan: " + lapor.getNoLaporan(),
                "PROCESS_LAPORAN",
                sessionManager.getCurrentRole()
            );
            loadAllLaporan();
        } else {
            AlertUtil.showError("Gagal", "Gagal memproses laporan!");
        }
    }
    
    // ============================================================
    // FILTERS
    // ============================================================
    
    /**
     * Setup filters
     */
    private void setupFilters() {
        if (filterStatusBorrow != null) {
            filterStatusBorrow.setItems(FXCollections.observableArrayList(
                "Semua", "Pending", "Dipinjam", "Dikembalikan"
            ));
            filterStatusBorrow.setValue("Semua");
            filterStatusBorrow.setOnAction(e -> loadAllBorrows());
        }
        
        if (filterStatusLapor != null) {
            filterStatusLapor.setItems(FXCollections.observableArrayList(
                "Semua", "Diproses", "Selesai", "Ditolak"
            ));
            filterStatusLapor.setValue("Semua");
            filterStatusLapor.setOnAction(e -> loadAllLaporan());
        }
    }
    
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
    
   
    
    
    /**
     * Refresh all data
     */
    @FXML
    private void handleRefresh() {
        loadAllUsers();
        loadAllBorrows();
        loadAllLaporan();
        AlertUtil.showInfo("Refresh", "Data berhasil diperbarui!");
    }
}