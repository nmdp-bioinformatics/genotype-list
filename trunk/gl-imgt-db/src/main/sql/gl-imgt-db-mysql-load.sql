/*

    gl-imgt-db  IMGT/HLA database for the gl project.
    Copyright (c) 2012-2014 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.fsf.org/licensing/licenses/lgpl.html
    > http://www.opensource.org/licenses/lgpl-license.php

*/

-- Note: Must be run before database can be loaded using gl-imgt-loader app
-- Load the LOCUS table

use imgt_hla;

insert into LOCUS (LOCUS_IID,LOCUS_NAME,GENE_SYSTEM,CLASS,CREATE_DTE,LAST_UPDATE_DTE,LAST_UPDATE_USER_ID) values (1,'A','HLA','I',now(),now(),'imgt_admin');

insert into LOCUS (LOCUS_IID,LOCUS_NAME,GENE_SYSTEM,CLASS,CREATE_DTE,LAST_UPDATE_DTE,LAST_UPDATE_USER_ID) values (2,'B','HLA','I',now(),now(),'imgt_admin');

insert into LOCUS (LOCUS_IID,LOCUS_NAME,GENE_SYSTEM,CLASS,CREATE_DTE,LAST_UPDATE_DTE,LAST_UPDATE_USER_ID) values (3,'C','HLA','I',now(),now(),'imgt_admin');

insert into LOCUS (LOCUS_IID,LOCUS_NAME,GENE_SYSTEM,CLASS,CREATE_DTE,LAST_UPDATE_DTE,LAST_UPDATE_USER_ID) values (4,'DPA1','HLA','II',now(),now(),'imgt_admin');

insert into LOCUS (LOCUS_IID,LOCUS_NAME,GENE_SYSTEM,CLASS,CREATE_DTE,LAST_UPDATE_DTE,LAST_UPDATE_USER_ID) values (5,'DPB1','HLA','II',now(),now(),'imgt_admin');

insert into LOCUS (LOCUS_IID,LOCUS_NAME,GENE_SYSTEM,CLASS,CREATE_DTE,LAST_UPDATE_DTE,LAST_UPDATE_USER_ID) values (6,'DQA1','HLA','II',now(),now(),'imgt_admin');

insert into LOCUS (LOCUS_IID,LOCUS_NAME,GENE_SYSTEM,CLASS,CREATE_DTE,LAST_UPDATE_DTE,LAST_UPDATE_USER_ID) values (7,'DQB1','HLA','II',now(),now(),'imgt_admin');

insert into LOCUS (LOCUS_IID,LOCUS_NAME,GENE_SYSTEM,CLASS,CREATE_DTE,LAST_UPDATE_DTE,LAST_UPDATE_USER_ID) values (8,'DRB1','HLA','II',now(),now(),'imgt_admin');

insert into LOCUS (LOCUS_IID,LOCUS_NAME,GENE_SYSTEM,CLASS,CREATE_DTE,LAST_UPDATE_DTE,LAST_UPDATE_USER_ID) values (9,'DRB3','HLA','II',now(),now(),'imgt_admin');

insert into LOCUS (LOCUS_IID,LOCUS_NAME,GENE_SYSTEM,CLASS,CREATE_DTE,LAST_UPDATE_DTE,LAST_UPDATE_USER_ID) values (10,'DRB4','HLA','II',now(),now(),'imgt_admin');

insert into LOCUS (LOCUS_IID,LOCUS_NAME,GENE_SYSTEM,CLASS,CREATE_DTE,LAST_UPDATE_DTE,LAST_UPDATE_USER_ID) values (11,'DRB5','HLA','II',now(),now(),'imgt_admin');
