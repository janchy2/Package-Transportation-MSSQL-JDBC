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
import jdk.internal.org.jline.terminal.TerminalBuilder;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.DistrictOperations;

/**
 *
 * @author Jana
 */
public class sj210381_CityOperations implements CityOperations {

    @Override
    public int insertCity(String string, String string1) {
        Connection conn = DB.getInstance().getConnection();
        String query = "insert into [City](name, postalCode) values(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, string);
            ps.setString(2, string1);
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
    public int deleteCity(String... strings) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [City] where name=?";
        int numRows = 0;
        sj210381_DistrictOperations districtOperations = new sj210381_DistrictOperations();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (String string : strings) {
                districtOperations.deleteAllDistrictsFromCity(string);
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
    public boolean deleteCity(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [City] where idCity=?";
        sj210381_DistrictOperations districtOperations = new sj210381_DistrictOperations();
        districtOperations.deleteAllDistrictsFromCity(i);
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
    public List<Integer> getAllCities() {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select idCity from [City]");) {
            while (rs.next()) {
                result.add(rs.getInt(1));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    public int getIdCity(String name) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select idCity from [City] where name=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException ex) {
            return -1;
        }
    }

    public boolean exists(int id) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select * from [City] where idCity=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }
}
