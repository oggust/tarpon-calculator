#!/bin/bash 

GP_SCRIPT="${0%/*}/chart_card.gnuplot"

TMP=$( mktemp -d )
${GP_SCRIPT} use_si             > ${TMP}/chart.png
${GP_SCRIPT} use_si do_stdgirth > ${TMP}/girth.png

pngtopam <${TMP}/chart.png | pnminvert > ${TMP}/chart.pam
pngtopam <${TMP}/girth.png | pnminvert > ${TMP}/girth.pam

pamarith -add ${TMP}/chart.pam ${TMP}/girth.pam | pnminvert | pamtopng 
