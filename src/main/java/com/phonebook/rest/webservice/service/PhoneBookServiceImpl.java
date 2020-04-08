package com.phonebook.rest.webservice.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.phonebook.rest.webservice.model.Contact;

@Service
public class PhoneBookServiceImpl implements IPhoneBook {

	private static List<Contact> contactList = new ArrayList<Contact>();
	private static int phoneBookCount = 3;
	static {
		contactList.add(new Contact(1, "Adam", "91-9878654352"));
		contactList.add(new Contact(2, "Eve", "91-9878654352"));
		contactList.add(new Contact(3, "Jack", "91-9878654352"));
	}
	
	@Override
	public Collection<Contact> findContacts(Optional<String> name, Optional<String> surname,
			Optional<String> phone) {
		return contactList;
	}

	@Override
	public Contact findContact(int id) {
		for(Contact contact : contactList) {
			if(contact.getId() == id) {
				return contact;
			}
		}
		return null;
	}

	@Override
	public Contact addContact(Contact contact) {
		if(contact.getId() == null) {
			contact.setId(++phoneBookCount);
		}
		contactList.add(contact);
		return contact;
	}

	@Override
	public void deleteContact(int id) {
		

	}

}
