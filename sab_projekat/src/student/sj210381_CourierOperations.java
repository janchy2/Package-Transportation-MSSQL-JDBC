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
import rs.etf.sab.operations.CourierOperations;

/**
 *
 * @author Jana
 */
public class sj210381_CourierOperations implements CourierOperations {

    @Override
    public boolean insertCourier(String string, String string1) {
        Connection conn = DB.getInstance().getConnection();
        String query = "insert into [Courier](userName, licencePlateNumber, [status], profit, numberOfDeliveries, currentProfit) values(?, ?, 0, 0, 0, 0)";
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
    public boolean deleteCourier(String string) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [Courier] where userName=?";
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
    public List<String> getCouriersWithStatus(int i) {
        Connection conn = DB.getInstance().getConnection();
        List<String> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select userName from [Courier] where [status]=" + i);) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public List<String> getAllCouriers() {
        Connection conn = DB.getInstance().getConnection();
        List<String> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select userName from [Courier]");) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public BigDecimal getAverageCourierProfit(int i) {
        Connection conn = DB.getInstance().getConnection();
        BigDecimal result;
        int sum;
        int count;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select sum(profit), count(*) from [Courier] where numberOfDeliveries>=" + i);) {
            rs.next();
            Object o = rs.getObject(1);
            if (o == null) {
                return null;
            }
            sum = rs.getInt(1);
            count = rs.getInt(2);
            double res = (double) sum / count;
            return new BigDecimal(res);
        } catch (SQLException ex) {
            return null;
        }
    }

    public void deleteCourierWithLicencePlate(String string) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [Courier] where licencePlateNumber=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
            ps.executeUpdate();
        } catch (SQLException ex) {
        }
    }

    public void setDriving(String string) {
        Connection conn = DB.getInstance().getConnection();
        String query = "update [Courier] set status=1 where userName=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
            ps.executeUpdate();
        } catch (SQLException ex) {
        }
    }

}
