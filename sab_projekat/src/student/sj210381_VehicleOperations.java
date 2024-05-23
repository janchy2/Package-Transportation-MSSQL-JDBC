/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.VehicleOperations;

/**
 *
 * @author Jana
 */
public class sj210381_VehicleOperations implements VehicleOperations {
    
    @Override
    public boolean insertVehicle(String string, int i, BigDecimal bd) {
        Connection conn = DB.getInstance().getConnection();
        String query = "insert into [Vehicle](licencePlateNumber, fuelType, fuelConsumption) values(?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
            ps.setInt(2, i);
            ps.setBigDecimal(3, bd);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    @Override
    public int deleteVehicles(String... strings) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [Vehicle] where licencePlateNumber=?";
        int numRows = 0;
        sj210381_CourierOperations courierOperations = new sj210381_CourierOperations();
        sj210381_CourierRequestOperations courierRequestOperations = new sj210381_CourierRequestOperations();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (String string : strings) {
                courierOperations.deleteCourierWithLicencePlate(string);
                courierRequestOperations.deleteCourierRequestWithLicencePlate(string);
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
    public List<String> getAllVehichles() {
        Connection conn = DB.getInstance().getConnection();
        List<String> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select licencePlateNumber from [Vehicle]");) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }
    
    @Override
    public boolean changeFuelType(String string, int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "update [Vehicle] set fuelType=? where licencePlateNumber=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(2, string);
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
    public boolean changeConsumption(String string, BigDecimal bd) {
        Connection conn = DB.getInstance().getConnection();
        String query = "update [Vehicle] set fuelConsumption=? where licencePlateNumber=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(2, string);
            ps.setBigDecimal(1, bd);
            int affected = ps.executeUpdate();
            if (affected == 1) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }
    
}
