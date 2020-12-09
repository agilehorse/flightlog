insert into c_airplane_type(id, type, max_capacity) values (1, 'WT-9 Dynamic', 2);
insert into c_airplane_type(id, type, max_capacity) values (2, 'Zlín Z-42M', 1);
insert into c_airplane_type(id, type, max_capacity) values (3, 'L-13A Blaník', 2);
insert into c_airplane_type(id, type, max_capacity) values (4, 'ASW 15 B', 1);

insert into c_club_airplane(id, immatriculation, type_id, archived) values (1, 'OK-V23424', 2, false);
insert into c_club_airplane(id, immatriculation, type_id, archived) values (2, 'OK-B123', 3, false);

insert into t_person(id, person_type, member_id, first_name, last_name, street, city, postal_code, country) values (1, 'CLUB_MEMBER',1, 'Kamila', 'Spoustová', 'Ulice 678', 'Město ABCD', '82828', null);
insert into t_person(id, person_type, member_id, first_name, last_name, street, city, postal_code, country) values (2, 'CLUB_MEMBER', 2, 'Naděžda', 'Pavelková', 'Ulice dlouhá 678', 'Město CDGF', '82828', null);
insert into t_person(id, person_type ,member_id, first_name, last_name, street, city, postal_code, country) values (3, 'GUEST', 2, 'Řehoř', 'Novák', 'Ulice krátká 765', 'Karlov', '82828', null);


insert into t_flight(id, flight_type, club_airplane_id, guest_airplane_immatriculation, guest_airplane_type, takeoff_time, landing_time, task, pilot_person_id, copilot_person_id, note) values (2, 'GLIDER', 2, null, null, (TIMESTAMP '2018-10-23 13:30:00'), null,'',1,null,'Note A');
insert into t_flight(id, flight_type, club_airplane_id, guest_airplane_immatriculation, guest_airplane_type, takeoff_time, landing_time, task, pilot_person_id, copilot_person_id, note) values (1, 'TOWPLANE', 1, null, null, (TIMESTAMP '2018-10-23 13:30:00'), null,'VLEK',1,null,'Note');
insert into t_flight(id, flight_type, club_airplane_id, guest_airplane_immatriculation, guest_airplane_type, takeoff_time, landing_time, task, pilot_person_id, copilot_person_id, note) values (3, 'TOWPLANE', 1, null, null, (TIMESTAMP '2018-10-24 17:30:00'), (TIMESTAMP '2018-10-23 19:30:00'),'VLEK',3,1,'Note C');
insert into t_flight(id, flight_type, club_airplane_id, guest_airplane_immatriculation, guest_airplane_type, takeoff_time, landing_time, task, pilot_person_id, copilot_person_id, note) values (4, 'GLIDER', 2, null, null, (TIMESTAMP '2018-10-24 17:30:00'), (TIMESTAMP '2018-10-23 17:30:00'),'1A',2,3,'Note B');
insert into t_flight(id, flight_type, club_airplane_id, guest_airplane_immatriculation, guest_airplane_type, takeoff_time, landing_time, task, pilot_person_id, copilot_person_id, note) values (5, 'TOWPLANE', null , 'OK-123', 'KKB-15', (TIMESTAMP '2018-10-22 12:30:00'), null,'',1,null,'Note D');

update t_flight set glider_flight_id = 2 where id = 1;
update t_flight set towplane_flight_id = 1 where id = 2;
update t_flight set glider_flight_id = 4 where id = 3;
update t_flight set towplane_flight_id = 3 where id = 4;

commit;