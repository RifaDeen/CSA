/**
 * Data Access Object (DAO) class for managing Person entities.
 * This class provides methods for CRUD operations on person objects.
 * Author: Rifa
 * IIT no: 20220701
 */
package daoClasses;

import exceptionClasses.AlreadyExistsException;
import exceptionClasses.NotFoundException;
import modelClasses.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PersonDAO {

    // Logger for logging messages
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonDAO.class);
    // In-memory storage for Person objects with their IDs
    private static Map<Integer, Person> personMap = new HashMap<>();
    // Counter for generating unique IDs
    private static AtomicInteger idCount = new AtomicInteger(0);

    // Retrieve all Person objects stored in the database and return Map of Person objects with their IDs
    public static Map<Integer, Person> getPersons() {
        return personMap;
    }
    
    // Generate a unique ID for a new Person object and return Unique ID.
    public static int generateId() {
        int newId = idCount.incrementAndGet();
        return newId;
    }
    
    /**
     * Add a new Person object to the database.
     * @param person Person object to be added
     */
    public static void addPerson(Person person) {
        try {
            
            // Check if the person with the same ID already exists
            if (personExists(person.getId())) {
                throw new AlreadyExistsException("Person with ID " + person.getId() + " already exists");
            }
            // Generate a new ID for the person
            int newId = generateId();
            person.setId(newId);
            
            // Add the person to the database
            personMap.put(newId, person);
            LOGGER.info("Added new person successfully: {}", person);
        } catch (AlreadyExistsException e) {
            // error handling if person already exists
            LOGGER.error("Failed to add person: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            //other errors
            LOGGER.error("Failed to add person: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add person", e);
        }
    }

     /**
     * Retrieve a Person object by its ID.
     * @param id ID of the Person to retrieve
     * @return Retrieved Person object
     */
    public static Person getPersonById(int id) {
        //check if person exists
        if (personExists(id)) {
            return personMap.get(id);
        } else {
            LOGGER.warn("Could not find person: {}", id);
            throw new NotFoundException("Person not found for ID: " + id);
        }
    }

    /**
     * Update an existing Person object in the database.
     * @param id ID of the Person to update
     * @param updatedPerson Updated Person object
     */
    public static void updatePerson(int id, Person updatedPerson) {
        try {
            //check if person exists
            if (!personExists(id)) {
                throw new NotFoundException("Person not found for ID: " + id);
            }
            //set id and udate in map
            updatedPerson.setId(id);
            personMap.put(id, updatedPerson);
            LOGGER.info("Updated person successfully: {}", updatedPerson);
        } catch (NotFoundException e) {
            //error handling for not found
            LOGGER.warn("Failed to update person: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to update person: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update person", e);
        }
    }

    /**
     * Delete a Person object from the database by its ID.
     * @param id ID of the Person to delete
     */
    public static void deletePerson(int id) {
        try {
            if (!personExists(id)) {
                throw new NotFoundException("Person not found for ID: " + id);
            }
            personMap.remove(id);
            LOGGER.info("Deleted person successfully with ID: {}", id);
        } catch (NotFoundException e) {
            LOGGER.warn("Person not found for Id: {} to delete", id);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to delete person: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete person", e);
        }
    }

    /**
     * Check if a Person with the given ID exists in the database.
     * @param id ID of the Person to check
     * @return True if the person exists, otherwise False
     */
    public static boolean personExists(int id) {
        return personMap.containsKey(id);
    }

    /**
     * Validate a Person object.
     * This method checks if the Person object is not null and has valid attributes.
     * @param person Person object to validate
     * @return True if the Person is valid, otherwise False
     */
    public static boolean isValidPerson(Person person) {
        return person != null && person.getName() != null && !person.getName().isEmpty() && person.getContact() != null && !person.getContact().isEmpty() && person.getAddress() != null && !person.getAddress().isEmpty();
    }
}
