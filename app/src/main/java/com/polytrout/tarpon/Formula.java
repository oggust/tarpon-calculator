package com.polytrout.tarpon;

/**
 * The actual math. All the interface in here are in SI units. (cm and kg)
 */
class Formula {

    // Classic "Wood's" formula. Originally in clown units, so convert.
    public static double oldFormula(double cm_length, double cm_girth) {
        final double inch_length = cm_length / 2.54;
        final double inch_girth = cm_girth / 2.54;
        final double pounds_weight = inch_length * inch_girth * inch_girth / 800.0;
        return pounds_weight * 0.453592;
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

    // The a and b values are from the Fishbase page
    // (http://www.fishbase.org/Summary/SpeciesSummary.php?ID=1079)
    //
    // But the error bars are pretty significant. They have b in a range from 2.82 - 3.14
    // and a in  0.00458 - 0.01378, which result in weights between 10 (low, low) and 166 kg
    // (high,high) for a 180cm Tarpon!
    //
    // Also, just from eyeballing, these numbers look low - the estimate looks really light.
    //
    // You could probably derive better values from the ALE paper, or
    // better(?) get this factor directly from length and girth.
    public static double conditionIndex(double cm_length, double kg_weight) {
        final double a = 0.00794;
        final double b = 2.98;

        final double kg_standard_weight = a*Math.pow(cm_length, b);

        return kg_weight/kg_standard_weight;
    }

    //  From "Atlantic Tarpon Allometric Growth Model" (Ault)
    public static double girthFromLength(double len) {
        return 5.424571 + 0.479914 * len;
    }

}
