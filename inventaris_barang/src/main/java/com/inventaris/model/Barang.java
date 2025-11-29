// ================================================================
// File: src/main/java/com/inventaris/model/Barang.java
// ================================================================
package com.inventaris.model;

import java.sql.Timestamp;

/**
 * Barang Model Class
 * Represents an item in the inventory system
 */
public class Barang {
    
    // Attributes
    private int idBarang;
    private Integer idInstansi; // ID pemilik (instansi)
    private String kodeBarang;
    private String namaBarang;
    private String lokasiBarang;
    private int jumlahTotal;
    private int jumlahTersedia;
    private String deskripsi;
    private String kondisiBarang; // baik, rusak ringan, rusak berat
    private String status; // tersedia, dipinjam, rusak, hilang
    private String foto;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Extended property
    private String namaPemilik; // Nama instansi pemilik (from JOIN)
    
    // ============================================================
    // CONSTRUCTORS
    // ============================================================
    
    /**
     * Default constructor
     */
    public Barang() {
        // Empty constructor
    }
    
    /**
     * Constructor with basic fields
     */
    public Barang(String kodeBarang, String namaBarang, String lokasiBarang, 
                  int jumlahTotal, String deskripsi) {
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.lokasiBarang = lokasiBarang;
        this.jumlahTotal = jumlahTotal;
        this.jumlahTersedia = jumlahTotal;
        this.deskripsi = deskripsi;
        this.kondisiBarang = "baik";
        this.status = "tersedia";
    }
    
    /**
     * Constructor with all fields
     */
    public Barang(int idBarang, String kodeBarang, String namaBarang, 
                  String lokasiBarang, int jumlahTotal, int jumlahTersedia, 
                  String deskripsi, String kondisiBarang, String status, 
                  String foto, Timestamp createdAt, Timestamp updatedAt) {
        this.idBarang = idBarang;
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.lokasiBarang = lokasiBarang;
        this.jumlahTotal = jumlahTotal;
        this.jumlahTersedia = jumlahTersedia;
        this.deskripsi = deskripsi;
        this.kondisiBarang = kondisiBarang;
        this.status = status;
        this.foto = foto;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // ============================================================
    // GETTERS AND SETTERS
    // ============================================================
    
    public int getIdBarang() {
        return idBarang;
    }
    
    public void setIdBarang(int idBarang) {
        this.idBarang = idBarang;
    }
    
    public String getKodeBarang() {
        return kodeBarang;
    }
    
    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }
    
    public String getNamaBarang() {
        return namaBarang;
    }
    
    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }
    
    public String getLokasiBarang() {
        return lokasiBarang;
    }
    
    public void setLokasiBarang(String lokasiBarang) {
        this.lokasiBarang = lokasiBarang;
    }
    
    public int getJumlahTotal() {
        return jumlahTotal;
    }
    
    public void setJumlahTotal(int jumlahTotal) {
        this.jumlahTotal = jumlahTotal;
    }
    
    public int getJumlahTersedia() {
        return jumlahTersedia;
    }
    
    public void setJumlahTersedia(int jumlahTersedia) {
        this.jumlahTersedia = jumlahTersedia;
    }
    
    public String getDeskripsi() {
        return deskripsi;
    }
    
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
    
    public String getKondisiBarang() {
        return kondisiBarang;
    }
    
    public void setKondisiBarang(String kondisiBarang) {
        this.kondisiBarang = kondisiBarang;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getFoto() {
        return foto;
    }
    
    public void setFoto(String foto) {
        this.foto = foto;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // ============================================================
    // HELPER METHODS
    // ============================================================
    
    /**
     * Check if barang is available
     */
    public boolean isAvailable() {
        return jumlahTersedia > 0 && "tersedia".equalsIgnoreCase(status);
    }
    
    /**
     * Check if barang is good condition
     */
    public boolean isGoodCondition() {
        return "baik".equalsIgnoreCase(kondisiBarang);
    }
    
    /**
     * Get stock percentage
     */
    public double getStockPercentage() {
        if (jumlahTotal == 0) return 0;
        return ((double) jumlahTersedia / jumlahTotal) * 100;
    }
    
    // ============================================================
    // OVERRIDE METHODS
    // ============================================================
    
    @Override
    public String toString() {
        return namaBarang + " (" + kodeBarang + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Barang barang = (Barang) obj;
        return idBarang == barang.idBarang;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idBarang);
    }
    public Integer getIdInstansi() {
    return idInstansi;
}

public void setIdInstansi(Integer idInstansi) {
    this.idInstansi = idInstansi;
}

public String getNamaPemilik() {
    return namaPemilik;
}

public void setNamaPemilik(String namaPemilik) {
    this.namaPemilik = namaPemilik;
}

}