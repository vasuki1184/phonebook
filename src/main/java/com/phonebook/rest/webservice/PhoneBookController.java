package com.phonebook.rest.webservice;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phonebook.rest.webservice.exceptions.ContactNotFoundException;
import com.phonebook.rest.webservice.exceptions.IllegalContactException;
import com.phonebook.rest.webservice.model.Contact;
import com.phonebook.rest.webservice.service.IPhoneBook;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/phonebook/contacts")
public class PhoneBookController {

	@Autowired
    private IPhoneBook phoneBook;

    /**
     * Exposes the URI "phonebook/contacts" and "listens" for GET reqeusts. If the request does not contain any more parameters,
     * the method will return all contacts that exist in the phonebook. If some of the optional parameters "name", "surname" or "phone" exist in the HTTP request,
     * it will return the contacts matching these values.
     *
     * @param name - the name to be looked up in phonebook contacts.
     * @param surname - the surname to be looked up in phonebook contacts.
     * @param phone - the phone to be looked up in phonebook contacts.
     * @return a collection of contacts that match with the name, surname and/or phone provided by the request. If no parameters are provided returns all the contacts in the phonebook.
     */
	@ApiOperation("Search inside the phone book contacts by name, surname or phone. If you provide no search criteria all the contacts will be returned.")
    @RequestMapping(method=RequestMethod.GET, produces="application/json")
    public Collection<Contact> getContacts(@ApiParam(value="Search contacts by name") @RequestParam(value="name", required=false) String name,
                                                  @ApiParam(value="Search contacts by surname") @RequestParam(value="surname", required=false) String surname,
                                                  @ApiParam(value="Search contacts by phone") @RequestParam(value="phone", required=false) String phone) {
        return phoneBook.findContacts(Optional.ofNullable(name), Optional.ofNullable(surname), Optional.ofNullable(phone));
    }

    /**
     * Searches in phonebook for a contact that maches the GET reqeust's id and if such a contact exists, returns it.
     *
     * @param id - the contact id to be looked up in the phone book.
     * @return the contact that matches request's id, if such contact exists.
     * @throws ContactNotFoundException if no contact is found with this id.
     */
	@ApiOperation("Get a single contact using its id.")
    @RequestMapping(value="{id}", method=RequestMethod.GET, produces="application/json")
    public Contact getContact(@ApiParam(value="The id of the contact to be retrieved") @PathVariable String id) throws ContactNotFoundException {
		Contact contact = phoneBook.findContact(Integer.valueOf(id));
		if(contact == null) new ContactNotFoundException(" "+id);
        return contact;
    }

    /**
     * Adds the contact included in the request body to the phone book. The {@link Contact} provided by the client
     * will contain no indexing, but just the name, surname and phone details. It does not matter if there is already
     * another client with the same details. The new client will be provided with a unique id and be added in
     * the phone book.
     *
     * @param contact - the contact to be added in the phone book.
     * @return an {@link IndexedContact}, which has the same details with the contact provided, but also the unique id that identifies it in the phone book.
     */
	@ApiOperation("Add an un-indexed contact to the phone book. Provide just name, surname and phone the the phone book will handle its indexing.")
    @RequestMapping(method=RequestMethod.POST, consumes="application/json", produces="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Contact addContact(@ApiParam(value="The contact to be added in the phone book") @RequestBody Contact contact) {
        return phoneBook.addContact(contact);
    }

    /**
     * Adds the {@link IndexedContact} in the request body to the phone book. If there already exists a contact
     * under the specific URI it will update the contact. For this reason the URI's id and the {@link IndexedContact}'s id
     * should be the same. If there is not contact yet, it will create it with
     * the request body.
     *
     * @param id - the id that identifies the resource (contact) to be added.
     * @param indexedContact - the contact to be added under the specific URI.
     * @return the contact added to the phone book.
     * @throws IllegalContactException if the URI's id and the {@link IndexedContact}'s id are not the same.
     */
	@ApiOperation("Add an indexed contact to the phone book. If the id belongs to an older contact, the contact will be replaced.")
    @RequestMapping(value="{id}", method=RequestMethod.PUT, consumes="application/json", produces="application/json")
    public Contact addContact(@ApiParam(value="The id of the contact to be added") @PathVariable String id,
                                     @ApiParam(value="The contact to be added, or replace the older one with the same id") @RequestBody Contact contact) throws IllegalContactException {
        if (contact.getId() == null || !id.equals(contact.getId().toString())) {
            throw new IllegalContactException("The contact's id should be the same with the URI's id.");
        }
        return phoneBook.addContact(contact);
    }

    /**
     * Deletes the contact under the specific URI.
     *
     * @param id - the id of the contact to be deleted.
     */
	@ApiOperation("Delete a contact from the phone book using its id.")
    @RequestMapping(value="{id}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@ApiParam(value="The id of the contact to be deleted") @PathVariable String id) {
        phoneBook.deleteContact(Integer.valueOf(id));
    }

}
