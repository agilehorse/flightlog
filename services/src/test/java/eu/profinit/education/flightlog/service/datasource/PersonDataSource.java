package eu.profinit.education.flightlog.service.datasource;

import eu.profinit.education.flightlog.domain.entities.Address;
import eu.profinit.education.flightlog.domain.entities.Person;
import eu.profinit.education.flightlog.to.AddressTo;
import eu.profinit.education.flightlog.to.PersonTo;

public class PersonDataSource {

    public static PersonTo guestPersonTo = PersonTo.builder()
        .firstName("Karel")
        .lastName("Novák")
        .address(AddressTo.builder()
            .street("Uliční ulice 550")
            .city("Městské město")
            .postalCode("99090")
            .build())
        .build();

    public static PersonTo clubPersonTo = PersonTo.builder()
        .memberId(1L)
        .firstName("Pavel")
        .lastName("Dvořák")
        .address(AddressTo.builder()
            .street("Konečná 666")
            .city("Brno Venkov")
            .postalCode("97066")
            .build())
        .build();

    public static PersonTo guestPersonTo2 = PersonTo.builder()
        .firstName("Pavlína")
        .lastName("Zelená")
        .address(AddressTo.builder()
            .street("Univerzitní ulice 550")
            .city("Ostrava")
            .postalCode("89090")
            .build())
        .build();

    public static PersonTo clubPersonTo2 = PersonTo.builder()
        .memberId(2L)
        .firstName("Karla")
        .lastName("Kolomazníková")
        .address(AddressTo.builder()
            .street("Zelená 676")
            .city("Hradec Králové")
            .postalCode("10102")
            .build())
        .build();


    public static Person guestPerson = new Person(11L, Person.Type.GUEST, "Karel", "Novák",
        new Address("Uliční ulice 550", "Městské město", "99090", null),null);

    public static Person clubPerson = new Person(11L, Person.Type.GUEST, "Karla", "Kolomazníková",
        new Address("Zelená 676", "Hradec Králové", "10102", null),2L);
}
