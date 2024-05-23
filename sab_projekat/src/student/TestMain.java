/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;
import testovi.*;
import rs.etf.sab.operations.*;
/**
 *
 * @author Jana
 */
public class TestMain {
    public static void main(String[] args) {
        CityOperations cityOperations = new sj210381_CityOperations();
        DistrictOperations districtOperations = new sj210381_DistrictOperations();
        CourierOperations courierOperations = new sj210381_CourierOperations();
        CourierRequestOperation courierRequestOperation = new sj210381_CourierRequestOperations();
        GeneralOperations generalOperations = new sj210381_GeneralOperations();
        UserOperations userOperations = new sj210381_UserOperations();
        VehicleOperations vehicleOperations = new sj210381_VehicleOperations();
        PackageOperations packageOperations = new sj210381_PackageOperations();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
    }
    
}
