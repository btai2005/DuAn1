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
public class HoaDonChiTietModel {

    private int id;
    private String ma;
    private String ten;
    private int gia;
    private int soLuong;
    private int tongTien;

    private String tenKhachHang;
    private String soDienThoai;
    private String ngayTao;
    private String ngaySua;
    private int trangThai;
    private int chiPhiKhac;
    private String loaiThanhToan;
    private String idHoaDon;
    private int idSPCT;
    private int thanhTien;

    public HoaDonChiTietModel() {
    }

    public HoaDonChiTietModel(String ten, int gia, int soLuong, int tongTien) {
        this.ten = ten;
        this.gia = gia;
        this.soLuong = soLuong;
        this.tongTien = tongTien;
    }

    public HoaDonChiTietModel(String ma, String ten, int gia, int soLuong, int tongTien) {
        this.ma = ma;
        this.ten = ten;
        this.gia = gia;
        this.soLuong = soLuong;
        this.tongTien = tongTien;
    }

    public HoaDonChiTietModel(int id, String ma, String ten, int gia, int soLuong, int tongTien) {
        this.id = id;
        this.ma = ma;
        this.ten = ten;
        this.gia = gia;
        this.soLuong = soLuong;
        this.tongTien = tongTien;
    }

    public HoaDonChiTietModel(int id, String ma, String tenKhachHang, String soDienThoai, String ngayTao, String loaiThanhToan, int trangThai, int tongTien, String ten, int gia, int soLuong, int thanhTien) {
        this.id = id;
        this.ma = ma;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.loaiThanhToan = loaiThanhToan;
        this.ten = ten;
        this.gia = gia;
        this.soLuong = soLuong;
        this.thanhTien = thanhTien;
    }

    public HoaDonChiTietModel(int id, String ma, String tenKhachHang, String soDienThoai, String ngayTao, String loaiThanhToan, int trangThai, int tongTien) {
        this.id = id;
        this.ma = ma;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.loaiThanhToan = loaiThanhToan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getTongTien() {
        return tongTien;
    }

    public int getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public int getIdSPCT() {
        return idSPCT;
    }

    public void setIdSPCT(int idSPCT) {
        this.idSPCT = idSPCT;
    }

    public HoaDonChiTietModel(int id, int idSPCT) {
        this.id = id;
        this.idSPCT = idSPCT;
    }

    public Object[] toDataRow() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        double gia = getGia();
        int soLuong = getSoLuong();
        double thanhTien = gia * soLuong;

        String giaFormatted = currencyFormat.format(gia);
        String thanhTienFormatted = currencyFormat.format(thanhTien);

        return new Object[]{
            getMa(),
            getTen(),
            giaFormatted, 
            soLuong,
            thanhTienFormatted 
        };
    }

}
