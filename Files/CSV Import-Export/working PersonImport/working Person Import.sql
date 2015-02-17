
/***** [START Transaction Package] *****/
BEGIN;

/* create temporary table */
create temporary table person_import(salutation varchar(20), title varchar(20), first_name varchar(100),
	last_name varchar(100), comment varchar(1000), address varchar(100), zip varchar(5), city varchar(100),
	country varchar(50), type_mitarbeiter varchar(10), type_unterstuetzer varchar(10), type_mitglied varchar(10), type_gast varchar(10));

copy person_import from 'C:\Users\IBM_ADMIN\ERP-light\DatabaseModel\CSV Import-Export\To Import\PersonImport.csv' with csv header delimiter ';' encoding 'UTF8';

/* insert all Addresses,Zips, Cities and Countries if they don't exist */
/* check for no address */
insert into address (address) select distinct address from person_import pi where
	not exists ( select address_id from address where address = pi.address ) and address != '' ;

insert into city (zip, city) select distinct zip, city from person_import pi where
	not exists ( select city_id from city where city = pi.city and zip = pi.zip) and ( city != '' and zip != '');

insert into country (country) select distinct country from person_import pi where
	not exists ( select country_id from country where country = pi.country ) and country != '';

/* check for no address */
insert into person( country_id, city_id, address_id, salutation, title, first_name, last_name, comment)
 select (select co.country_id from country co where co.country = pi.country limit 1), (select c.city_id from city c where c.city = pi.city and c.zip = pi.zip limit 1), 
	(select a.address_id from address a where a.address = pi.address limit 1), coalesce(salutation, ''), coalesce(title,''), coalesce(first_name,''),
	last_name, coalesce(comment,'') from person_import pi;

/* insert type associations for persons */

/* insert all Mitarbeiter SQL-version 2 */
insert into relpersontype(person_id, type_id)
select p.person_id, type_id from person_import pi join person p on (pi.last_name = p.last_name and pi.first_name = p.first_name)
	join type on (pi.type_mitarbeiter != '' and type.name = 'Mitarbeiter');

/* insert all Unterstützer SQL-version 2 */
insert into relpersontype(person_id, type_id)
select p.person_id, type_id from person_import pi join person p on (pi.last_name = p.last_name and pi.first_name = p.first_name)
	join type on (pi.type_unterstuetzer != '' and type.name = 'Unterstützer');

/* insert all Mitglied SQL-version 2 */
insert into relpersontype(person_id, type_id)
select p.person_id, type_id from person_import pi join person p on (pi.last_name = p.last_name and pi.first_name = p.first_name)
	join type on (pi.type_mitglied != '' and type.name = 'Mitglied');

/* insert all Gast SQL-version 2 */
insert into relpersontype(person_id, type_id)
select p.person_id, type_id from person_import pi join person p on (pi.last_name = p.last_name and pi.first_name = p.first_name)
	join type on (pi.type_gast != '' and type.name = 'Gast');


/* create temporary telephone table */
create temporary table telephone_import (first_name varchar(100), last_name varchar(100), telephone varchar(20), type varchar(20));
/* copy to temporary table */
copy telephone_import from 'C:\Users\IBM_ADMIN\ERP-light\DatabaseModel\CSV Import-Export\To Import\TelephoneImport.csv' with csv header delimiter ';' encoding 'UTF8';

/* insert telephones into table */
insert into telephone (person_id, type_id, telephone)
select p.person_id, t.type_id, ti.telephone from telephone_import ti join person p on (ti.last_name = p.last_name and ti.first_name = p.first_name)
	join type t on( (ti.type LIKE 'p%' and t.name = 'privat') or (ti.type LIKE 'g%' and t.name = 'geschäftlich') );

/* create temporary email table */
create temporary table email_import (first_name varchar(100), last_name varchar(100), email varchar(20), type varchar(20));
/* copy to temporary table */
copy email_import from 'C:\Users\IBM_ADMIN\ERP-light\DatabaseModel\CSV Import-Export\To Import\EmailImport.csv' with csv header delimiter ';' encoding 'UTF8';

/* insert telephones into table */
insert into email (person_id, type_id, email)
select p.person_id, t.type_id, ei.email from email_import ei join person p on (ei.last_name = p.last_name and ei.first_name = p.first_name)
	join type t on( (ei.type LIKE 'p%' and t.name = 'privat') or (ei.type LIKE 'g%' and t.name = 'geschäftlich') );

COMMIT;

ROLLBACK;

select * from address;
select * from city;
select * from country;

select * from telephone join person using (person_id);

select * from person;

drop table person_import;
drop table telephone_import;
drop table email_import;

/***** [END Transaciton Package] *****/

