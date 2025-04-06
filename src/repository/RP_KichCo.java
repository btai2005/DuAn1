/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import Utils.Dbconnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import model.KichCo;

/**
 *
 * @author ADMIN
 */
public class RP_KichCo {
    private Connection conn;
    
    public RP_KichCo() {
        this.conn = Dbconnection.getConnection();
    }
    
    public ArrayList<KichCo> search() {
        String SQL = "SELECT * FROM KichCo WHERE TrangThai = 0";
        
        ArrayList<KichCo> ds = new ArrayList<>();
        
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery(); 
            while (rs.next()) {         
                int id = rs.getInt("ID");
                String maKichCo = rs.getString("MaKichCo");
                BigDecimal size = rs.getBigDecimal("Size"); 
                
                // Chuyển đổi Timestamp sang Date một cách an toàn
                Timestamp tsNgayTao = rs.getTimestamp("NgayTao");
                Date ngayTao = (tsNgayTao != null) ? new Date(tsNgayTao.getTime()) : null;
                
                Timestamp tsNgaySua = rs.getTimestamp("NgaySua");
                Date ngaySua = (tsNgaySua != null) ? new Date(tsNgaySua.getTime()) : null;
                
                int trangThai = rs.getInt("TrangThai");
                if (rs.wasNull()) {
                    trangThai = 0; // Giá trị mặc định nếu TrangThai là NULL
                }
                
                KichCo kc = new KichCo(id, maKichCo, size, ngayTao, ngaySua, trangThai);
                ds.add(kc);
            }           
            return ds;
            
        } catch (SQLException e) {
            // In thông tin lỗi nếu có
            System.out.println("Lỗi khi lấy dữ liệu từ ResultSet: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void create(KichCo kc) {
    String SQL = "INSERT INTO KichCo(MaKichCo, Size) VALUES(?,?)";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, kc.getMaKichCo());
        ps.setBigDecimal(2, kc.getSize());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi thêm KichCo: " + e.getMessage());
        e.printStackTrace();
    }
}

public void update(KichCo kc) {
    String SQL = "UPDATE KichCo SET MaKichCo = ?, Size = ?, NgaySua = GETDATE() WHERE ID = ?";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, kc.getMaKichCo());
        ps.setBigDecimal(2, kc.getSize());
        ps.setInt(3, kc.getId());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi cập nhật KichCo: " + e.getMessage());
        e.printStackTrace();
    }
}

    // Thêm phương thức kiểm tra mã trùng
    public boolean checkMaExists(String maKichCo) {
        String SQL = "SELECT COUNT(*) FROM KichCo WHERE MaKichCo = ? ";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setString(1, maKichCo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu mã đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean checkTenExists(String size) {
    String sql = "SELECT COUNT(*) FROM KichCo WHERE Size = ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, size);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    public boolean checkTenExistsExcludingId(String size, int id) {
    String sql = "SELECT COUNT(*) FROM KichCo WHERE Size = ? AND Id != ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, size);
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
        String SQL = "UPDATE KichCo SET TrangThai = 1, NgaySua = GETDATE() WHERE ID = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi ẩn kích cỡ: " + e.getMessage());
        }
    }
    
    // Phương thức mới để lấy danh sách KichCo cho JComboBox
    public ArrayList<KichCo> getAllForComboBox() {
        String SQL = "SELECT ID, Size FROM KichCo WHERE TrangThai = 0";
        ArrayList<KichCo> ds = new ArrayList<>();
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                BigDecimal size = rs.getBigDecimal("Size");
                ds.add(new KichCo(id, null, size, null, null, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}
