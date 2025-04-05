/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class Model_SanPhamChiTiet {
    private int id;
    private String maSanPham;
    private String tenSanPham;
    private String danhMuc;
    private String chatLieu;
    private String kichCo;
    private String mauSac;
    private String nhaSanXuat;
    private String anhSanPham;
    private int donGia;
    private int soLuong;
    private String moTa;
    private int trangThai;

    public Model_SanPhamChiTiet() {
    }

    public Model_SanPhamChiTiet(int id, int donGia) {
        this.id = id;
        this.donGia = donGia;
    }

    public Model_SanPhamChiTiet(int id, String maSanPham, int soLuong) {
        this.id = id;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
    }
    
    public Model_SanPhamChiTiet(String maSanPham, String tenSanPham, String danhMuc, String chatLieu, String kichCo, String mauSac, String nhaSanXuat, String anhSanPham, int donGia, int soLuong, String moTa, int trangThai) {
        
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.danhMuc = danhMuc;
        this.chatLieu = chatLieu;
        this.kichCo = kichCo;
        this.mauSac = mauSac;
        this.nhaSanXuat = nhaSanXuat;
        this.anhSanPham = anhSanPham;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    public Model_SanPhamChiTiet(int id, String maSanPham, String tenSanPham, String danhMuc, String chatLieu, String kichCo, String mauSac, String nhaSanXuat, String anhSanPham, int donGia, int soLuong, String moTa, int trangThai) {
        this.id = id;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.danhMuc = danhMuc;
        this.chatLieu = chatLieu;
        this.kichCo = kichCo;
        this.mauSac = mauSac;
        this.nhaSanXuat = nhaSanXuat;
        this.anhSanPham = anhSanPham;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        this.chatLieu = chatLieu;
    }

    public String getKichCo() {
        return kichCo;
    }

    public void setKichCo(String kichCo) {
        this.kichCo = kichCo;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getNhaSanXuat() {
        return nhaSanXuat;
    }

    public void setNhaSanXuat(String nhaSanXuat) {
        this.nhaSanXuat = nhaSanXuat;
    }

    public String getAnhSanPham() {
        return anhSanPham;
    }

    public void setAnhSanPham(String anhSanPham) {
        this.anhSanPham = anhSanPham;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public Object[] toDataRow() {
        return new Object[] {getMaSanPham(), getTenSanPham(), getDanhMuc(), getSoLuong(), getDonGia(), getTrangThai() == 0 ? "Đang Hoạt Động":"Dừng Hoạt Động"};
    }
}
