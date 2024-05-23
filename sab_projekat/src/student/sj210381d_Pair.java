/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author Jana
 */
public class sj210381d_Pair<A, B> implements PackageOperations.Pair<A, B> {

    private A a;
    private B b;

    public sj210381d_Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public A getFirstParam() {
        return a;
    }

    @Override
    public B getSecondParam() {
        return b;
    }

    public static boolean equals(PackageOperations.Pair a, PackageOperations.Pair b) {
        return a.getFirstParam().equals(b.getFirstParam()) && a.getSecondParam().equals(b.getSecondParam());
    }

}
