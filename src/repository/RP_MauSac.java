/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import Utils.Dbconnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import model.MauSac;

/**
 *
 * @author ADMIN
 */
public class RP_MauSac {
    private Connection conn;
    
    public RP_MauSac() {
        this.conn = Dbconnection.getConnection();
    }
    
    public ArrayList<MauSac> search() {
        String SQL = "SELECT * FROM MauSac WHERE TrangThai = 0";
        
        ArrayList<MauSac> ds = new ArrayList<>();
        
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery(); 
            
            while (rs.next()) {
                int id = rs.getInt("ID");
                String maMauSac = rs.getString("MaMauSac");
                String tenMau = rs.getString("TenMau");
                
                // Chuyển đổi Timestamp sang Date một cách an toàn
                Timestamp tsNgayTao = rs.getTimestamp("NgayTao");
                Date ngayTao = (tsNgayTao != null) ? new Date(tsNgayTao.getTime()) : null;
                
                Timestamp tsNgaySua = rs.getTimestamp("NgaySua");
                Date ngaySua = (tsNgaySua != null) ? new Date(tsNgaySua.getTime()) : null;
                
                int trangThai = rs.getInt("TrangThai");
                if (rs.wasNull()) {
                    trangThai = 0; // Giá trị mặc định nếu TrangThai là NULL
                }
                
                MauSac ms = new MauSac(id, maMauSac, tenMau, ngayTao, ngaySua, trangThai);
                ds.add(ms);
            }
            
            return ds;
            
        } catch (SQLException e) {
            // In thông tin lỗi nếu có
            System.out.println("Lỗi khi lấy dữ liệu từ ResultSet: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void create(MauSac ms) {
    String SQL = "INSERT INTO MauSac(MaMauSac, TenMau) VALUES(?,?)";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, ms.getMaMauSac());
        ps.setString(2, ms.getTenMau());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi thêm MauSac: " + e.getMessage());
        e.printStackTrace();
    }
}

public void update(MauSac ms) {
    String SQL = "UPDATE MauSac SET MaMauSac = ?, TenMau = ?, NgaySua = GETDATE() WHERE ID = ?";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, ms.getMaMauSac());
        ps.setString(2, ms.getTenMau());
        ps.setInt(3, ms.getId());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi cập nhật MauSac: " + e.getMessage());
        e.printStackTrace();
    }
}

    // Thêm phương thức kiểm tra mã trùng
    public boolean checkMaExists(String maMauSac) {
        String SQL = "SELECT COUNT(*) FROM MauSac WHERE MaMauSac = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setString(1, maMauSac);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu mã đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean checkTenExists(String tenMau) {
    String sql = "SELECT COUNT(*) FROM MauSac WHERE TenMau = ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tenMau);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    public boolean checkTenExistsExcludingId(String tenMau, int id) {
    String sql = "SELECT COUNT(*) FROM MauSac WHERE TenMau = ? AND Id != ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tenMau);
        ps.setInt(2, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    // Thêm phương thức hide
    public void hide(int id) {
        String SQL = "UPDATE MauSac SET TrangThai = 1, NgaySua = GETDATE() WHERE ID = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi ẩn màu sắc: " + e.getMessage());
        }
    }
    
    // Phương thức mới để lấy danh sách MauSac cho JComboBox
    public ArrayList<MauSac> getAllForComboBox() {
        String SQL = "SELECT ID, TenMau FROM MauSac WHERE TrangThai = 0";
        ArrayList<MauSac> ds = new ArrayList<>();
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String tenMau = rs.getString("TenMau");
                ds.add(new MauSac(id, null, tenMau, null, null, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}
