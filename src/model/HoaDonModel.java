/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author ADMIN
 */
public class HoaDonModel {
    private int id;
    private String maHoaDon;
    private String loaiThanhToan;
    private int tongTien;
    private String tenKhachHang;
    private String soDienThoai;
    private String tenNhanVien;
    private int idVoucher;
    private int giaTri;
    private String tenVoucher;
    private String thoiGianTao;
    private int trangThai;
    private String loaiVoucher;
    
    

    public HoaDonModel() {
    }

    public HoaDonModel(int id, String maHoaDon) {
        this.id = id;
        this.maHoaDon = maHoaDon;
    }

    public HoaDonModel(int id, String maHoaDon, int tongTien, String tenNhanVien, String tenKhachHang, String thoiGianTao, int trangThai, String loaiThanhToan, String soDienThoai) {
        this.id = id;
        this.maHoaDon = maHoaDon;
        this.tongTien = tongTien;
        this.tenKhachHang = tenKhachHang;
        this.tenNhanVien = tenNhanVien;
        this.thoiGianTao = thoiGianTao;
        this.trangThai = trangThai;
        this.loaiThanhToan = loaiThanhToan;
        this.soDienThoai = soDienThoai;
    }

    public HoaDonModel(int id, String maHoaDon, String loaiThanhToan, int tongTien, String tenKhachHang, String soDienThoai, String tenNhanVien, int idVoucher, String thoiGianTao, int trangThai) {
        this.id = id;
        this.maHoaDon = maHoaDon;
        this.loaiThanhToan = loaiThanhToan;
        this.tongTien = tongTien;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.tenNhanVien = tenNhanVien;
        this.idVoucher = idVoucher;
        this.thoiGianTao = thoiGianTao;
        this.trangThai = trangThai;
    }

    public HoaDonModel(int id, String maHoaDon, String tenNhanVien, String tenKhachHang, String soDienThoai, String thoiGianTao, String loaiThanhToan, int tongTien, String tenVoucher, int trangThai) {
        this.id = id;
        this.maHoaDon = maHoaDon;
        this.tenNhanVien = tenNhanVien;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.thoiGianTao = thoiGianTao;
        this.loaiThanhToan = loaiThanhToan;
        this.tongTien = tongTien;
        this.tenVoucher = tenVoucher;
        this.trangThai = trangThai;
    }
    

    public HoaDonModel(int id, String maHoaDon, String tenNhanVien, String tenKhachHang, String soDienThoai, String thoiGianTao, String loaiThanhToan, int tongTien, int giaTri, int trangThai) {
        this.id = id;
        this.maHoaDon = maHoaDon;
        this.tenNhanVien = tenNhanVien;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.thoiGianTao = thoiGianTao;
        this.loaiThanhToan = loaiThanhToan;
        this.tongTien = tongTien;
        this.giaTri = giaTri;
        this.trangThai = trangThai;
    }

    public HoaDonModel(int id, String maHoaDon, String tenNhanVien, String tenKhachHang, String soDienThoai, String thoiGianTao, String loaiThanhToan, int tongTien, int giaTri, int trangThai, String loaiVoucher) {
        this.id = id;
        this.maHoaDon = maHoaDon;
        this.tenNhanVien = tenNhanVien;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.thoiGianTao = thoiGianTao;
        this.loaiThanhToan = loaiThanhToan;
        this.tongTien = tongTien;
        this.giaTri = giaTri;
        this.trangThai = trangThai;
        this.loaiVoucher = loaiVoucher;
    }
    
    public String getLoaiVoucher() {
        return loaiVoucher;
    }

    public void setLoaiVoucher(String loaiVoucher) {
        this.loaiVoucher = loaiVoucher;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getLoaiThanhToan() {
        return loaiThanhToan;
    }

    public void setLoaiThanhToan(String loaiThanhToan) {
        this.loaiThanhToan = loaiThanhToan;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public int getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(int idVoucher) {
        this.idVoucher = idVoucher;
    }

    public String getTenVoucher() {
        return tenVoucher;
    }

    public void setTenVoucher(String tenVoucher) {
        this.tenVoucher = tenVoucher;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getThoiGianTao() {
        return thoiGianTao;
    }

    public void setThoiGianTao(String thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }

    public int getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(int giaTri) {
        this.giaTri = giaTri;
    }

    @Override
    public String toString() {
        return "HoaDonModel{" + "id=" + id + ", maHoaDon=" + maHoaDon + ", loaiThanhToan=" + loaiThanhToan + ", tongTien=" + tongTien + ", tenKhachHang=" + tenKhachHang + ", soDienThoai=" + soDienThoai + ", tenNhanVien=" + tenNhanVien + ", idVoucher=" + idVoucher + ", tenVoucher=" + tenVoucher + ", thoiGianTao=" + thoiGianTao + ", trangThai=" + trangThai + '}';
    }

    public Object[] toDataRow() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String tongTienFormatted = currencyFormat.format(getTongTien());
        String ten = getTenKhachHang() != null ? getTenKhachHang() : "Khách Vãng Lai";
        return new Object[]{
            getMaHoaDon(),
            getTenNhanVien(),
            ten,
            getThoiGianTao(),
            getTrangThai() == 1 ? "Chưa Thanh Toán" : "Đã Thanh Toán",
            tongTienFormatted
        };
    }

    public Object[] toDataRowHoaDon() {
        return new Object[]{getMaHoaDon(), getTenNhanVien(), getTenKhachHang(), getThoiGianTao(), getTongTien(), getTrangThai() == 1 ? "Chưa Thanh Toán" : "Đã Thanh Toán"};
    }
}
