package com.phonebook.rest.webservice.service;

import java.util.Collection;
import java.util.Optional;

import com.phonebook.rest.webservice.model.Contact;


public interface IPhoneBook {
	/**
     * If all the optional parameters are empty, will return a collection with all the phonebook's contacts.
     * <p/>
     * If there are non-empty parameters, will return a collection with the contacts that match these parameters.
     *
     * @param name - optional parameter that when not empty will be used to filter contacts with this specific name.
     * @param surname - optional parameter that when not empty will be used to filter contacts with this specific surname.
     * @param phone - optional parameter that when not empty will be used to filter contacts with this specific phone.
     * @return all contacts if all parameters are empty, or the contacts that match the non empty parameters.
     */
	public Collection<Contact> findContacts(Optional<String> name, Optional<String> surname, Optional<String> phone);

    /**
     * Will return a contact with the specific id, or an empty optional if no match could be made.
     *
     * @param id - the id of the contact that should be returned.
     * @return a contact with the specific id, or an empty optional if no match could be made.
     */
    public Contact findContact(int id);

    /**
     * Will add a contact in the phone book. The specific {@link IPhoneBook} implementation will handle the indexing of the contact,
     * and make sure it provides a valid id to it.
     *
     * @param contact - the contact that will be added in the phone book. No id information should be given here.
     * @return an {@link Contact} with the same details as the {@link Contact} provided, but also the id given by the phone book.
     */
    public Contact addContact(Contact contact);

    /**
     * Will delete the {@link Contact} from the phone book.
     *
     * @param contact - the contact to be deleted.
     */
    public void deleteContact(int id);
}
