/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.DistrictOperations;

/**
 *
 * @author Jana
 */
public class sj210381_DistrictOperations implements DistrictOperations {

    @Override
    public int insertDistrict(String string, int i, int i1, int i2) {
        Connection conn = DB.getInstance().getConnection();
        String query = "insert into [District](xCord, yCord, idCity, name) values(?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(4, string);
            ps.setInt(1, i1);
            ps.setInt(2, i2);
            ps.setInt(3, i);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException ex) {
            return -1;
        }
    }

    @Override
    public int deleteDistricts(String... strings) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [District] where name=?";
        int numRows = 0;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (String string : strings) {
                ps.setString(1, string);
                int affected = ps.executeUpdate();
                numRows += affected;
            }
            return numRows;
        } catch (SQLException ex) {
            return 0;
        }
    }

    @Override
    public boolean deleteDistrict(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [District] where idDistrict=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, i);
            int affected = ps.executeUpdate();
            if (affected == 1) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public int deleteAllDistrictsFromCity(String string) {
        sj210381_CityOperations cityOperations = new sj210381_CityOperations();
        List<Integer> districtIds = this.getAllDistrictsFromCity(cityOperations.getIdCity(string));
        if (districtIds == null) {
            return 0;
        }
        int numRows = 0;
        for (int id : districtIds) {
            if (this.deleteDistrict(id)) {
                numRows++;
            }
        }
        return numRows;
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int i) {
        sj210381_CityOperations cityOperations = new sj210381_CityOperations();
        if (!cityOperations.exists(i)) {
            return null;
        }
        Connection conn = DB.getInstance().getConnection();
        List<Integer> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select idDistrict from [District] where idCity=" + i);) {
            while (rs.next()) {
                result.add(rs.getInt(1));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public List<Integer> getAllDistricts() {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select idDistrict from [District]");) {
            while (rs.next()) {
                result.add(rs.getInt(1));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    public void deleteAllDistrictsFromCity(int id) {
        List<Integer> districtIds = this.getAllDistrictsFromCity(id);
        if (districtIds == null) {
            return;
        }
        for (int districtId : districtIds) {
            this.deleteDistrict(districtId);
        }
    }

    public double distance(int districtFrom, int districtTo) {
        int x1, y1, x2, y2;
        int[] result = getCords(districtFrom);
        x1 = result[0];
        y1 = result[1];
        result = getCords(districtTo);
        x2 = result[0];
        y2 = result[1];

        int dx = x1 - x2;
        int dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private int[] getCords(int i) {
        int[] result = new int[2];
        Connection conn = DB.getInstance().getConnection();
        String query = "select xCord, yCord from [District] where idDistrict=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            rs.next();
            result[0] = rs.getInt(1);
            result[1] = rs.getInt(2);
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }
}
