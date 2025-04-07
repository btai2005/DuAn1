/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import Utils.Dbconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.SanPhamChiTiet;

/**
 *
 * @author ADMIN
 */
public class SanPhamChiTietRepo {
    public List<SanPhamChiTiet> getdata(){
        List<SanPhamChiTiet> list= new ArrayList<>();
        String query ="Select AnhSanPham,MaSanPhamChiTiet,TenSanPhamChiTiet,GiaSanPham,SoLuong from SanPhamChiTiet";
        try(Connection con = Dbconnection.getConnection();PreparedStatement ps = con.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                SanPhamChiTiet cd = new SanPhamChiTiet();
                cd.setAnhSanPham(rs.getString(1));
                cd.setMaSanPhamChiTiet(rs.getString(2));
                cd.setTenSanPhamChiTiet(rs.getString(3));
                cd.setGiaSanPham(rs.getBigDecimal(4));
                cd.setSoLuong(rs.getInt(5));
                list.add(cd);
            }
            return list;
        } catch (Exception e) {
        }
        return null;
    }
}
