#!/usr/bin/gnuplot -c
set terminal pngcairo size 900,580 font "Arial,8"

# Commandline switch for USC units
if (ARG1 eq "use_usc") {
    use_SI = 0
} else {
    use_SI = 1
}

if (ARG2 eq "do_stdgirth") {
    do_stdgirth = 1
} else {
    do_stdgirth = 0
}

use_old = 0

b0 = 2.828
b1 = 0.0000296
b2 = 0.006123
b3 = -0.008284
b4 = 0.1845
b5 = -0.1943
	    
ale(L,G) = b0 + b1*G*G*L + b2*G*L + b3*G*G + b4*G + b5*L

usc_ale(L,G) = ale(L*2.54, G*2.54)/0.453592
usc_old(L,G) = G*G*L/800
old(L,G) = usc_old(L/2.54, G/2.54)*0.453592

# See the code.
stdgirth(L) = 5.424571 + 0.479914*L

set lmargin at screen 0.05
set rmargin at screen 0.85
set bmargin at screen 0.15
set tmargin at screen 0.95

if (use_SI) {
    set xrange [100 : 250]
    set yrange [50  : 140]
    if (do_stdgirth) {
        set key outside right bottom
        unset xtics
        unset ytics
        plot stdgirth(x) with linespoints
    } else {
        set key noautotitle
        set key outside right
        set contour base
        set view map
        unset surface
        set xlabel "Length (cm)"
        set ylabel "Girth (cm)"
        set key title "Weight (kg)"
        set cntrparam levels incremental 10, 10, 170
        if (use_old) {
            set title "Old formula"
            splot old(x,y)
        } else {
            set title "ALE formula"
            splot ale(x,y)
        }
    }

} else {
    set key noautotitle
    set key outside right
    set contour base
    set view map
    unset surface
    set xrange [40 : 90]
    set yrange [19 : 51]
    set xlabel "Length (in)"
    set ylabel "Girth (in)"
    set key title "Weight (lb)"
    set cntrparam levels incremental 30, 10, 270
    if (use_old) {
        set title "Old formula"
        splot usc_old(x,y)
    } else {
        set title "ALE formula"
        splot usc_ale(x,y)
    }

}
