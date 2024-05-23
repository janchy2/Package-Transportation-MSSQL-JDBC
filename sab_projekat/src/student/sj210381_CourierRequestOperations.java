/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.CourierRequestOperation;

/**
 *
 * @author Jana
 */
public class sj210381_CourierRequestOperations implements CourierRequestOperation {

    @Override
    public boolean insertCourierRequest(String string, String string1) {
        Connection conn = DB.getInstance().getConnection();
        String query = "insert into [CourierRequest](userName, licencePlateNumber) values(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
            ps.setString(2, string1);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean deleteCourierRequest(String string) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [CourierRequest] where userName=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
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
    public boolean changeVehicleInCourierRequest(String string, String string1) {
        Connection conn = DB.getInstance().getConnection();
        String query = "update [CourierRequest] set licencePlateNumber=? where userName=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string1);
            ps.setString(2, string);
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
    public List<String> getAllCourierRequests() {
        Connection conn = DB.getInstance().getConnection();
        List<String> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select userName from [CourierRequest]");) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public boolean grantRequest(String string) {
        Connection conn = DB.getInstance().getConnection();
        try (CallableStatement cstmt = conn.prepareCall("{? = call spGrantRequest(?)}")) {
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, string);
            cstmt.execute();
            if (cstmt.getInt(1) == 0) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    public void deleteCourierRequestWithLicencePlate(String string) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [CourierRequest] where licencePlateNumber=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
            ps.executeUpdate();
        } catch (SQLException ex) {
        }
    }
}
