/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Image;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.ImageIcon;

/**
 *
 * @author ADMIN
 */
public class SanPhamChiTiet1 {
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
    private int idLoaiSanPham;
    private int trangThai;
    private String tenMau;
    private int kichCo;
    private Date ngayTao;
    private Date ngaySua;

    public SanPhamChiTiet1() {
    }

    public SanPhamChiTiet1(int id, String maSanPhamChiTiet, String tenSanPhamChiTiet, BigDecimal giaSanPham, String anhSanPham, int soLuong, String moTa, int idChatLieu, int idKichCo, int idMauSac, int idLoaiSanPham, int trangThai, Date ngayTao, Date ngaySua) {
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
        this.idLoaiSanPham = idLoaiSanPham;
        this.trangThai = trangThai;
        this.ngayTao = ngayTao;
        this.ngaySua = ngaySua;
    }

    public SanPhamChiTiet1(int id, String tenSanPhamChiTiet, BigDecimal giaSanPham, String anhSanPham, int soLuong, String tenMau, int kichCo) {
        this.id = id;
        this.tenSanPhamChiTiet = tenSanPhamChiTiet;
        this.giaSanPham = giaSanPham;
        this.anhSanPham = anhSanPham;
        this.soLuong = soLuong;
        this.tenMau = tenMau;
        this.kichCo = kichCo;
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

    public String getTenMau() {
        return tenMau;
    }

    public void setTenMau(String tenMau) {
        this.tenMau = tenMau;
    }

    public int getKichCo() {
        return kichCo;
    }

    public void setKichCo(int kichCo) {
        this.kichCo = kichCo;
    }

    @Override
    public String toString() {
        return "SanPhamChiTiet{" + "id=" + id + ", maSanPhamChiTiet=" + maSanPhamChiTiet + ", tenSanPhamChiTiet=" + tenSanPhamChiTiet + ", giaSanPham=" + giaSanPham + ", anhSanPham=" + anhSanPham + ", soLuong=" + soLuong + ", moTa=" + moTa + ", idChatLieu=" + idChatLieu + ", idKichCo=" + idKichCo + ", idMauSac=" + idMauSac + ", idLoaiSanPham=" + idLoaiSanPham + ", trangThai=" + trangThai + ", ngayTao=" + ngayTao + ", ngaySua=" + ngaySua + '}';
    }

    private ImageIcon resizeImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public Object[] getData() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String giaFormatted = currencyFormat.format(getGiaSanPham());

        return new Object[]{
            resizeImage(getAnhSanPham(), 80, 80),
            getTenSanPhamChiTiet(),
            giaFormatted,
            getSoLuong(),
            getKichCo(),
            getTenMau()
        };
    }
}
