delete from brand;
delete from color;
delete from country;
delete from model;
delete from transmission;
delete from fuel;
delete from car;

insert into brand(id, name) values
(1, 'Audi'),
(2, 'Ford');

insert into color(id, name) values
(1, 'White'),
(2, 'Red');

insert into country(id, name) values
(1, 'Belarus'),
(2, 'USA');

insert into model(id, name) values
(1, 'Minivan'),
(2, 'Sport');

insert into transmission(id, type) values
(1, 'Automatic'),
(2, 'Manual');

insert into fuel(id, type) values
(1, 'Diesel'),
(2, 'Petrol');

insert into car(id, price, year_of_issue, mileage, color_id, country_id, brand_id, transmission_id, model_id, fuel_id, file_name) values
(1, 100, 2000, 100, 1, 1, 1, 1, 1, 1, ''),
(2, 200, 2001, 200, 1, 1, 1, 1, 1, 1, ''),
(3, 300, 2010, 300, 2, 2, 2, 2, 2, 2, ''),
(4, 400, 2011, 400, 2, 2, 2, 2, 2, 2, '');