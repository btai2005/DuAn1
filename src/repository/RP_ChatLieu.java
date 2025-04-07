/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;
import Utils.Dbconnection;
import java.sql.*;
import java.util.ArrayList;
import model.ChatLieu;
/**
 *
 * @author Dang
 */
public class RP_ChatLieu {
    private Connection conn;
    
    public RP_ChatLieu(){
        this.conn = Dbconnection.getConnection();
    }
    
    public ArrayList<ChatLieu> search(){
        String SQL = "SELECT * FROM ChatLieu WHERE TrangThai = 0";
        
        ArrayList<ChatLieu> ds = new ArrayList<>();
        
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while(rs.next()){
                int id = rs.getInt("ID");
                String maChatLieu = rs.getString("MaChatLieu");
                String tenChatLieu = rs.getString("TenChatLieu");
                
                Timestamp tsNgayTao = rs.getTimestamp("NgayTao");
                Date ngayTao = (tsNgayTao != null) ? new Date(tsNgayTao.getTime()) : null;
                
                Timestamp tsNgaySua = rs.getTimestamp("NgaySua");
                Date ngaySua = (tsNgaySua != null) ? new Date(tsNgaySua.getTime()) : null;
                
                int trangThai = rs.getInt("TrangThai");
                if (rs.wasNull()) {
                    trangThai = 0; // Giá trị mặc định nếu TrangThai là NULL
                }
                ds.add(new ChatLieu(id,maChatLieu, tenChatLieu, ngayTao, ngaySua, trangThai));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
    
    public void create(ChatLieu cl) {
    String SQL = "INSERT INTO ChatLieu(MaChatLieu, TenChatLieu) VALUES(?,?)";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, cl.getMaChatLieu());
        ps.setString(2, cl.getTenChatLieu());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi thêm ChatLieu: " + e.getMessage());
        e.printStackTrace();
    }
}

public void update(ChatLieu cl) {
    String SQL = "UPDATE ChatLieu SET MaChatLieu = ?, TenChatLieu = ?, NgaySua = GETDATE() WHERE ID = ?";
    try {
        PreparedStatement ps = this.conn.prepareStatement(SQL);
        ps.setString(1, cl.getMaChatLieu());
        ps.setString(2, cl.getTenChatLieu());
        ps.setInt(3, cl.getId());
        ps.execute();
    } catch (SQLException e) {
        System.out.println("Lỗi khi cập nhật ChatLieu: " + e.getMessage());
        e.printStackTrace();
    }
}

    // Thêm phương thức kiểm tra mã trùng
    public boolean checkMaExists(String maChatLieu) {
        String SQL = "SELECT COUNT(*) FROM ChatLieu WHERE MaChatLieu = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setString(1, maChatLieu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu mã đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean checkTenExists(String tenChatLieu) {
    String sql = "SELECT COUNT(*) FROM ChatLieu WHERE TenChatLieu = ?";
    try (
         PreparedStatement ps = this.conn.prepareStatement(sql)) {
        ps.setString(1, tenChatLieu);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0; // Trả về true nếu tên đã tồn tại
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    public boolean checkTenExistsExcludingId(String tenChatLieu, int id) {
    String sql = "SELECT COUNT(*) FROM ChatLieu WHERE TenChatLieu = ? AND Id != ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tenChatLieu);
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
    
    public String getTenChatLieu(int idChatLieu) {
        String sql = "SELECT TenChatLieu FROM ChatLieu WHERE ID = ?";
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idChatLieu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("TenChatLieu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Không xác định";
    }
    
    public boolean isUsedInSanPhamChiTiet(int idChatLieu) {
    String sql = "SELECT COUNT(*) FROM SanPhamChiTiet WHERE IdChatLieu = ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idChatLieu);
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
        String SQL = "UPDATE ChatLieu SET TrangThai = 1, NgaySua = GETDATE() WHERE ID = ?";
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi ẩn chất liệu: " + e.getMessage());
        }
    }
    
    // Phương thức mới để lấy danh sách ChatLieu cho JComboBox
    public ArrayList<ChatLieu> getAllForComboBox() {
        String SQL = "SELECT ID, TenChatLieu FROM ChatLieu WHERE TrangThai = 0";
        ArrayList<ChatLieu> ds = new ArrayList<>();
        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String tenChatLieu = rs.getString("TenChatLieu");
                ds.add(new ChatLieu(id, null, tenChatLieu, null, null, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    
    public ArrayList<Object[]> getAllHidden() {
    ArrayList<Object[]> list = new ArrayList<>();
    String sql = "SELECT ID, MaChatLieu, TenChatLieu FROM ChatLieu WHERE TrangThai = 1";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Object[]{
                rs.getInt("ID"),
                rs.getString("MaChatLieu"),
                rs.getString("TenChatLieu")
            });
        }
    } catch (SQLException e) {
        throw new RuntimeException("Lỗi khi lấy danh sách chất liệu ẩn: " + e.getMessage(), e);
    }
    return list;
}

public boolean show(int id) {
    String sql = "UPDATE ChatLieu SET TrangThai = 0, NgaySua = GETDATE() WHERE ID = ?";
    try (
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        throw new RuntimeException("Lỗi khi hiển thị lại chất liệu: " + e.getMessage(), e);
    }
}
}
