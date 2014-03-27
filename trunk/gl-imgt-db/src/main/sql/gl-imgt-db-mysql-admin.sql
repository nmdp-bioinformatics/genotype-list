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

-- Note: Must be run as root
-- Drop and recreate imgt_hla database, create imgt_admin user

use mysql;

DROP DATABASE IF EXISTS imgt_hla;
CREATE DATABASE imgt_hla;

DROP USER 'imgt_admin'@'localhost';
CREATE USER 'imgt_admin'@'localhost' IDENTIFIED BY 'imgthla';
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON imgt_hla.* TO 'imgt_admin'@'localhost';
