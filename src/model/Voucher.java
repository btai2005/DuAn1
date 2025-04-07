/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Voucher {
    public static void setRowCount(int i) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    private int id;
    private String maVoucher;
    private String tenVoucher;
    private String ngayBatDau; // Changed to String
    private String ngayKetThuc; // Changed to String
    private String loaiVoucher;
    private double giaTri;  // Kept as String
    private int trangThai;

    public Voucher() {
    }

    public Voucher(int id, String maVoucher, String tenVoucher, String ngayBatDau, String ngayKetThuc, String loaiVoucher, Double giaTri, int trangThai) {
        this.id = id;
        this.maVoucher = maVoucher;
        this.tenVoucher = tenVoucher;
        this.ngayBatDau = ngayBatDau; // Updated
        this.ngayKetThuc = ngayKetThuc; // Updated
        this.loaiVoucher = loaiVoucher;
        this.giaTri = giaTri; 
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public String getTenVoucher() {
        return tenVoucher;
    }

    public void setTenVoucher(String tenVoucher) {
        this.tenVoucher = tenVoucher;
    }

    public String getNgayBatDau() { // Updated
        return ngayBatDau; // Updated
    }

    public void setNgayBatDau(String ngayBatDau) { // Updated
        this.ngayBatDau = ngayBatDau; // Updated
    }

    public String getNgayKetThuc() { // Updated
        return ngayKetThuc; // Updated
    }

    public void setNgayKetThuc(String ngayKetThuc) { // Updated
        this.ngayKetThuc = ngayKetThuc; // Updated
    }

    public String getLoaiVoucher() {
        return loaiVoucher;
    }

    public void setLoaiVoucher(String loaiVoucher) {
        this.loaiVoucher = loaiVoucher;
    }

    public Double getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(Double giaTri) {
        this.giaTri = giaTri;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public Object[] toDataRow() {
        return new Object[]{
            this.getId(),
            this.getMaVoucher(),
            this.getTenVoucher(),
            this.getNgayBatDau(), // Updated
            this.getNgayKetThuc(), // Updated
            this.getLoaiVoucher(),
            this.getGiaTri(),
            this.getTrangThai()
        };
    }
    
    // Thêm phương thức tinhTrangThai()
    public int tinhTrangThai() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Kiểm tra null cho ngayBatDau và ngayKetThuc
            if (this.ngayBatDau == null || this.ngayKetThuc == null) {
                return 1; // Trả về "Hết hạn" nếu ngày bắt đầu hoặc ngày kết thúc là null
            }

            Date ngayHienTai = new Date(); // Ngày hiện tại
            Date batDau = sdf.parse(this.ngayBatDau);
            Date ketThuc = sdf.parse(this.ngayKetThuc);

            // Nếu ngày hiện tại nằm trong khoảng từ ngày bắt đầu đến ngày kết thúc
            // Cần kiểm tra cả trường hợp ngày hiện tại bằng ngày bắt đầu hoặc ngày kết thúc
            if ((ngayHienTai.after(batDau) || ngayHienTai.equals(batDau)) && 
                (ngayHienTai.before(ketThuc) || ngayHienTai.equals(ketThuc))) {
                return 0; // Còn hạn
            } else {
                return 1; // Hết hạn
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 1; // Mặc định là hết hạn nếu có lỗi
        }
    }
}
