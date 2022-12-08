package phonesystem.edu.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import phonesystem.edu.ultil.jdbcHelper;

/**
 *
 * @author LAPTOP LENOVO
 */
public class ThongKeDAO {

    private List<Object[]> getListOfArray(String sql, String[] cols, Object... args) {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                Object[] vals = new Object[cols.length];
                for (int i = 0; i < cols.length; i++) {
                    vals[i] = rs.getObject(cols[i]);
                }
                list.add(vals);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getDoanhThuTheoNgayHienTai(Date ngayHienTai) {

        String sql = "{CALL thongKeTheoNgayHienTai(?)}";
        String[] cols = {"Tong Tien", "Tong SL"};

        return this.getListOfArray(sql, cols, ngayHienTai);
    }

    public List<Object[]> getDoanhThuTheoThangHienTai(Date thangHienTai) {

        String sql = "{CALL thongKeTheoThangHienTai(?)}";
        String[] cols = {"Tong Tien", "Tong SL"};

        return this.getListOfArray(sql, cols, thangHienTai);
    }

    public List<Object[]> getDoanhThuTheoNamHienTai(Date namHienTai) {

        String sql = "{CALL thongKeTheoNamHienTai(?)}";
        String[] cols = {"Tong Tien", "Tong SL"};

        return this.getListOfArray(sql, cols, namHienTai);
    }

    public List<Object[]> getTongDoanhThuTheoNgay(Date ngayBatDau, Date ngayKetThuc) {

        String sql = "{CALL getTongDoanhThuTheoNgay(?, ?)}";
        String[] cols = {"TenSanPham", "TongDoanhThu"};

        return this.getListOfArray(sql, cols, ngayBatDau, ngayKetThuc);
    }

    public List<Object[]> getTongDoanhThu() {

        String sql = "{CALL getTongDoanhThu}";
        String[] cols = {"TenSanPham", "TongDoanhThu"};

        return this.getListOfArray(sql, cols);
    }

    public List<Object[]> getThongKeFull() {

        String sql = "{CALL thongKeFull}";
        String[] cols = {"MaSanPham", "TenSanPham", "HangSanXuat", "DonGia", "Tong So Luong", "Tổng tiền", "NgayTao"};

        return this.getListOfArray(sql, cols);
    }

    public List<Object[]> getThongKeTop5(Date ngayBatDau, Date ngayKetThuc) {

        String sql = "{CALL Top5BanChay(?,?)}";
        String[] cols = {"MaSanPham", "TenSanPham", "HangSanXuat", "DonGia", "Số lượng bán", "Tổng tiền", "NgayTao"};

        return this.getListOfArray(sql, cols, ngayBatDau, ngayKetThuc);
    }

    public List<Object[]> getThongKeTheoHangSanXuat(Date ngayBatDau, Date ngayKetThuc, String hang) {

        String sql = "{CALL thongKeTheoHang(?,?,?)}";
        String[] cols = {"MaSanPham", "TenSanPham", "HangSanXuat", "DonGia", "Tong So Luong", "Tổng tiền", "NgayTao"};

        return this.getListOfArray(sql, cols, ngayKetThuc, ngayKetThuc, hang);
    }

    public static void main(String[] args) {

        ThongKeDAO d = new ThongKeDAO();

        List<Object[]> l = d.getThongKeFull();
        for (Object[] o : l) {

            System.out.println(o[0] + "    số lương" + o[1]);
        }
    }
}
