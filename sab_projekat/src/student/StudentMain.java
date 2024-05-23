package student;

import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;
import rs.etf.sab.operations.*;


public class StudentMain {

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
