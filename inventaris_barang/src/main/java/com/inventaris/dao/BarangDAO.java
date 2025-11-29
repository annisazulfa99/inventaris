// ================================================================
// File: src/main/java/com/inventaris/dao/BarangDAO.java
// ================================================================
package com.inventaris.dao;

import com.inventaris.config.DatabaseConfig;
import com.inventaris.model.Barang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * BarangDAO - Data Access Object for Barang
 * Handles all database operations related to Barang entity
 */
public class BarangDAO {
    
    private final DatabaseConfig dbConfig;
    
    /**
     * Constructor
     */
    public BarangDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    /**
     * Create new barang
     * 
     * @param barang Barang object
     * @return true if successful, false otherwise
     */
    public boolean create(Barang barang) {
        String sql = "INSERT INTO barang (kode_barang, nama_barang, lokasi_barang, jumlah_total, " +
                     "jumlah_tersedia, deskripsi, kondisi_barang, status, foto) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, barang.getKodeBarang());
            stmt.setString(2, barang.getNamaBarang());
            stmt.setString(3, barang.getLokasiBarang());
            stmt.setInt(4, barang.getJumlahTotal());
            stmt.setInt(5, barang.getJumlahTersedia());
            stmt.setString(6, barang.getDeskripsi());
            stmt.setString(7, barang.getKondisiBarang());
            stmt.setString(8, barang.getStatus());
            stmt.setString(9, barang.getFoto());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating barang: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get barang by kode
     * 
     * @param kode Kode barang
     * @return Barang object if found, null otherwise
     */
    public Barang getByKode(String kode) {
        String sql = "SELECT * FROM barang WHERE kode_barang = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, kode);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractBarangFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting barang: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get barang by ID
     * 
     * @param id Barang ID
     * @return Barang object if found, null otherwise
     */
    public Barang getById(int id) {
        String sql = "SELECT * FROM barang WHERE id_barang = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractBarangFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting barang by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all barang
     * 
     * @return List of all barang
     */
    public List<Barang> getAll() {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM barang ORDER BY created_at DESC";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(extractBarangFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all barang: " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get available barang (stok > 0 and status tersedia)
     * 
     * @return List of available barang
     */
    public List<Barang> getAvailable() {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM barang WHERE jumlah_tersedia > 0 AND status = 'tersedia' ORDER BY nama_barang";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(extractBarangFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting available barang: " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get barang by status
     * 
     * @param status Status barang
     * @return List of barang with specified status
     */
    public List<Barang> getByStatus(String status) {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM barang WHERE status = ? ORDER BY nama_barang";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractBarangFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting barang by status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Update barang
     * 
     * @param barang Barang object with updated information
     * @return true if successful, false otherwise
     */
    public boolean update(Barang barang) {
        String sql = "UPDATE barang SET nama_barang = ?, lokasi_barang = ?, jumlah_total = ?, " +
                     "jumlah_tersedia = ?, deskripsi = ?, kondisi_barang = ?, status = ?, foto = ? " +
                     "WHERE kode_barang = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, barang.getNamaBarang());
            stmt.setString(2, barang.getLokasiBarang());
            stmt.setInt(3, barang.getJumlahTotal());
            stmt.setInt(4, barang.getJumlahTersedia());
            stmt.setString(5, barang.getDeskripsi());
            stmt.setString(6, barang.getKondisiBarang());
            stmt.setString(7, barang.getStatus());
            stmt.setString(8, barang.getFoto());
            stmt.setString(9, barang.getKodeBarang());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating barang: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update jumlah tersedia
     * 
     * @param kodeBarang Kode barang
     * @param jumlah Jumlah yang akan ditambah/dikurang
     * @param isAdd true untuk menambah, false untuk mengurangi
     * @return true if successful, false otherwise
     */
    public boolean updateJumlahTersedia(String kodeBarang, int jumlah, boolean isAdd) {
        String sql;
        if (isAdd) {
            sql = "UPDATE barang SET jumlah_tersedia = jumlah_tersedia + ? WHERE kode_barang = ?";
        } else {
            sql = "UPDATE barang SET jumlah_tersedia = jumlah_tersedia - ? WHERE kode_barang = ?";
        }
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, jumlah);
            stmt.setString(2, kodeBarang);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating jumlah tersedia: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete barang
     * 
     * @param kode Kode barang
     * @return true if successful, false otherwise
     */
    public boolean delete(String kode) {
        String sql = "DELETE FROM barang WHERE kode_barang = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, kode);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting barang: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Search barang by keyword (kode, nama, or lokasi)
     * 
     * @param keyword Search keyword
     * @return List of matching barang
     */
    public List<Barang> search(String keyword) {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM barang WHERE kode_barang LIKE ? OR nama_barang LIKE ? OR lokasi_barang LIKE ? ORDER BY nama_barang";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(extractBarangFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching barang: " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Check if kode barang exists
     * 
     * @param kodeBarang Kode barang to check
     * @return true if exists, false otherwise
     */
    public boolean kodeExists(String kodeBarang) {
        String sql = "SELECT COUNT(*) FROM barang WHERE kode_barang = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, kodeBarang);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking kode barang: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get total barang count
     * 
     * @return Total count of barang
     */
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM barang";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    /**
 * Get barang by instansi ID
 * 
 * @param idInstansi ID instansi pemilik barang
 * @return List of barang owned by the instansi
 */
public List<Barang> getByInstansi(Integer idInstansi) {
    List<Barang> list = new ArrayList<>();
    String sql = "SELECT * FROM barang WHERE id_instansi = ? ORDER BY nama_barang";

    try (Connection conn = dbConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idInstansi);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(extractBarangFromResultSet(rs));
        }

    } catch (SQLException e) {
        System.err.println("Error getting barang by instansi: " + e.getMessage());
        e.printStackTrace();
    }

    return list;
}

    /**
     * Get barang with low stock (< 5)
     * 
     * @return List of barang with low stock
     */
    public List<Barang> getLowStock() {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM barang WHERE jumlah_tersedia < 5 AND jumlah_tersedia > 0 ORDER BY jumlah_tersedia";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(extractBarangFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting low stock barang: " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    // ============================================================
    // HELPER METHODS
    // ============================================================
    
    /**
     * Extract Barang object from ResultSet
     * 
     * @param rs ResultSet
     * @return Barang object
     * @throws SQLException if error occurs
     */
    private Barang extractBarangFromResultSet(ResultSet rs) throws SQLException {
        Barang barang = new Barang();
        barang.setIdBarang(rs.getInt("id_barang"));
        barang.setKodeBarang(rs.getString("kode_barang"));
        barang.setNamaBarang(rs.getString("nama_barang"));
        barang.setLokasiBarang(rs.getString("lokasi_barang"));
        barang.setJumlahTotal(rs.getInt("jumlah_total"));
        barang.setJumlahTersedia(rs.getInt("jumlah_tersedia"));
        barang.setDeskripsi(rs.getString("deskripsi"));
        barang.setKondisiBarang(rs.getString("kondisi_barang"));
        barang.setStatus(rs.getString("status"));
        barang.setFoto(rs.getString("foto"));
        barang.setCreatedAt(rs.getTimestamp("created_at"));
        barang.setUpdatedAt(rs.getTimestamp("updated_at"));
        return barang;
    }
}