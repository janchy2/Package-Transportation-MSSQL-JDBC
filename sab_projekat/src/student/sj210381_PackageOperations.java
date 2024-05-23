/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author Jana
 */
public class sj210381_PackageOperations implements PackageOperations {

    @Override
    public int insertPackage(int i, int i1, String string, int i2, BigDecimal bd) {
        Connection conn = DB.getInstance().getConnection();
        BigDecimal price = this.calculatePrice(i2, bd, i, i1);
        String query = "insert into [Package](districtFrom, districtTo, userName, packageType, weight, price, [status]) "
                + "values(?, ?, ?, ?, ?, ? ,0)";
        try (PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i);
            ps.setInt(2, i1);
            ps.setString(3, string);
            ps.setInt(4, i2);
            ps.setBigDecimal(5, bd);
            ps.setBigDecimal(6, price);
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
    public int insertTransportOffer(String string, int i, BigDecimal bd) {
        Connection conn = DB.getInstance().getConnection();
        if (bd == null) {
            bd = new BigDecimal(Math.random() * 10 * ((Math.random() > 0.5) ? 1 : -1));
        }
        String query = "insert into [TransportOffer](userName, idPackage, pricePercentage) "
                + "values(?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(2, i);
            ps.setString(1, string);
            ps.setBigDecimal(3, bd);
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
    public boolean acceptAnOffer(int i) {
        Connection conn = DB.getInstance().getConnection();
        try (CallableStatement cstmt = conn.prepareCall("{? = call spAcceptOffer(?)}")) {
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setInt(2, i);
            cstmt.execute();
            if (cstmt.getInt(1) == 0) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public List<Integer> getAllOffers() {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select idOffer from [TransportOffer]");) {
            while (rs.next()) {
                result.add(rs.getInt(1));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int i) {
        Connection conn = DB.getInstance().getConnection();
        List<Pair<Integer, BigDecimal>> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select idOffer, pricePercentage from [TransportOffer] where idPackage=" + i);) {
            while (rs.next()) {
                result.add(new sj210381d_Pair<Integer, BigDecimal>(rs.getInt(1), rs.getBigDecimal(2)));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public boolean deletePackage(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [Package] where idPackage=?";
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
    public boolean changeWeight(int i, BigDecimal bd) {
        Connection conn = DB.getInstance().getConnection();
        String query = "update [Package] set weight=? where idPackage=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBigDecimal(1, bd);
            ps.setInt(2, i);
            int affected = ps.executeUpdate();
            if (affected == 1) {
                this.setNewPrice(i);
                return true;
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean changeType(int i, int i1) {
        Connection conn = DB.getInstance().getConnection();
        String query = "update [Package] set packageType=? where idPackage=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, i1);
            ps.setInt(2, i);
            int affected = ps.executeUpdate();
            if (affected == 1) {
                this.setNewPrice(i);
                return true;
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public Integer getDeliveryStatus(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select [status] from [Package] where idPackage=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return null;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public BigDecimal getPriceOfDelivery(int i) {
        Connection conn = DB.getInstance().getConnection();
        try (CallableStatement cstmt = conn.prepareCall("{call spGetDeliveryPrice(?, ?)}")) {
            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.setInt(2, i);
            cstmt.execute();
            if (cstmt.getBigDecimal(1).doubleValue() == -1) {
                return null;
            }
            return cstmt.getBigDecimal(1);
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public Date getAcceptanceTime(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select acceptanceTime from [Package] where idPackage=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp timestamp = rs.getTimestamp(1);
                return timestamp != null ? new Date(timestamp.getTime()) : null;
            }
            return null;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int i) {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select idPackage from [Package] where packageType=" + i);) {
            while (rs.next()) {
                result.add(rs.getInt(1));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public List<Integer> getAllPackages() {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select idPackage from [Package]");) {
            while (rs.next()) {
                result.add(rs.getInt(1));
            }
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public List<Integer> getDrive(String string) {
        Connection conn = DB.getInstance().getConnection();
        List<Integer> result = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select Package.idPackage from [Package] join "
                + "[AcceptedOffer] on(Package.idPackage=AcceptedOffer.idPackage) where AcceptedOffer.username=" + string + " and Package.[status]=1");) {
            while (rs.next()) {
                result.add(rs.getInt(1));
            }

            if (result.size() == 0) {
                return null;
            }
            
            return result;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public int driveNextPackage(String string) {
        Connection conn = DB.getInstance().getConnection();
        int idPackage = -1;
        int nextPackage = -1;
        try (CallableStatement cstmt = conn.prepareCall("{call spNextDrive(?, ?, ?)}")) {
            cstmt.setString(1, string);
            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.registerOutParameter(3, Types.INTEGER);
            cstmt.execute();
            idPackage = cstmt.getInt(2);
            if (idPackage == -1) {
                return -1;
            }
            new sj210381_CourierOperations().setDriving(string);
            nextPackage = cstmt.getInt(3);

        } catch (SQLException ex) {
            return -2;
        }

        BigDecimal distance = new BigDecimal(this.getDistanceForPackage(idPackage));
        BigDecimal betweenDistance = new BigDecimal(-1);
        if (nextPackage != -1) {
            betweenDistance = new BigDecimal(this.getDistanceBetween(idPackage, nextPackage));
        }

        try (CallableStatement cstmt = conn.prepareCall("{call spCalculateProfit(?, ?, ?, ?)}")) {
            cstmt.setBigDecimal(1, distance);
            cstmt.setBigDecimal(2, betweenDistance);
            cstmt.setInt(3, idPackage);
            cstmt.setString(4, string);
            cstmt.execute();
        } catch (SQLException ex) {
            return -2;
        }
        return idPackage;
    }

    private BigDecimal calculatePrice(int type, BigDecimal weight, int districtFrom, int districtTo) {
        double distance = new sj210381_DistrictOperations().distance(districtFrom, districtTo);
        switch (type) {
            case 0:
                return (new BigDecimal(10.0D * distance));
            case 1:
                return (new BigDecimal((25.0D + weight.doubleValue() * 100.0D) * distance));
            case 2:
                return (new BigDecimal((75.0D + weight.doubleValue() * 300.0D) * distance));
        }
        return null;
    }

    private void setNewPrice(int i) {
        BigDecimal newPrice = this.calculateNewPrice(i);

        Connection conn = DB.getInstance().getConnection();
        String query = "update [Package] set price=? where idPackage=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBigDecimal(1, newPrice);
            ps.setInt(2, i);
            ps.executeUpdate();
        } catch (SQLException ex) {
        }
    }

    private BigDecimal calculateNewPrice(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select districtFrom, districtTo, packageType, weight from [Package] where idPackage=?";
        int districtFrom = -1, districtTo = -1, packageType = -1;
        BigDecimal weight = null;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            rs.next();
            districtFrom = rs.getInt(1);
            districtTo = rs.getInt(2);
            packageType = rs.getInt(3);
            weight = rs.getBigDecimal(4);
        } catch (SQLException ex) {
        }

        return this.calculatePrice(packageType, weight, districtFrom, districtTo);
    }

    private double getDistanceForPackage(int i) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select districtFrom, districtTo from [Package] where idPackage=?";
        int districtFrom = -1, districtTo = -1;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            rs.next();
            districtFrom = rs.getInt(1);
            districtTo = rs.getInt(2);
        } catch (SQLException ex) {
        }

        return new sj210381_DistrictOperations().distance(districtFrom, districtTo);
    }

    private double getDistanceBetween(int i, int i1) {
        Connection conn = DB.getInstance().getConnection();
        String query = "select districtTo from [Package] where idPackage=?";
        int districtFrom = -1, districtTo = -1;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, i);
            ResultSet rs = ps.executeQuery();
            rs.next();
            districtTo = rs.getInt(1);
        } catch (SQLException ex) {
        }

        query = "select districtFrom from [Package] where idPackage=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, i1);
            ResultSet rs = ps.executeQuery();
            rs.next();
            districtFrom = rs.getInt(1);
        } catch (SQLException ex) {
        }

        return new sj210381_DistrictOperations().distance(districtFrom, districtTo);
    }

    public void deletePackagesForUser(String string) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from [Package] where userName=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, string);
            ps.executeUpdate();
        } catch (SQLException ex) {
        }
    }
}
