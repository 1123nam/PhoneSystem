/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonesystem.edu.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import phonesystem.edu.entity.SanPham;
import phonesystem.edu.ultil.jdbcHelper;

/**
 *
 * @author NP
 */
// hhhhhhhh
public class SanPhamDAO extends PhoneSysDAO<SanPham, String> {

    String INSERT_SQL = "INSERT INTO SanPham(MaSanPham, TenSanPham, HangSanXuat, XuatXu, MauSac, SoLuong, DonGia, HinhAnh, TrangThai, GhiChu) values(?,?,?,?,?,?,?,?,?,?)";
    String UPDATE_SQL = "UPDATE SanPham set TenSanPham = ?, HangSanXuat = ?, XuatXu = ?, MauSac = ?, SoLuong = ?, DonGia = ?, HinhAnh = ?, TrangThai = 'True', GhiChu = ? WHERE MaSanPham = ?";
    String DELETE_SQL = "UPDATE SanPham set TenSanPham = ?, HangSanXuat = ?, XuatXu = ?, MauSac = ?, SoLuong = ?, DonGia = ?, HinhAnh = ?, TrangThai = 'False', GhiChu = ? WHERE MaSanPham = ?";
    String SELECT_ALL_SQL = "SELECT * FROM SanPham";
    String SELECT_BY_ID_SQL = "SELECT * FROM SanPham WHERE MaSanPham = ?";
    String SELECT_BY_HANG_SQL = "SELECT DISTINCT HangSanXuat FROM SanPham";
    String SELECT_BY_MAUSAC_SQL = "SELECT DISTINCT MauSac FROM SanPham";
    String SELECT_BY_XUATXU_SQL = "SELECT DISTINCT XuatXu FROM SanPham";
    String SELECT_LAST_ID = "select top 1 MaSanPham from SanPham order by MaSanPham desc";
    String RESTORE_SQL = "UPDATE SanPham SET TrangThai = ? WHERE MaSanPham = ?";
    String SELECT_SP_CHUACO_KHUYENMAI = "select * from SanPham where MaSanPham not in (select MaSanPham from KhuyenMai where TenKhuyenMai = ?)";
    String UPDATE_SOLUONG = "UPDATE SanPham set SoLuong = ? WHERE TenSanPham = ?";
    String UPDATE_TRANGTHAI = "UPDATE SanPham SET TrangThai = ? WHERE TenSanPham = ?";
    
    @Override
    public void insert(SanPham entity) {
        try {
            jdbcHelper.update(INSERT_SQL,
                    entity.getMaSanPham(), entity.getTenSanPham(), entity.getHangSanXuat(), entity.getXuatXu(), entity.getMauSac(),
                    entity.getSoLuong(), entity.getDonGia(), entity.getHinhAnh(), true, entity.getGhiChu()
            );
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(SanPham entity) {
        try {
            jdbcHelper.update(UPDATE_SQL,
                    entity.getTenSanPham(), entity.getHangSanXuat(), entity.getXuatXu(), entity.getMauSac(),
                    entity.getSoLuong(), entity.getDonGia(), entity.getHinhAnh(), entity.getGhiChu(), entity.getMaSanPham()
            );
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void update_SoLuong(String tenSp , int soluong) {
        try {
            jdbcHelper.update(UPDATE_SOLUONG,
                    soluong,tenSp
            );
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     public void update_trangThai(String tenSP) {
        try {
            jdbcHelper.update(UPDATE_TRANGTHAI, "false", tenSP);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(String key) {
        try {
            jdbcHelper.update(DELETE_SQL, key);
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete_SanPham(SanPham entity) {
        try {
            jdbcHelper.update(DELETE_SQL,
                    entity.getTenSanPham(), entity.getHangSanXuat(), entity.getXuatXu(), entity.getMauSac(),
                    entity.getSoLuong(), entity.getDonGia(), entity.getHinhAnh(), entity.getGhiChu(), entity.getMaSanPham()
            );
        } catch (Exception e) {
        }
    }

    public void restore(String id) {
        try {
            jdbcHelper.update(RESTORE_SQL, "True", id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public SanPham selectByid(String key) {
        List<SanPham> list = (List<SanPham>) selectBySql(SELECT_BY_ID_SQL, key);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<SanPham> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public List<SanPham> selectBySql(String sql, Object... args) {
        List<SanPham> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                SanPham entity = new SanPham();
                entity.setMaSanPham(rs.getString("MaSanPham"));
                entity.setTenSanPham(rs.getString("TenSanPham"));
                entity.setHangSanXuat(rs.getString("HangSanXuat"));
                entity.setXuatXu(rs.getString("XuatXu"));
                entity.setMauSac(rs.getString("MauSac"));
                entity.setSoLuong(rs.getInt("SoLuong"));
                entity.setDonGia(rs.getFloat("DonGia"));
                entity.setHinhAnh(rs.getString("HinhAnh"));
                entity.setTrangThai(rs.getBoolean("TrangThai"));
                entity.setGhiChu(rs.getString("GhiChu"));

                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> selectByHang() {
        List<Object> list_Hang = new ArrayList<Object>();
        try {
            ResultSet rs = jdbcHelper.query(SELECT_BY_HANG_SQL);
            while (rs.next()) {
                list_Hang.add(rs.getObject(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_Hang;
    }

    public List<Object> selectByMauSac() {
        List<Object> list_Mau = new ArrayList<Object>();
        try {
            ResultSet rs = jdbcHelper.query(SELECT_BY_MAUSAC_SQL);
            while (rs.next()) {
                list_Mau.add(rs.getObject(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_Mau;
    }

    public List<Object> selectByXuatXu() {
        List<Object> list_XuatXu = new ArrayList<Object>();
        try {
            ResultSet rs = jdbcHelper.query(SELECT_BY_XUATXU_SQL);
            while (rs.next()) {
                list_XuatXu.add(rs.getObject(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list_XuatXu;
    }

    String SELECT_BY_NAME_PRODUCT = "Select * from SanPham where TenSanPham = ?";

    public List<SanPham> select_All_TheoTenSP(String tenSP) {
        return this.selectBySql(SELECT_BY_NAME_PRODUCT, tenSP);
    }

    public String getID_SanPham() {
        String id = "";

        try {
            ResultSet rs = jdbcHelper.query(SELECT_LAST_ID);
            while (rs.next()) {
                id = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public List<SanPham> selectSanPhamChuaCoKhuyenMai(String tenKM) {
       return this.selectBySql(SELECT_SP_CHUACO_KHUYENMAI,tenKM);
    }
    
    public static void main(String[] args) {
        SanPhamDAO sp = new SanPhamDAO();
        List<SanPham> l = null;
      //  l = sp.select_All_TheoTenSP("SamSung J4");
        l = sp.selectSanPhamChuaCoKhuyenMai("Noel an lành");
        System.out.println(l);
    }
}
