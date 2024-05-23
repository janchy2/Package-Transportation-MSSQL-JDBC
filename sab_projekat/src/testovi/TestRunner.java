package testovi;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

public final class TestRunner {
   private static final int MAX_POINTS_ON_PUBLIC_TEST = 10;
   private static final Class[] UNIT_TEST_CLASSES = new Class[]{CityOperationsTest.class, DistrictOperationsTest.class, UserOperationsTest.class, VehicleOperationsTest.class, PackageOperationsTest.class};
   private static final Class[] MODULE_TEST_CLASSES = new Class[]{PublicModuleTest.class};

   private static double runUnitTestsPublic() {
      double numberOfSuccessfulCases = 0.0D;
      double numberOfAllCases = 0.0D;
      double points = 0.0D;
      JUnitCore jUnitCore = new JUnitCore();
      Class[] var7 = UNIT_TEST_CLASSES;
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Class testClass = var7[var9];
         System.out.println("\n" + testClass.getName());
         Request request = Request.aClass(testClass);
         Result result = jUnitCore.run(request);
         numberOfAllCases = (double)result.getRunCount();
         numberOfSuccessfulCases = (double)(result.getRunCount() - result.getFailureCount());
         if (numberOfSuccessfulCases < 0.0D) {
            numberOfSuccessfulCases = 0.0D;
         }

         System.out.println("Successful: " + numberOfSuccessfulCases);
         System.out.println("All: " + numberOfAllCases);
         double points_curr = 2.0D * numberOfSuccessfulCases / numberOfAllCases;
         System.out.println("Points: " + points_curr);
         points += points_curr;
      }

      return points;
   }

   private static double runModuleTestsPublic() {
      double numberOfSuccessfulCases = 0.0D;
      double numberOfAllCases = 0.0D;
      double points = 0.0D;
      JUnitCore jUnitCore = new JUnitCore();
      Class[] var7 = MODULE_TEST_CLASSES;
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Class testClass = var7[var9];
         System.out.println("\n" + testClass.getName());
         Request request = Request.aClass(testClass);
         Result result = jUnitCore.run(request);
         numberOfAllCases = (double)result.getRunCount();
         numberOfSuccessfulCases = (double)(result.getRunCount() - result.getFailureCount());
         if (numberOfSuccessfulCases < 0.0D) {
            numberOfSuccessfulCases = 0.0D;
         }

         System.out.println("Successful: " + numberOfSuccessfulCases);
         System.out.println("All: " + numberOfAllCases);
         double points_curr = 2.0D * numberOfSuccessfulCases / numberOfAllCases;
         System.out.println("Points: " + points_curr);
         points += points_curr;
      }

      return points;
   }

   private static double runPublic() {
      double res = 0.0D;
      res += runUnitTestsPublic();
      res += runModuleTestsPublic();
      return res;
   }

   public static void runTests() {
      double resultsPublic = runPublic();
      System.out.println("Points won on public tests is: " + resultsPublic + " out of 10");
   }
}
