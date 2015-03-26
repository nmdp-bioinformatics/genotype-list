#
#
#    gl-imgt-biosql  IMGT/HLA in biosql.
#    Copyright (c) 2012-2015 National Marrow Donor Program (NMDP)
#
#    This library is free software; you can redistribute it and/or modify it
#    under the terms of the GNU Lesser General Public License as published
#    by the Free Software Foundation; either version 3 of the License, or (at
#    your option) any later version.
#
#    This library is distributed in the hope that it will be useful, but WITHOUT
#    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
#    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
#    License for more details.
#
#    You should have received a copy of the GNU Lesser General Public License
#    along with this library;  if not, write to the Free Software Foundation,
#    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.
#
#    > http://www.fsf.org/licensing/licenses/lgpl.html
#    > http://www.opensource.org/licenses/lgpl-license.php
#
#
from BioSQL import BioSeqDatabase

server = BioSeqDatabase.open_database(driver="MySQLdb", user="imgt-hla-biosql",
                                      passwd = "imgt-hla-biosql", host = "localhost", db="imgt_hla_biosql")

db = server["imgt-hla-test0"]
seq_record = db.lookup(gi="HLA00001")
print seq_record.id, seq_record.description
print "Sequence length %i" % len(seq_record.seq)
print seq_record
print "Features:"
for feature in seq_record.features : print feature
