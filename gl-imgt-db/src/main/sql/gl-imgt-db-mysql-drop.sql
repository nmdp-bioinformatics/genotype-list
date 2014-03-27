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

-- Drop Tables

use imgt_hla;

drop table if exists SOURCE_XREF;

drop table if exists SEQUENCE_COORDINATE;

drop table if exists REMOVED_ALLELE;

drop table if exists NUC_SEQUENCE;

drop table if exists INCOMPLETE_ALLELE;

drop table if exists G_GRP_RLS_VER_DTL;

drop table if exists G_GROUP_ALLELE;

drop table if exists SPECIES;

drop table if exists SAMPLE;

drop table if exists ETHNICITY;

drop table if exists SOURCE_MATERIAL;

drop table if exists CITATION;

drop table if exists CDNA_INDEL;

drop table if exists CDNA_COORDINATE;

drop table if exists FEATURE_TRANSLATION;

drop table if exists FEATURE;

drop table if exists AMBIG_COMBO_ELEMENT;

drop table if exists AMBIG_COMBO_GROUP;

drop table if exists G_GROUP;

drop table if exists ALLELE_RLS_VER_DTL;

drop table if exists ALIGNMENT_REFERENCE;

drop table if exists SEQUENCE;

drop table if exists ALLELE;

drop table if exists RELEASE_VERSION;

drop table if exists LOCUS;
