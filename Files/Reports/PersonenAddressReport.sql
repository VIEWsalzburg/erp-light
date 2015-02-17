select * from relorgcat;

select * from organisation left join relorgcat using (organisation_id) left join category using (category_id);


/* Report for getting every person with private address and if contact person with organisation address */
create view personAddressReportView as

select 
	p.salutation as "salutation",
	p.title as "title",
	p.first_name as "firstName",
	p.last_name as "lastName",
	pa.address as "privateAddress",
	pc.zip as "privateZip",
	pc.city as "privateCity",
	pcountry.country as "privateCountry",
	o.name as "orgName",
	ot.name as "orgType",
	oa.address as "orgAddress",
	oc.zip as "orgZip",
	oc.city as "orgCity",
	ocountry.country as "orgCountry"
	
 from person p left join orgcontactperson using (person_id)
	left join organisation o using (organisation_id)
	left join relorgtype on (relorgtype.organisation_id = o.organisation_id)
	left join type ot on (ot.type_id = relorgtype.type_id)
	left join address pa on (p.address_id = pa.address_id)
	left join address oa on (o.address_id = oa.address_id)
	left join city pc on (p.city_id = pc.city_id)
	left join city oc on (o.city_id = oc.city_id)
	left join country pcountry on (p.country_id = pcountry.country_id)
	left join country ocountry on (o.country_id = ocountry.country_id)
order by p.last_name;



select * from type;



select p.salutation, p.title, p.last_name, p.first_name, from person p left join orgcontactperson using (person_id)
	left join organisation using (organisation_id)
	left join address using (address_id);