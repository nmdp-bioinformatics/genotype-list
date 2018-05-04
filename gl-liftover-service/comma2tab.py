#!/usr/bin/env python3
"""
comma2tab.py
removes lines that begin with #
changes commas to tab in remaining lines
"""

import argparse

parser = argparse.ArgumentParser()
parser.add_argument("-c", "--infile", help="input file with comma separated fields", type=str)
parser.add_argument("-t", "--outfile", help="output file in tab delmited format", type=str)

args = parser.parse_args()

with open(args.infile) as fin, open(args.outfile, 'w') as fout:
    for line in fin:
        if not line.startswith('#'):
            fout.write(line.replace(',','\t'))
