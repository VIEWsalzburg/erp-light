
/***** [START Transaction Package] *****/
BEGIN;

/* create temporary table */
create temporary table organisation_import(name varchar(100), comment varchar(1000), address varchar(100), zip varchar(5), city varchar(100), country varchar(50),
	type_lieferant varchar(10), type_kunde varchar(10), type_sponsor varchar(10));

copy organisation_import from 'C:\Users\IBM_ADMIN\ERP-light\DatabaseModel\CSV Import-Export\working OrganisationImport\OrganisationImport.csv' with csv header delimiter ';' encoding 'UTF8';

/* insert all Addresses,Zips, Cities and Countries if they don't exist */
/* check for no address */
insert into address (address) select distinct address from organisation_import oi where
	not exists ( select address_id from address where address = oi.address ) and address != '' ;

insert into city (zip, city) select distinct zip, city from organisation_import oi where
	not exists ( select city_id from city where city = oi.city and zip = oi.zip) and ( city != '' and zip != '');

insert into country (country) select distinct country from organisation_import oi where
	not exists ( select country_id from country where country = oi.country ) and country != '';

/* check for no address */
insert into organisation( country_id, city_id, address_id, name, comment)
 select (select co.country_id from country co where co.country = oi.country limit 1), (select c.city_id from city c where c.city = oi.city and c.zip = oi.zip limit 1), 
	(select a.address_id from address a where a.address = oi.address limit 1), oi.name, coalesce(comment,'') from organisation_import oi;

/* insert type associations */

/* insert all Lieferant SQL-version 2 */
insert into relorgtype(organisation_id, type_id)
select o.organisation_id, type_id from organisation_import oi join organisation o on (oi.name = o.name)
	join type on (oi.type_lieferant != '' and type.name = 'Lieferant');

/* insert all Kunde SQL-version 2 */
insert into relorgtype(organisation_id, type_id)
select o.organisation_id, type_id from organisation_import oi join organisation o on (oi.name = o.name)
	join type on (oi.type_kunde != '' and type.name = 'Kunde');

/* insert all Kunde SQL-version 2 */
insert into relorgtype(organisation_id, type_id)
select o.organisation_id, type_id from organisation_import oi join organisation o on (oi.name = o.name)
	join type on (oi.type_sponsor != '' and type.name = 'Sponsor');


/* create temporary telephone table */
create temporary table orgcontact_import (orgname varchar(100), first_name varchar(100), last_name varchar(100));
/* copy to temporary table */
copy orgcontact_import from 'C:\Users\IBM_ADMIN\ERP-light\DatabaseModel\CSV Import-Export\working OrganisationImport\ContactPersonImport.csv' with csv header delimiter ';' encoding 'UTF8';

insert into orgcontactperson (person_id, organisation_id) select p.person_id, o.organisation_id
	from orgcontact_import oi join person p on (oi.first_name = p.first_name and oi.last_name = p.last_name) join organisation o on (oi.orgname = o.name);

COMMIT;

ROLLBACK;

select * from address;
select * from city;
select * from country;

select * from person;

select * from organisation left join address using (address_id) left join city using (city_id) left join country using (country_id);

select * from orgcontactperson oc join organisation using (organisation_id) join person p on (p.person_id = oc.person_id);

select * from relorgtype join organisation using (organisation_id) join type using (type_id);

drop table organisation_import;
drop table orgcontact_import;

/***** [END Transaciton Package] *****/

