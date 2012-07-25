create table if not exists `locus_id` (
 `glstring` varchar(32768) not null,
 `id` varchar(512) not null,
 primary key (`glstring`)
) engine=myisam charset=latin1;

create table if not exists `allele_id` (
 `glstring` varchar(32768) not null,
 `id` varchar(512) not null,
 primary key (`glstring`)
) engine=myisam charset=latin1;

create table if not exists `allele_list_id` (
 `glstring` varchar(32768) not null,
 `id` varchar(512) not null,
 primary key (`glstring`)
) engine=myisam charset=latin1;

create table if not exists `haplotype_id` (
 `glstring` varchar(32768) not null,
 `id` varchar(512) not null,
 primary key (`glstring`)
) engine=myisam charset=latin1;

create table if not exists `genotype_id` (
 `glstring` varchar(32768) not null,
 `id` varchar(512) not null,
 primary key (`glstring`)
) engine=myisam charset=latin1;

create table if not exists `genotype_list_id` (
 `glstring` varchar(32768) not null,
 `id` varchar(512) not null,
 primary key (`glstring`)
) engine=myisam charset=latin1;

create table if not exists `multilocus_unphased_genotype_id` (
 `glstring` varchar(32768) not null,
 `id` varchar(512) not null,
 primary key (`glstring`)
) engine=myisam charset=latin1;

create table if not exists `locus` (
 `id` varchar(512) not null,
 `locus` blob not null,
 primary key (`id`)
) engine=myisam charset=latin1;

create table if not exists `allele` (
 `id` varchar(512) not null,
 `allele` blob not null,
 primary key (`id`)
) engine=myisam charset=latin1;

create table if not exists `allele_list` (
 `id` varchar(512) not null,
 `allele_list` blob not null,
 primary key (`id`)
) engine=myisam charset=latin1;

create table if not exists `haplotype` (
 `id` varchar(512) not null,
 `haplotype` blob not null,
 primary key (`id`)
) engine=myisam charset=latin1;

create table if not exists `genotype` (
 `id` varchar(512) not null,
 `genotype` blob not null,
 primary key (`id`)
) engine=myisam charset=latin1;

create table if not exists `genotype_list` (
 `id` varchar(512) not null,
 `genotype_list` blob not null,
 primary key (`id`)
) engine=myisam charset=latin1;

create table if not exists `multilocus_unphased_genotype` (
 `id` varchar(512) not null,
 `multilocus_unphased_genotype` blob not null,
 primary key (`id`)
) engine=myisam charset=latin1;