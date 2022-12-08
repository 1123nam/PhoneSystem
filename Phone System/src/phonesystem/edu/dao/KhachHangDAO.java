package phonesystem.edu.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import phonesystem.edu.entity.KhachHang;
import phonesystem.edu.ultil.jdbcHelper;

/**
 *
 * @author LAPTOP LENOVO
 */
public class KhachHangDAO extends PhoneSysDAO<KhachHang, String> {

    private String INSERT_SQL = "INSERT INTO KHACHHANG VALUES(?,?,?,?,?,?)";
    private String UPDATE_SQL = "UPDATE KHACHHANG SET TENKHACHHANG = ?, GIOITINH = ?, SDT = ?,  TRANGTHAI = ?,GHICHU = ? WHERE MAKHACHHANG = ?";
    private String RESTORE_SQL = "UPDATE KHACHHANG SET TRANGTHAI = ? WHERE MAKHACHHANG = ?";

    private String SELECT_ALL_SQL = "SELECT * FROM KHACHHANG";
    private String SELECT_BY_ID_SQL = "SELECT * FROM KHACHHANG WHERE MAKHACHHANG = ?";
    private String SELECT_LAST_ID = "select top 1 MaKhachHang from KhachHang order by MaKhachHang  desc";
    private String SELECT_SDT = "SELECT * FROM KHACHhANG WHERE SDT = ? ";

    @Override
    public void insert(KhachHang entity) {
        try {
            jdbcHelper.update(INSERT_SQL, entity.getMaKhachHang(), entity.getTenKhachHang(), entity.isGioiTinh(), entity.getSDT(), entity.isTrangThai(), entity.getGhiChu());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(KhachHang entity) {
        try {
            jdbcHelper.update(UPDATE_SQL, entity.getTenKhachHang(), entity.isGioiTinh(), entity.getSDT(), entity.isTrangThai(), entity.getGhiChu(), entity.getMaKhachHang());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void restore(String id) {
        try {
            jdbcHelper.update(RESTORE_SQL, "true", id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(String key) {
    }

    @Override
    public List<KhachHang> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    KhachHang selectByid(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<KhachHang> selectBySql(String sql, Object... args) {
        List<KhachHang> list = new ArrayList<KhachHang>();

        try {
            ResultSet rs = jdbcHelper.query(sql, args);

            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKhachHang(rs.getString("MaKhachHang"));
                kh.setTenKhachHang(rs.getString("TenKhachHang"));
                kh.setGioiTinh(rs.getBoolean("GioiTinh"));
                kh.setSDT(rs.getString("SDT"));
                kh.setTrangThai(rs.getBoolean("TrangThai"));
                kh.setGhiChu(rs.getString("GhiChu"));
                list.add(kh);
            }
            rs.getStatement().getConnection().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getID_KhachHang() {
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
    
    
      public KhachHang getKhachHangBySDT(String sdt) {
        try {
            ResultSet rs = jdbcHelper.query(SELECT_SDT,sdt);
            while (rs.next()) {
                KhachHang kh = new KhachHang(rs.getString(1), rs.getString(2), rs.getBoolean(3), rs.getString(4), rs.getBoolean(5), rs.getString(6));
                return kh;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
 public static void main(String[] args) {
        KhachHangDAO dao = new KhachHangDAO();
        System.out.println(dao.getID_KhachHang());
    }
}
