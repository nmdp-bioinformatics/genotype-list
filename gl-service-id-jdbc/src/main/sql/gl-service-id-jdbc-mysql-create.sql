create table if not exists `sequence` (
 `sequence_name` varchar(32) unique not null,
 `sequence_value` bigint unsigned not null
) engine=innodb charset=latin1;

insert into `sequence`(`sequence_name`, `sequence_value`) values ("locus", 0);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("allele", 0);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("allele-list", 0);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("haplotype", 0);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("genotype", 0);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("genotype-list", 0);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("multilocus-unphased-genotype", 0);