create or alter table locus_id (
 locus_id_pk biginteger not null,
 glstring blob not null,
 glstring_hash varbinary(64) not null,
 id varchar2(512) not null,
 primary key (locus_id_pk)
 key (glstring_hash)
);

create or alter table allele_id (
 allele_id_pk biginteger not null,
 glstring blob not null,
 glstring_hash varbinary(64) not null,
 id varchar2(512) not null,
 primary key (allele_id_pk)
 key (glstring_hash)
);

create or alter table allele_list_id (
 allele_list_id_pk biginteger not null,
 glstring blob not null,
 glstring_hash varbinary(64) not null,
 id varchar2(512) not null,
 primary key (allele_list_id_pk)
 key (glstring_hash)
);

create or alter table haplotype_id (
 haplotype_id_pk biginteger not null,
 glstring blob not null,
 glstring_hash varbinary(64) not null,
 id varchar2(512) not null,
 primary key (haplotype_id_pk)
 key (glstring_hash)
);

create or alter table genotype_id (
 genotype_id_pk biginteger not null,
 glstring blob not null,
 glstring_hash varbinary(64) not null,
 id varchar2(512) not null,
 primary key (genotype_id_pk)
 key (glstring_hash)
);

create or alter table genotype_list_id (
 genotype_list_id_pk biginteger not null,
 glstring blob not null,
 glstring_hash varbinary(64) not null,
 id varchar2(512) not null,
 primary key (genotype_list_id_pk)
 key (glstring_hash)
);

create or alter table multilocus_unphased_genotype_id (
 multilocus_unphased_genotype_id_pk biginteger not null,
 glstring blob not null,
 glstring_hash varbinary(64) not null,
 id varchar2(512) not null,
 primary key (multilocus_unphased_genotype_id_pk)
 key (glstring_hash)
);

create or alter table locus (
 id varchar2(512) not null,
 locus blob not null,
 primary key (id)
);

create or alter table allele (
 id varchar2(512) not null,
 allele blob not null,
 primary key (id)
);

create or alter table allele_list (
 id varchar2(512) not null,
 allele_list blob not null,
 primary key (id)
);

create or alter table haplotype (
 id varchar2(512) not null,
 haplotype blob not null,
 primary key (id)
);

create or alter table genotype (
 id varchar2(512) not null,
 genotype blob not null,
 primary key (id)
);

create or alter table genotype_list (
 id varchar2(512) not null,
 genotype_list blob not null,
 primary key (id)
);

create or alter table multilocus_unphased_genotype (
 id varchar2(512) not null,
 multilocus_unphased_genotype blob not null,
 primary key (id)
);

create sequence locus_id_pk_seq start with 1 increment by 1;

create or replace trigger locus_id_insert
before insert on locus_id
for each row
begin
    select locus_id_pk_seq.nextval into :new.locus_id_pk from dual;
end;
/

create sequence allele_id_pk_seq start with 1 increment by 1;

create or replace trigger allele_id_insert
before insert on allele_id
for each row
begin
    select allele_id_pk_seq.nextval into :new.allele_id_pk from dual;
end;
/

create sequence allele_list_id_pk_seq start with 1 increment by 1;

create or replace trigger allele_list_id_insert
before insert on allele_list_id
for each row
begin
    select allele_list_id_pk_seq.nextval into :new.allele_list_id_pk from dual;
end;
/

create sequence haplotype_id_pk_seq start with 1 increment by 1;

create or replace trigger haplotype_id_insert
before insert on haplotype_id
for each row
begin
    select haplotype_id_pk_seq.nextval into :new.haplotype_id_pk from dual;
end;
/

create sequence genotype_id_pk_seq start with 1 increment by 1;

create or replace trigger genotype_id_insert
before insert on genotype_id
for each row
begin
    select genotype_id_pk_seq.nextval into :new.genotype_id_pk from dual;
end;
/

create sequence genotype_list_id_pk_seq start with 1 increment by 1;

create or replace trigger genotype_list_id_insert
before insert on genotype_list_id
for each row
begin
    select genotype_list_id_pk_seq.nextval into :new.genotype_list_id_pk from dual;
end;
/

create sequence multilocus_unphased_genotype_id_pk_seq start with 1 increment by 1;

create or replace trigger multilocus_unphased_genotype_id_insert
before insert on multilocus_unphased_genotype_id
for each row
begin
    select multilocus_unphased_genotype_id_pk_seq.nextval into :new.multilocus_unphased_genotype_id_pk from dual;
end;
/
