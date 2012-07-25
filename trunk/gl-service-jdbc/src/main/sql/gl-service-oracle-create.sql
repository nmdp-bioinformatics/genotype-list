create or alter table locus_id (
 glstring varchar2(4000 byte) not null,
 id varchar2(512) not null,
 primary key (glstring)
);

create or alter table allele_id (
 glstring varchar2(4000 byte) not null,
 id varchar2(512) not null,
 primary key (glstring)
);

create or alter table allele_list_id (
 glstring varchar2(4000 byte) not null,
 id varchar2(512) not null,
 primary key (glstring)
);

create or alter table haplotype_id (
 glstring varchar2(4000 byte) not null,
 id varchar2(512) not null,
 primary key (glstring)
);

create or alter table genotype_id (
 glstring varchar2(4000 byte) not null,
 id varchar2(512) not null,
 primary key (glstring)
);

create or alter table genotype_list_id (
 glstring varchar2(4000 byte) not null,
 id varchar2(512) not null,
 primary key (glstring)
);

create or alter table multilocus_unphased_genotype_id (
 glstring varchar2(4000 byte) not null,
 id varchar2(512) not null,
 primary key (glstring)
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