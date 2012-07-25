create table if not exists `locus_id` (
 `locus_id` bigint not null auto_increment,
 `glstring` varchar(32768) not null,
 `glstring_hash_code` integer not null,
 `id` varchar(512) not null,
 primary key (`locus_id`),
 key (`glstring`(512)),
 key (`glstring_hash_code`)
) engine=myisam charset=latin1 auto_increment=1;

create table if not exists `allele_id` (
 `allele_id` bigint not null auto_increment,
 `glstring` varchar(32768) not null,
 `glstring_hash_code` integer not null,
 `id` varchar(512) not null,
 primary key (`glstring`),
 key (`glstring`(512)),
 key (`glstring_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `allele_list_id` (
 `allele_list_id` bigint not null auto_increment,
 `glstring` varchar(32768) not null,
 `glstring_hash_code` integer not null,
 `id` varchar(512) not null,
 primary key (`glstring`),
 key (`glstring`(512)),
 key (`glstring_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `haplotype_id` (
 `haplotype_id` bigint not null auto_increment,
 `glstring` varchar(32768) not null,
 `glstring_hash_code` integer not null,
 `id` varchar(512) not null,
 primary key (`glstring`),
 key (`glstring`(512)),
 key (`glstring_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `genotype_id` (
 `genotype_id` bigint not null auto_increment,
 `glstring` varchar(32768) not null,
 `glstring_hash_code` integer not null,
 `id` varchar(512) not null,
 primary key (`glstring`),
 key (`glstring`(512)),
 key (`glstring_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `genotype_list_id` (
 `genotype_list_id` bigint not null auto_increment,
 `glstring` varchar(32768) not null,
 `glstring_hash_code` integer not null,
 `id` varchar(512) not null,
 primary key (`glstring`),
 key (`glstring`(512)),
 key (`glstring_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `multilocus_unphased_genotype_id` (
 `multilocus_unphased_genotype_id` bigint not null auto_increment,
 `glstring` varchar(32768) not null,
 `glstring_hash_code` integer not null,
 `id` varchar(512) not null,
 primary key (`glstring`),
 key (`glstring`(512)),
 key (`glstring_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `locus` (
 `locus_id` bigint not null auto_increment,
 `id` varchar(512) not null,
 `id_hash_code` integer not null,
 `locus` blob not null,
 primary key (`locus_id`),
 key (`id`(512)),
 key (`id_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `allele` (
 `allele_id` bigint not null auto_increment,
 `id` varchar(512) not null,
 `id_hash_code` integer not null,
 `allele` blob not null,
 primary key (`allele_id`),
 key (`id`(512)),
 key (`id_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `allele_list` (
 `allele_list_id` bigint not null auto_increment,
 `id` varchar(512) not null,
 `id_hash_code` integer not null,
 `allele_list` blob not null,
 primary key (`allele_list_id`),
 key (`id`(512)),
 key (`id_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `haplotype` (
 `haplotype_id` bigint not null auto_increment,
 `id` varchar(512) not null,
 `id_hash_code` integer not null,
 `haplotype` blob not null,
 primary key (`haplotype_id`),
 key (`id`(512)),
 key (`id_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `genotype` (
 `genotype_id` bigint not null auto_increment,
 `id` varchar(512) not null,
 `id_hash_code` integer not null,
 `genotype` blob not null,
 primary key (`genotype_id`),
 key (`id`(512)),
 key (`id_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `genotype_list` (
 `genotype_list_id` bigint not null auto_increment,
 `id` varchar(512) not null,
 `id_hash_code` integer not null,
 `genotype_list` blob not null,
 primary key (`genotype_list_id`),
 key (`id`(512)),
 key (`id_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;

create table if not exists `multilocus_unphased_genotype` (
 `multilocus_unphased_genotype_id` bigint not null auto_increment,
 `id` varchar(512) not null,
 `id_hash_code` integer not null,
 `multilocus_unphased_genotype` blob not null,
 primary key (`multilocus_unphased_genotype_id`),
 key (`id`(512)),
 key (`id_hash_code`)
) engine=myisam charset=latin1 auto-increment=1;