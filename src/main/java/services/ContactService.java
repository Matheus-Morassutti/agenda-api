package services;

import exceptions.NotFoundException;
import exceptions.ValidationException;
import models.Address;
import models.Contact;
import repositories.ContactRepository;

import java.util.List;
import java.util.regex.Pattern;

public class ContactService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    private final ContactRepository contactRepository;
    private final CepService cepService;

    public ContactService(ContactRepository contactRepository, CepService cepService) {
        this.contactRepository = contactRepository;
        this.cepService = cepService;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public Contact findById(long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contact not found."));
    }

    public Contact create(Contact contact) {
        validate(contact, null);
        fillAddressFromZipCode(contact);
        return contactRepository.create(contact);
    }

    public Contact update(long id, Contact contact) {
        findById(id);
        validate(contact, id);
        fillAddressFromZipCode(contact);
        return contactRepository.update(id, contact);
    }

    /**
     * When a zip code is provided, consume the external ViaCEP API and store the
     * resolved address together with the contact. If the zip code does not exist,
     * lookup throws before persistence, so nothing is saved.
     */
    private void fillAddressFromZipCode(Contact contact) {
        if (contact.getZipCode() == null) {
            contact.setStreet(null);
            contact.setNeighborhood(null);
            contact.setCity(null);
            contact.setState(null);
            return;
        }

        Address address = cepService.lookup(contact.getZipCode());
        contact.setStreet(address.getStreet());
        contact.setNeighborhood(address.getNeighborhood());
        contact.setCity(address.getCity());
        contact.setState(address.getState());
    }

    public void delete(long id) {
        findById(id);
        contactRepository.delete(id);
    }

    private void validate(Contact contact, Long currentId) {
        if (contact == null) {
            throw new ValidationException("Contact body is required.");
        }
        contact.setName(normalizeRequired(contact.getName(), "Name is required."));
        contact.setEmail(normalizeRequired(contact.getEmail(), "Email is required.").toLowerCase());
        contact.setPhone(normalizeRequired(contact.getPhone(), "Phone is required."));

        if (!EMAIL_PATTERN.matcher(contact.getEmail()).matches()) {
            throw new ValidationException("Email format is invalid.");
        }
        if (contactRepository.existsByEmail(contact.getEmail(), currentId)) {
            throw new ValidationException("Email is already used by another contact.");
        }

        contact.setZipCode(normalizeZipCode(contact.getZipCode()));
    }

    private String normalizeZipCode(String zipCode) {
        if (zipCode == null || zipCode.isBlank()) {
            return null;
        }
        String digits = zipCode.replaceAll("\\D", "");
        if (digits.length() != 8) {
            throw new ValidationException("zipCode must have 8 digits.");
        }
        return digits;
    }

    private String normalizeRequired(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(message);
        }
        return value.trim();
    }
}
