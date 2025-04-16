/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import Utils.Dbconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.NhanVienLoginInfo;

/**
 *
 * @author ADMIN
 */
public class RP_DangNhap {
    private Connection conn;
    
    public RP_DangNhap(){
        this.conn = Dbconnection.getConnection();
    }
    
  public NhanVienLoginInfo getLoginInfo(String soDienThoai, String matKhau) {
        String SQL = "SELECT ID, VaiTro, TrangThai FROM NhanVien WHERE SoDienThoai = ? AND MatKhau = ? AND TrangThai = 0";
        
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setString(1, soDienThoai);
            ps.setString(2, matKhau);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("ID");
                String vaiTro = rs.getString("VaiTro");
                return new NhanVienLoginInfo(id, vaiTro);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức kiểm tra xem nhân viên có nghỉ việc không
    public boolean isNhanVienNghiViec(String soDienThoai) {
        String SQL = "SELECT TrangThai FROM NhanVien WHERE SoDienThoai = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setString(1, soDienThoai);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int trangThai = rs.getInt("TrangThai");
                return trangThai == 1; // Trả về true nếu nhân viên đã nghỉ việc
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
