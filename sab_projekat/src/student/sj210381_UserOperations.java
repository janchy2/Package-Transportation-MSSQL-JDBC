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
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author Jana
 */
public class sj210381_UserOperations implements UserOperations {
    
    @Override
    public boolean insertUser(String string, String string1, String string2, String string3) {
        Connection conn = DB.getInstance().getConnection();
        String query = "insert into [User](userName, firstName, lastName, password, numberOfSentPackages) values(?, ?, ?, ?, 0)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
            ps.setString(2, string1);
            ps.setString(3, string2);
            ps.setString(4, string3);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    @Override
    public int declareAdmin(String string) {
        if (!this.exists(string)) {
            return 2;
        }
        Connection conn = DB.getInstance().getConnection();
        String query = "insert into [Admin](userName) values(?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
            ps.executeUpdate();
            return 0;
        } catch (SQLException ex) {
            return 1;
        }
    }
    
    @Override
    public Integer getSentPackages(String... strings) {
        int sum = 0;
        for (String string : strings) {
            int num = this.getSentPackagesForOne(string);
            if (num == -1) {
                return null;
            }
            sum += num;
        }
        return sum;
    }
    
    @Override
    public int deleteUsers(String... strings) {
        int affected = 0;
        for (String string : strings) {
            affected += this.deleteUser(string);
        }
        return affected;
    }
    
    @Override
    public List<String> getAllUsers() {
        Connection conn = DB.getInstance().getConnection();
        List<String> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select userName from [User]");) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }
    
    private int getSentPackagesForOne(String string) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select numberOfSentPackages from [User] where userName=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException ex) {
            return -1;
        }
    }
    
    private int deleteUser(String string) {
        new sj210381_CourierRequestOperations().deleteCourierRequest(string);
        new sj210381_PackageOperations().deletePackagesForUser(string);
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [User] where userName=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
            int affected = ps.executeUpdate();
            return affected;
        } catch (SQLException ex) {
            return 0;
        }
    }
    
    private boolean exists(String string) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select * from [User] where userName=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
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
