/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import Utils.Dbconnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.KhachHang;

/**
 *
 * @author trankhanhmobi
 */
public class RP_KhachHang {

    private Connection conn;

    public RP_KhachHang() {
        this.conn = Dbconnection.getConnection();
    }
    public List<KhachHang> getAll() {
        List<KhachHang> listKH = new ArrayList<>();
        String sql = "SELECT * FROM dbo.KHACHHANG WHERE TrangThai = 0";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5),
                        rs.getDate(6),
                        rs.getInt(7)
                );
                listKH.add(kh);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listKH;

    }

    public Integer checkSdt(String sdt, Integer id) {
        String sql;
        if (id != null) {
            sql = "SELECT Count(Id) FROM dbo.KHACHHANG WHERE SoDienThoai = ? And ID != ?";
        } else {
            sql = "SELECT Count(Id) FROM dbo.KHACHHANG WHERE SoDienThoai = ? ";
        }
        Integer result = 0;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sdt);
            if (id != null) {
                ps.setInt(2, id);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception e) {
        }
        return result;
    }

  public boolean add(KhachHang kh) {
        String sql = "INSERT INTO dbo.KHACHHANG(MaKhachHang, HoTen, SoDienThoai) VALUES(?, ?, ?)";
        // SỬA: Dùng try-with-resources và kiểm tra kết quả thực thi
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getSoDienThoai());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   public boolean update(KhachHang kh) {
        String sql = "UPDATE dbo.KHACHHANG SET MaKhachHang = ?, HoTen = ?, SoDienThoai = ? WHERE Id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getSoDienThoai());
            ps.setInt(4, kh.getId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // SỬA: Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // SỬA: Trả về false nếu có lỗi
        }
    }
   
   // THÊM: Phương thức kiểm tra mã khách hàng trùng
    public Integer checkMaKhachHang(String maKhachHang, Integer id) {
        String sql = id != null ?
                "SELECT COUNT(Id) FROM dbo.KHACHHANG WHERE MaKhachHang = ? AND ID != ?" :
                "SELECT COUNT(Id) FROM dbo.KHACHHANG WHERE MaKhachHang = ?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhachHang);
            if (id != null) {
                ps.setInt(2, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // THÊM: Phương thức ẩn khách hàng (đặt TrangThai = 1)
    public boolean hideKhachHang(int id) {
        String sql = "UPDATE dbo.KHACHHANG SET TrangThai = 1 WHERE Id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<KhachHang> getAllHidden() {
        List<KhachHang> listKH = new ArrayList<>();
        String sql = "SELECT * FROM dbo.KHACHHANG WHERE TrangThai = 1";
        try (
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5),
                        rs.getDate(6),
                        rs.getInt(7)
                );
                listKH.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listKH;
    }
    
    public boolean showKhachHang(int id) {
        String sql = "UPDATE dbo.KHACHHANG SET TrangThai = 0 WHERE Id = ?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
