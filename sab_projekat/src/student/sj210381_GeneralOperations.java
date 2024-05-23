/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author Jana
 */
public class sj210381_GeneralOperations implements GeneralOperations {

    @Override
    public void eraseAll() {
        Connection conn = DB.getInstance().getConnection();
        String[] tables = new String[]{"AcceptedOffer", "TransportOffer", "Package", "CourierRequest", "Courier",
            "Admin", "Vehicle", "User", "District", "City"};
        for (String table : tables) {
            String query = "delete from [" + table + "]";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.executeUpdate();
            } catch (SQLException ex) {
            }
        }
    }

}
