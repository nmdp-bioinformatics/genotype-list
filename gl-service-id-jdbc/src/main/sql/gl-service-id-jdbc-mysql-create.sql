create table if not exists `sequence` (
 `sequence_name` varchar(32) unique not null,
 `sequence_value` bigint not null
) engine=innodb charset=latin1;

insert into `sequence`(`sequence_name`, `sequence_value`) values ("locus", -1);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("allele", -1);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("allele-list", -1);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("haplotype", -1);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("genotype", -1);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("genotype-list", -1);
insert into `sequence`(`sequence_name`, `sequence_value`) values ("multilocus-unphased-genotype", -1);