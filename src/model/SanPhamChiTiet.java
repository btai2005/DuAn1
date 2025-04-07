/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Dang
 */
public class SanPhamChiTiet {
    private int id;                    
    private String maSanPhamChiTiet;   
    private String tenSanPhamChiTiet;  
    private BigDecimal giaSanPham;     
    private String anhSanPham;         
    private int soLuong;               
    private String moTa;               
    private int idChatLieu;                       
    private int idKichCo;              
    private int idMauSac;
    private int idNSX;
    private int idLoaiSanPham;             
    private int trangThai;             
    private Date ngayTao;              
    private Date ngaySua;

    public SanPhamChiTiet() {
    }

    public SanPhamChiTiet(int id, String maSanPhamChiTiet, String tenSanPhamChiTiet, BigDecimal giaSanPham, String anhSanPham, int soLuong, String moTa, int idChatLieu, int idKichCo, int idMauSac, int idNSX, int idLoaiSanPham, int trangThai, Date ngayTao, Date ngaySua) {
        this.id = id;
        this.maSanPhamChiTiet = maSanPhamChiTiet;
        this.tenSanPhamChiTiet = tenSanPhamChiTiet;
        this.giaSanPham = giaSanPham;
        this.anhSanPham = anhSanPham;
        this.soLuong = soLuong;
        this.moTa = moTa;
        this.idChatLieu = idChatLieu;
        this.idKichCo = idKichCo;
        this.idMauSac = idMauSac;
        this.idNSX = idNSX;
        this.idLoaiSanPham = idLoaiSanPham;
        this.trangThai = trangThai;
        this.ngayTao = ngayTao;
        this.ngaySua = ngaySua;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaSanPhamChiTiet() {
        return maSanPhamChiTiet;
    }

    public void setMaSanPhamChiTiet(String maSanPhamChiTiet) {
        this.maSanPhamChiTiet = maSanPhamChiTiet;
    }

    public String getTenSanPhamChiTiet() {
        return tenSanPhamChiTiet;
    }

    public void setTenSanPhamChiTiet(String tenSanPhamChiTiet) {
        this.tenSanPhamChiTiet = tenSanPhamChiTiet;
    }

    public BigDecimal getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(BigDecimal giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public String getAnhSanPham() {
        return anhSanPham;
    }

    public void setAnhSanPham(String anhSanPham) {
        this.anhSanPham = anhSanPham;
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

    public int getIdChatLieu() {
        return idChatLieu;
    }

    public void setIdChatLieu(int idChatLieu) {
        this.idChatLieu = idChatLieu;
    }

    public int getIdKichCo() {
        return idKichCo;
    }

    public void setIdKichCo(int idKichCo) {
        this.idKichCo = idKichCo;
    }

    public int getIdMauSac() {
        return idMauSac;
    }

    public void setIdMauSac(int idMauSac) {
        this.idMauSac = idMauSac;
    }

    public int getIdNSX() {
        return idNSX;
    }

    public void setIdNSX(int idNSX) {
        this.idNSX = idNSX;
    }

    public int getIdLoaiSanPham() {
        return idLoaiSanPham;
    }

    public void setIdLoaiSanPham(int idLoaiSanPham) {
        this.idLoaiSanPham = idLoaiSanPham;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Date getNgaySua() {
        return ngaySua;
    }

    public void setNgaySua(Date ngaySua) {
        this.ngaySua = ngaySua;
    }

    @Override
    public String toString() {
        return "SanPhamChiTiet{" + "id=" + id + ", maSanPhamChiTiet=" + maSanPhamChiTiet + ", tenSanPhamChiTiet=" + tenSanPhamChiTiet + ", giaSanPham=" + giaSanPham + ", anhSanPham=" + anhSanPham + ", soLuong=" + soLuong + ", moTa=" + moTa + ", idChatLieu=" + idChatLieu + ", idKichCo=" + idKichCo + ", idMauSac=" + idMauSac + ", idNSX=" + idNSX + ", idLoaiSanPham=" + idLoaiSanPham + ", trangThai=" + trangThai + ", ngayTao=" + ngayTao + ", ngaySua=" + ngaySua + '}';
    }

    public Object[] getData () {
        return new Object[] {
//            resizeImage(getAnhSanPham(), 30, 30),
                getMaSanPhamChiTiet(),
                    getTenSanPhamChiTiet(), 
                        getGiaSanPham(),
                            getSoLuong()
        };
    }
}
