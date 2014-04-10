Install biopython:

$ sudo easy_install numpy
$ sudo easy_install biopython
$ sudo easy_install MySQL-python

$ python read-imgt.py | grep /Library
HLA06630
HLA00959
HLA06625
/Library/Python/2.7/site-packages/biopython-1.63-py2.7-macosx-10.7-intel.egg/Bio/GenBank/__init__.py:1096: BiopythonParserWarning: Couldn't parse feature location: '<1..546>'
  % (location_line)))
...

Fix warnings:

$ grep -v "<1" hla.dat > hla-fix.dat

$ python load-imgt.py
Created new database
Loaded 10689 records

$ python pull-seq.py
HLA00001 HLA-A*01:01:01:01, Human MHC Class I sequence
Sequence length 3503
ID: HLA00001
...
