package com.polytrout.tarpon;

/**
 * The actual math.
 */
class Formula {

    // Classic formula. In clown units, so convert.
    public static double oldFormula(double length, double girth) {
        final double inch_length = length / 2.54;
        final double inch_girth = girth / 2.54;
        final double pounds_weight = inch_length * inch_girth * inch_girth / 800.0;
        return pounds_weight * 0.453592; //says google
    }

    // New and shiny (ALE) formula. (Ault and Luo, 2013)
    // ("A reliable game fish weight estimation model for Atlantic Tarpon (Megalops Atlanticus)")
    // (The non-java-like variable names are from the paper
    // L and G are in cm, the return value in kg.
    public static double newFormula(double L, double G) {
        final double b0 = 2.828;
        final double b1 = 0.0000296;
        final double b2 = 0.006123;
        final double b3 = -0.008284;
        final double b4 = 0.1845;
        final double b5 = -0.1943;

        final double G2 = G*G;
        return  b0 + b1*G2*L + b2*G*L + b3*G2 + b4*G + b5*L;
    }

}
