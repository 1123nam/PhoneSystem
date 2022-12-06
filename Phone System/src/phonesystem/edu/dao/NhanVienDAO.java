package phonesystem.edu.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import phonesystem.edu.entity.NhanVien;
import phonesystem.edu.ultil.jdbcHelper;

/**
 *
 * @author LAPTOP LENOVO
 */
public class NhanVienDAO extends PhoneSysDAO<NhanVien, String> {

    private String INSERT_SQL = "INSERT INTO NHANVIEN VALUES(?,?,?,?,?,?,?,?,?,?,?) ";
    private String UPDATE_SQL = "UPDATE NHANVIEN SET TENNHANVIEN = ?, NGAYSINH = ?, GIOITINH = ?, CCCD = ?, SDT = ?, DIACHI = ?, EMAIL = ?, TRANGTHAI = ?, HINHANH = ?, GHICHU = ? WHERE MANHANVIEN = ?";
    private String DELETE_SQL = "DELETE NhanVien WHERE MaNhanVien = ?";
    private String SELECT_ALL_SQL = "SELECT * FROM NHANVIEN";
    private String SELECT_BY_ID_SQL = "SELECT * FROM NHANVIEN WHERE MANHANVIEN = ?";
    private String SELECT_BY_EMAIL = "SELECT EMAIL FROM NHANVIEN WHERE EMAIL = ?";
    private String SELECT_SDT = "SELECT SDT FROM NHANVIEN where sdt = ? ";

    @Override
    public void insert(NhanVien entity) {

        try {
            jdbcHelper.update(INSERT_SQL, entity.getMaNhanVien(), entity.getTenNhanVien(), entity.getNgaySinh(), entity.getGioiTinh(), entity.getCCCD(),
                    entity.getSDT(), entity.getDiaChi(), entity.getEmail(), entity.getTrangThai(), entity.getHinhAnh(), entity.getGhiChu());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(NhanVien nhanVien) {

        try {
            jdbcHelper.update(UPDATE_SQL, nhanVien.getTenNhanVien(), nhanVien.getNgaySinh(), nhanVien.getGioiTinh(), nhanVien.getCCCD(),
                    nhanVien.getSDT(), nhanVien.getDiaChi(), nhanVien.getEmail(), nhanVien.getTrangThai(), nhanVien.getHinhAnh(), nhanVien.getGhiChu(), nhanVien.getMaNhanVien());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<NhanVien> selectAll() {

        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public NhanVien selectByid(String id) {

        List<NhanVien> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public boolean kiemTraTrungEmail(String Email) throws SQLException {

        ResultSet rs = jdbcHelper.query(SELECT_BY_EMAIL, Email);
        while (rs.next() == true) {
            return true;
        }
        return false;
    }

    @Override
    public List<NhanVien> selectBySql(String sql, Object... args) {

        List<NhanVien> list = new ArrayList<NhanVien>();

        try {
            ResultSet rs = jdbcHelper.query(sql, args);

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("MaNhanVien"));
                nv.setTenNhanVien(rs.getString("TenNhanVien"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setGioiTinh(rs.getBoolean("GioiTinh"));
                nv.setCCCD(rs.getString("cccd"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setSDT(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setTrangThai(rs.getBoolean("TrangThai"));
                nv.setHinhAnh(rs.getString("HinhAnh"));
                nv.setGhiChu(rs.getString("GhiChu"));
                list.add(nv);
            }
            rs.getStatement().getConnection().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    String SELECT_NOTIN_TAIKHOAN = "SELECT * FROM NhanVien where MaNhanVien NOT IN (SELECT MaNhanVien FROM TaiKhoan)";

    public List<NhanVien> SelectNotInTaiKhoan() {
        return this.selectBySql(SELECT_NOTIN_TAIKHOAN);
    }

    private String SELECT_LAST_ID = "select top 1 MaNhanVien from NhanVien order by MaNhanVien  desc";

    public String getID_NhanVien() {
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

    public boolean isNumPhoneDup(String sdt) {

        ResultSet rs = null;
        try {
            rs = jdbcHelper.query(SELECT_SDT, sdt);
            while (rs.next() == true) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        NhanVienDAO dao = new NhanVienDAO();
        System.out.println(dao.isNumPhoneDup("dhfskf"));
        
    }
//    public static void main(String[] args) {
//        NhanVienDAO dao = new NhanVienDAO();
//        Date date = new Date(2000, 01, 12);
//        dao.insert(new NhanVien("NV05", "Nguyen Van Teo", date, true, "334522123332", "Vĩnh Long", "0324233444", "teonv@gmail.com", true, "D:/anh", "null"));
//
//    }
}
