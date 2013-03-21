#!/bin/sh

GL_BIN=../../../target/appassembler/bin/

for f in tests/*; 
do 
#ARGS="--namespace http://gl.immunogenomics.org/1.0 -g $f"
ARGS="--namespace http://gl.immunogenomics.org/gl-demo -g $f -t Od7hoCBghHSzuxPl"
echo $ARGS
$GL_BIN/gl-register-loci $ARGS
$GL_BIN/gl-register-alleles $ARGS
$GL_BIN/gl-register-allele-lists $ARGS
$GL_BIN/gl-register-genotypes $ARGS
$GL_BIN/gl-register-genotype-lists $ARGS
$GL_BIN/gl-register-haplotypes $ARGS
$GL_BIN/gl-register-multilocus-unphased-genotypes $ARGS
done
