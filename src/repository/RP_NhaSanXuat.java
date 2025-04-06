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
import model.NhaSanXuat;

/**
 *
 * @author ADMIN
 */
public class RP_NhaSanXuat {
    private Connection conn;
    
    public RP_NhaSanXuat() {
        this.conn = Dbconnection.getConnection();
    }
    
    public ArrayList<NhaSanXuat> search() {
        String SQL = "SELECT * FROM NhaSanXuat WHERE TrangThai = 0";
        
        ArrayList<NhaSanXuat> ds = new ArrayList<>();
        
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                // Lấy dữ liệu từ các cột
                int id = rs.getInt("ID");
                String maNSX = rs.getString("MaNSX");
                String tenNSX = rs.getString("TenNSX");
                
                // Chuyển đổi Timestamp sang Date một cách an toàn
                Timestamp tsNgayTao = rs.getTimestamp("NgayTao");
                Date ngayTao = (tsNgayTao != null) ? new Date(tsNgayTao.getTime()) : null;
                
                Timestamp tsNgaySua = rs.getTimestamp("NgaySua");
                Date ngaySua = (tsNgaySua != null) ? new Date(tsNgaySua.getTime()) : null;
                
                int trangThai = rs.getInt("TrangThai");
                if (rs.wasNull()) {
                    trangThai = 0; // Giá trị mặc định nếu TrangThai là NULL
                }
                
                NhaSanXuat nsx = new NhaSanXuat(id, maNSX, tenNSX, ngayTao, ngaySua, trangThai);
                ds.add(nsx);
            }
            
            return ds;
            
        } catch (SQLException e) {
            // In thông tin lỗi nếu có
            System.out.println("Lỗi khi lấy dữ liệu từ ResultSet: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void create(NhaSanXuat nsx) {
    String SQL = "INSERT INTO NhaSanXuat(MaNSX, TenNSX) VALUES(?,?)";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, nsx.getMaNSX());
        ps.setString(2, nsx.getTenNSX());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi thêm NhaSanXuat: " + e.getMessage());
        e.printStackTrace();
    }
}

public void update(NhaSanXuat nsx) {
    String SQL = "UPDATE NhaSanXuat SET MaNSX = ?, TenNSX = ?, NgaySua = GETDATE() WHERE ID = ?";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, nsx.getMaNSX());
        ps.setString(2, nsx.getTenNSX());
        ps.setInt(3, nsx.getId());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi cập nhật NhaSanXuat: " + e.getMessage());
        e.printStackTrace();
    }
}

    // Thêm phương thức kiểm tra mã trùng
    public boolean checkMaExists(String maNhaSanXuat) {
        String SQL = "SELECT COUNT(*) FROM NhaSanXuat WHERE MaNSX = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setString(1, maNhaSanXuat);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu mã đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean checkTenExists(String tenNSX) {
    String sql = "SELECT COUNT(*) FROM NhaSanXuat WHERE TenNSX = ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tenNSX);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    public boolean checkTenExistsExcludingId(String tenNSX, int id) {
    String sql = "SELECT COUNT(*) FROM NhaSanXuat WHERE TenNSX = ? AND Id != ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tenNSX);
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
        String SQL = "UPDATE NhaSanXuat SET TrangThai = 1, NgaySua = GETDATE() WHERE ID = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi ẩn nhà sản xuất: " + e.getMessage());
        }
    }
    
    // Phương thức mới để lấy danh sách NhaSanXuat cho JComboBox
    public ArrayList<NhaSanXuat> getAllForComboBox() {
        String SQL = "SELECT ID, TenNSX FROM NhaSanXuat WHERE TrangThai = 0";
        ArrayList<NhaSanXuat> ds = new ArrayList<>();
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String tenNSX = rs.getString("TenNSX");
                ds.add(new NhaSanXuat(id, null, tenNSX, null, null, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}
