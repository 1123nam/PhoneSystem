package phonesystem.edu.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import phonesystem.edu.entity.Luong;
import phonesystem.edu.ultil.jdbcHelper;

/**
 *
 * @author LAPTOP LENOVO
 */
public class LuongDAO extends PhoneSysDAO<Luong, String> {

    String SELECT_ALL_SQL = "{CALL getAllLuong}";
    String INSERT_SQL = "INSERT INTO LUONG (MANHANVIEN, LUONGTRENCA, TONGCALAM,TIENTHUONG, NGAYNHAN,TRANGTHAI,GHICHU) VALUES(?,?,?,?,?,?,?)";
    String UPDATE_SQL = "UPDATE LUONG  SET MANHANVIEN = ?, LUONGTRENCA = ? , TONGCALAM = ?,TIENTHUONG  = ?, NGAYNHAN  = ?,TRANGTHAI  = ?,GHICHU  = ? WHERE  MALUONG = ?";

    String SELECT_LUONG_BY_ID_NHANVIEN = "{CALL getLuongByMaNhanVien(?)}";

    @Override
    public void insert(Luong entity) {
        try {
            jdbcHelper.update(INSERT_SQL, entity.getMaNhanVien(), entity.getLuongTrenCa(), entity.getTongCaLam(), entity.getTienThuong(), entity.getNgayNhan(), entity.getTrangThai(), entity.getGhiChu());
        } catch (SQLException ex) {
            Logger.getLogger(LuongDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Luong entity) {
  try {
            jdbcHelper.update(UPDATE_SQL, entity.getMaNhanVien(), entity.getLuongTrenCa(), entity.getTongCaLam(), entity.getTienThuong(), entity.getNgayNhan(), entity.getTrangThai(), entity.getGhiChu(), entity.getMaLuong());
        } catch (SQLException ex) {
            Logger.getLogger(LuongDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }    }

    @Override
    public void delete(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Luong> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    Luong selectByid(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Luong> selectBySql(String sql, Object... args) {
        List<Luong> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                Luong entity = new Luong();
                entity.setMaLuong(rs.getString("MaLuong"));
                entity.setMaNhanVien(rs.getString("MaNhanVien"));
                entity.setLuongTrenCa(rs.getInt("LuongTrenCa"));
                entity.setTongCaLam(rs.getInt("TongCaLam"));
                entity.setTienThuong(rs.getFloat("TienThuong"));
                entity.setNgayNhan(rs.getDate("NgayNhan"));
                entity.setTrangThai(rs.getString("TrangThai"));
                entity.setGhiChu(rs.getString("GhiChu"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        LuongDAO dao = new LuongDAO();
//        List<Luong> ls = dao.selectAll();
//        for (Luong l : ls) {
//            System.out.println(l);
//        }

//test lay thong tin luong theo ma nhan vien
        List<Object[]> l = dao.getAllLuong();
        for (Object[] o : l) {
            System.out.println(o[0]);
            System.out.println(o[1]);
            System.out.println(o[2]);
        }

    }

    public List<Object[]> getLuong(String idNhanVien) {
        String[] cols = {"TenNhanVien", "Quyen", "Tổng ca"};
        return this.getListOfArray(SELECT_LUONG_BY_ID_NHANVIEN, cols, idNhanVien);
    }

    public List<Object[]> getAllLuong() {
        String[] cols = {"MaLuong", "MaNhanVien", "TenNhanVien", "TongCaLam", "LuongTrenCa", "TienThuong", "Tong luong", "NgayNhan", "TrangThai", "GhiChu"};
        return this.getListOfArray(SELECT_ALL_SQL, cols);
    }

    public List<Object[]> getListOfArray(String sql, String[] cols, Object... agrs) {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.query(sql, agrs);
            while (rs.next()) {
                Object[] vals = new Object[cols.length];
                for (int i = 0; i < cols.length; i++) {
                    vals[i] = rs.getObject(cols[i]);
                }
                list.add(vals);
            }
            rs.getStatement().getConnection().close();
            return list;

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

}
