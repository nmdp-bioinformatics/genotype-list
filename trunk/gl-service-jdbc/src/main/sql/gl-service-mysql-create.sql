create table if not exists `locus_id` (
 `locus_id_pk` bigint not null auto_increment,
 `glstring` blob not null,
 `glstring_hash` varbinary(64) not null,
 `id` varchar(512) not null,
 primary key (`locus_id_pk`),
 key (`glstring_hash`)
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `locus` (
 `locus_pk` bigint not null auto_increment,
 `id` varchar(512) not null,
 `locus` blob not null,
 primary key (`locus_pk`),
 key (`id`(512))
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `allele_id` (
 `allele_id_pk` bigint not null auto_increment,
 `glstring` blob not null,
 `glstring_hash` varbinary(64) not null,
 `id` varchar(512) not null,
 primary key (`allele_id_pk`),
 key (`glstring_hash`)
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `allele` (
 `allele_pk` bigint not null auto_increment,
 `id` varchar(512) not null,
 `allele` blob not null,
 primary key (`allele_pk`),
 key (`id`(512))
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `allele_list_id` (
 `allele_list_id_pk` bigint not null auto_increment,
 `glstring` blob not null,
 `glstring_hash` varbinary(64) not null,
 `id` varchar(512) not null,
 primary key (`allele_list_id_pk`),
 key (`glstring_hash`)
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `allele_list` (
 `allele_list_pk` bigint not null auto_increment,
 `id` varchar(512) not null,
 `allele_list` blob not null,
 primary key (`allele_list_pk`),
 key (`id`(512))
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `haplotype_id` (
 `haplotype_id_pk` bigint not null auto_increment,
 `glstring` blob not null,
 `glstring_hash` varbinary(64) not null,
 `id` varchar(512) not null,
 primary key (`haplotype_id_pk`),
 key (`glstring_hash`)
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `haplotype` (
 `haplotype_pk` bigint not null auto_increment,
 `id` varchar(512) not null,
 `haplotype` blob not null,
 primary key (`haplotype_pk`),
 key (`id`(512))
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `genotype_id` (
 `genotype_id_pk` bigint not null auto_increment,
 `glstring` blob not null,
 `glstring_hash` varbinary(64) not null,
 `id` varchar(512) not null,
 primary key (`genotype_id_pk`),
 key (`glstring_hash`)
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `genotype` (
 `genotype_pk` bigint not null auto_increment,
 `id` varchar(512) not null,
 `genotype` blob not null,
 primary key (`genotype_pk`),
 key (`id`(512))
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `genotype_list_id` (
 `genotype_list_id_pk` bigint not null auto_increment,
 `glstring` blob not null,
 `glstring_hash` varbinary(64) not null,
 `id` varchar(512) not null,
 primary key (`genotype_list_id_pk`),
 key (`glstring_hash`)
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `genotype_list` (
 `genotype_list_pk` bigint not null auto_increment,
 `id` varchar(512) not null,
 `genotype_list` blob not null,
 primary key (`genotype_list_pk`),
 key (`id`(512))
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `multilocus_unphased_genotype_id` (
 `multilocus_unphased_genotype_id_pk` bigint not null auto_increment,
 `glstring` blob not null,
 `glstring_hash` varbinary(64) not null,
 `id` varchar(512) not null,
 primary key (`multilocus_unphased_genotype_id_pk`),
 key (`glstring_hash`)
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `multilocus_unphased_genotype` (
 `multilocus_unphased_genotype_pk` bigint not null auto_increment,
 `id` varchar(512) not null,
 `multilocus_unphased_genotype` blob not null,
 primary key (`multilocus_unphased_genotype_pk`),
 key (`id`(512))
) engine=innodb charset=latin1 auto_increment=1;
