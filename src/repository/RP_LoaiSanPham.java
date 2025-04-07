/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;
import Utils.Dbconnection;
import java.sql.*;
import java.util.ArrayList;
import model.LoaiSanPham;
/**
 *
 * @author Dang
 */
public class RP_LoaiSanPham {
    private Connection conn;
    
    public RP_LoaiSanPham() {
        this.conn = Dbconnection.getConnection();
    }
    
    public ArrayList<LoaiSanPham> search() {
        String SQL = "SELECT * FROM LoaiSanPham WHERE TrangThai = 0";
        
        ArrayList<LoaiSanPham> ds = new ArrayList<>();
        
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery(); 
            
            while (rs.next()) {
                int id = rs.getInt("ID");
                String maLoaiSanPham = rs.getString("MaLoaiSanPham");
                String tenLoaiSanPham = rs.getString("TenLoaiSanPham");
                
                // Chuyển đổi Timestamp sang Date một cách an toàn
                Timestamp tsNgayTao = rs.getTimestamp("NgayTao");
                Date ngayTao = (tsNgayTao != null) ? new Date(tsNgayTao.getTime()) : null;
                
                Timestamp tsNgaySua = rs.getTimestamp("NgaySua");
                Date ngaySua = (tsNgaySua != null) ? new Date(tsNgaySua.getTime()) : null;
                
                int trangThai = rs.getInt("TrangThai");
                if (rs.wasNull()) {
                    trangThai = 0; // Giá trị mặc định nếu TrangThai là NULL
                }
                
                LoaiSanPham lsp = new LoaiSanPham(id, maLoaiSanPham, tenLoaiSanPham, ngayTao, ngaySua, trangThai);
                ds.add(lsp);
            }
            
            return ds;
            
        } catch (SQLException e) {
            // In thông tin lỗi nếu có
            System.out.println("Lỗi khi lấy dữ liệu từ ResultSet: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void create(LoaiSanPham lsp) {
    String SQL = "INSERT INTO LoaiSanPham(MaLoaiSanPham, TenLoaiSanPham) VALUES(?,?)";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, lsp.getMaLoaiSanPham());
        ps.setString(2, lsp.getTenLoaiSanPham());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi thêm LoaiSanPham: " + e.getMessage());
        e.printStackTrace();
    }
}

public void update(LoaiSanPham lsp) {
    String SQL = "UPDATE LoaiSanPham SET MaLoaiSanPham = ?, TenLoaiSanPham = ?, NgaySua = GETDATE() WHERE ID = ?";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, lsp.getMaLoaiSanPham());
        ps.setString(2, lsp.getTenLoaiSanPham());
        ps.setInt(3, lsp.getId());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi cập nhật LoaiSanPham: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    // Thêm phương thức kiểm tra mã trùng
    public boolean checkMaExists(String maLoaiSanPham) {
        String SQL = "SELECT COUNT(*) FROM LoaiSanPham WHERE MaLoaiSanPham = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setString(1, maLoaiSanPham);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu mã đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean checkTenExists(String tenLoaiSanPham) {
    String sql = "SELECT COUNT(*) FROM LoaiSanPham WHERE TenLoaiSanPham = ?";
    try (
        PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tenLoaiSanPham);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    public boolean checkTenExistsExcludingId(String tenLoaiSanPham, int id) {
    String sql = "SELECT COUNT(*) FROM LoaiSanPham WHERE TenLoaiSanPham = ? AND Id != ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tenLoaiSanPham);
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
    
    public String getTenLoaiSanPham(int idLoaiSanPham) {
        String sql = "SELECT TenLoaiSanPham FROM LoaiSanPham WHERE Id = ?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLoaiSanPham);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("TenLoaiSanPham");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Không xác định";
    }
    
    public boolean isUsedInSanPhamChiTiet(int idLoaiSanPham) {
    String sql = "SELECT COUNT(*) FROM SanPhamChiTiet WHERE IdLoaiSanPham = ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idLoaiSanPham);
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
    public boolean hide(int id) {
        String SQL = "UPDATE LoaiSanPham SET TrangThai = 1, NgaySua = GETDATE() WHERE ID = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi ẩn loại sản phẩm: " + e.getMessage());
        }
    }
    
    // Phương thức mới để lấy danh sách LoaiSanPham cho JComboBox
    public ArrayList<LoaiSanPham> getAllForComboBox() {
        String SQL = "SELECT ID, TenLoaiSanPham FROM LoaiSanPham WHERE TrangThai = 0";
        ArrayList<LoaiSanPham> ds = new ArrayList<>();
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String tenLoaiSanPham = rs.getString("TenLoaiSanPham");
                ds.add(new LoaiSanPham(id, null, tenLoaiSanPham, null, null, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    
    public ArrayList<Object[]> getAllHidden() {
    ArrayList<Object[]> list = new ArrayList<>();
    String sql = "SELECT ID, MaLoaiSanPham, TenLoaiSanPham FROM LoaiSanPham WHERE TrangThai = 1";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Object[]{
                rs.getInt("ID"),
                rs.getString("MaLoaiSanPham"),
                rs.getString("TenLoaiSanPham")
            });
        }
    } catch (SQLException e) {
        throw new RuntimeException("Lỗi khi lấy danh sách loại sản phẩm ẩn: " + e.getMessage(), e);
    }
    return list;
}

public boolean show(int id) {
    String sql = "UPDATE LoaiSanPham SET TrangThai = 0, NgaySua = GETDATE() WHERE ID = ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        throw new RuntimeException("Lỗi khi hiển thị lại loại sản phẩm: " + e.getMessage(), e);
    }
}
}
