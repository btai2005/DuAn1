/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;
import Utils.Dbconnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.SanPhamChiTiet;
/**
 *
 * @author Dang
 */
public class RP_SanPhamChiTiet {
    private Connection conn;
    
    public RP_SanPhamChiTiet() {
        this.conn = Dbconnection.getConnection();
    }
    
    public ArrayList<SanPhamChiTiet> search() {
        String SQL = "SELECT * FROM SanPhamChiTiet WHERE TrangThai = 0";
        
        ArrayList<SanPhamChiTiet> ds = new ArrayList<>();
        
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery(); // Sử dụng executeQuery() cho SELECT
            
            while (rs.next()) {
                int id = rs.getInt("ID");
                String maSanPhamChiTiet = rs.getString("MaSanPhamChiTiet");
                String tenSanPhamChiTiet = rs.getString("TenSanPhamChiTiet");
                BigDecimal giaSanPham = rs.getBigDecimal("GiaSanPham"); 
                String anhSanPham = rs.getString("AnhSanPham");
                int soLuong = rs.getInt("SoLuong");
                String moTa = rs.getString("MoTa"); // Có thể null
                int idChatLieu = rs.getInt("IdChatLieu");
                int idKichCo = rs.getInt("IdKichCo");
                int idMauSac = rs.getInt("IdMauSac");
                int idNSX = rs.getInt("IdNSX");
                int idLoaiSanPham = rs.getInt("IdLoaiSanPham");
                
                // Chuyển đổi Timestamp sang Date một cách an toàn
                Timestamp tsNgayTao = rs.getTimestamp("NgayTao");
                Date ngayTao = (tsNgayTao != null) ? new Date(tsNgayTao.getTime()) : null;
                
                Timestamp tsNgaySua = rs.getTimestamp("NgaySua");
                Date ngaySua = (tsNgaySua != null) ? new Date(tsNgaySua.getTime()) : null;
                
                int trangThai = rs.getInt("TrangThai");
                if (rs.wasNull()) {
                    trangThai = 0; // Giá trị mặc định nếu TrangThai là NULL
                }
                
                SanPhamChiTiet spct = new SanPhamChiTiet(id, maSanPhamChiTiet, tenSanPhamChiTiet, giaSanPham, anhSanPham, soLuong, moTa, idChatLieu, idKichCo, idMauSac, idNSX, idLoaiSanPham, trangThai, ngayTao, ngaySua);
                ds.add(spct);
            }
            
            return ds;
            
        } catch (SQLException e) {
            // In thông tin lỗi nếu có
            System.out.println("Lỗi khi lấy dữ liệu từ ResultSet: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void create(SanPhamChiTiet spct) {
    String SQL = "INSERT INTO SanPhamChiTiet(MaSanPhamChiTiet, TenSanPhamChiTiet, GiaSanPham, AnhSanPham, SoLuong, MoTa, IdChatLieu, IdKichCo, IdMauSac, IdNSX, IdLoaiSanPham) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, spct.getMaSanPhamChiTiet());
        ps.setString(2, spct.getTenSanPhamChiTiet());
        ps.setBigDecimal(3, spct.getGiaSanPham());
        ps.setString(4, spct.getAnhSanPham());
        ps.setInt(5, spct.getSoLuong());
        ps.setString(6, spct.getMoTa());
        ps.setInt(7, spct.getIdChatLieu());
        ps.setInt(8, spct.getIdKichCo());
        ps.setInt(9, spct.getIdMauSac());
        ps.setInt(10, spct.getIdNSX());
        ps.setInt(11, spct.getIdLoaiSanPham());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi thêm SanPhamChiTiet: " + e.getMessage());
        e.printStackTrace();
    }
}

public void update(SanPhamChiTiet spct) {
    String SQL = "UPDATE SanPhamChiTiet SET MaSanPhamChiTiet = ?, TenSanPhamChiTiet = ?, GiaSanPham = ?, AnhSanPham = ?, SoLuong = ?, MoTa = ?, IdChatLieu = ?, IdKichCo = ?, IdMauSac = ?, IdNSX = ?, IdLoaiSanPham = ?, NgaySua = GETDATE() WHERE ID = ?";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, spct.getMaSanPhamChiTiet());
        ps.setString(2, spct.getTenSanPhamChiTiet());
        ps.setBigDecimal(3, spct.getGiaSanPham());
        ps.setString(4, spct.getAnhSanPham());
        ps.setInt(5, spct.getSoLuong());
        ps.setString(6, spct.getMoTa());
        ps.setInt(7, spct.getIdChatLieu());
        ps.setInt(8, spct.getIdKichCo());
        ps.setInt(9, spct.getIdMauSac());
        ps.setInt(10, spct.getIdNSX());
        ps.setInt(11, spct.getIdLoaiSanPham());
        ps.setInt(12, spct.getId());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi cập nhật SanPhamChiTiet: " + e.getMessage());
        e.printStackTrace();
    }
}

    // Thêm phương thức kiểm tra mã trùng
    public boolean checkMaExists(String maSanPhamChiTiet) {
        String SQL = "SELECT COUNT(*) FROM SanPhamChiTiet WHERE MaSanPhamChiTiet = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setString(1, maSanPhamChiTiet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu mã đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean checkTenExistsExcludingId(String tenSanPham, int id) {
    String sql = "SELECT COUNT(*) FROM SanPhamChiTiet WHERE TenSanPhamChiTiet = ? AND Id != ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tenSanPham);
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
    
    public boolean checkTenExists(String tenSanPhamChiTiet) {
    String sql = "SELECT COUNT(*) FROM SanPhamChiTiet WHERE TenSanPhamChiTiet = ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tenSanPhamChiTiet);
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
        String SQL = "UPDATE SanPhamChiTiet SET TrangThai = 1, NgaySua = GETDATE() WHERE ID = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi ẩn sản phẩm chi tiết: " + e.getMessage());
        }
    }
    
    public List<SanPhamChiTiet> searchHidden() {
    List<SanPhamChiTiet> list = new ArrayList<>();
    String sql = """
        SELECT * FROM SanPhamChiTiet
        WHERE TrangThai = 1
        """;
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            SanPhamChiTiet spct = new SanPhamChiTiet();
            spct.setId(rs.getInt("Id"));
            spct.setMaSanPhamChiTiet(rs.getString("MaSanPhamChiTiet"));
            spct.setTenSanPhamChiTiet(rs.getString("TenSanPhamChiTiet"));
            spct.setGiaSanPham(rs.getBigDecimal("GiaSanPham"));
            spct.setAnhSanPham(rs.getString("AnhSanPham"));
            spct.setSoLuong(rs.getInt("SoLuong"));
            spct.setMoTa(rs.getString("MoTa"));
            spct.setIdChatLieu(rs.getInt("IdChatLieu"));
            spct.setIdKichCo(rs.getInt("IdKichCo"));
            spct.setIdMauSac(rs.getInt("IdMauSac"));
            spct.setIdNSX(rs.getInt("IdNSX"));
            spct.setIdLoaiSanPham(rs.getInt("IdLoaiSanPham"));
            spct.setTrangThai(rs.getInt("TrangThai"));
            list.add(spct);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
    
    public void show(int id) {
    String sql = "UPDATE SanPhamChiTiet SET TrangThai = 0 WHERE ID = ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException("Lỗi khi hiển thị lại sản phẩm chi tiết: " + e.getMessage());
    }
}
}
