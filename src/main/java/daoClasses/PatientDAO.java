/**
 * Data Access Object (DAO) class for managing Patient entities.
 * This class provides methods for CRUD operations on patient objects.
 * Author: Rifa
 * IIT no: 20220701
 */
package daoClasses;

import exceptionClasses.NotFoundException;
import modelClasses.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PatientDAO {

    // Logger for logging messages
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDAO.class);

    // In-memory storage for Patient objects with their IDs
    private static Map<Integer, Patient> patientMap = new HashMap<>();

    /**
     * Retrieve all Patient objects stored in the database.
     * @return Map of Patient objects with their IDs
     */
    public static Map<Integer, Patient> getPatients() {
        return patientMap;
    }

    /**
     * Add a new Patient object to the database.
     * @param patient Patient object to be added
     */
    public static void addPatient(Patient patient) {
        try {
            // Generate a new ID for the patient
            int newId = PersonDAO.generateId();
            patient.setId(newId);
            // Add the patient to the database
            patientMap.put(newId, patient);
            LOGGER.info("Added new patient successfully: {}", patient);
        } catch (Exception e) {
            // Log any unexpected errors
            LOGGER.error("Failed to add patient: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add patient", e);
        }
    }

    /**
     * Retrieve a Patient object by its ID.
     * @param id ID of the Patient to retrieve
     * @return Retrieved Patient object
     */
    public static Patient getPatientById(int id) {
        // Check if the patient exists in the database
        if (patientExists(id)) {
            return patientMap.get(id);
        } else {
            // Log a warning if patient not found
            LOGGER.warn("Patient not found for ID: {}", id);
            throw new NotFoundException("Patient not found for ID: " + id);
        }
    }

    /**
     * Update an existing Patient object in the database.
     * @param id ID of the Patient to update
     * @param updatedPatient Updated Patient object
     */
    public static void updatePatient(int id, Patient updatedPatient) {
        try {
            // Check if the patient exists in the database
            if (!patientExists(id)) {
                throw new NotFoundException("Patient not found for ID: " + id);
            }
            // Set the ID of the updated patient
            updatedPatient.setId(id);
            // Update the patient in the database
            patientMap.put(id, updatedPatient);
            LOGGER.info("Updated patient successfully: {}", updatedPatient);
        } catch (NotFoundException e) {
            // Log a warning if patient not found for update
            LOGGER.warn("Failed to update patient: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // Log any other unexpected errors
            LOGGER.error("Failed to update patient: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update patient", e);
        }
    }

    /**
     * Delete a Patient object from the database by its ID.
     * @param id ID of the Patient to delete
     */
    public static void deletePatient(int id) {
        try {
            // Check if the patient exists in the database
            if (!patientExists(id)) {
                throw new NotFoundException("Patient not found for ID: " + id);
            }
            // Remove the patient from the database
            patientMap.remove(id);
            LOGGER.info("Deleted patient successfully with ID: {}", id);
        } catch (NotFoundException e) {
            // Log a warning if patient not found for deletion
            LOGGER.warn("Failed to delete patient: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // Log any other unexpected errors
            LOGGER.error("Failed to delete patient: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete patient", e);
        }
    }

    /**
     * Check if a Patient with the given ID exists in the database.
     * @param id ID of the Patient to check
     * @return True if the patient exists, otherwise False
     */
    public static boolean patientExists(int id) {
        return patientMap.containsKey(id);
    }

    /**
     * Check if a Person with the given ID exists in the database.
     * @param id ID of the Person to check
     * @return True if the person exists, otherwise False
     */
    public static boolean patientIsPerson(int id) {
        return PersonDAO.personExists(id);
    }

    /**
     * Validate a Patient object.
     * This method checks if the Patient object is not null and has valid attributes.
     * @param patient Patient object to validate
     * @return True if the Patient is valid, otherwise False
     */
    public static boolean isValidPatient(Patient patient) {
        return patient != null && patient.getName() != null && !patient.getName().isEmpty()
                && patient.getContact() != null && !patient.getContact().isEmpty()
                && patient.getAddress() != null && !patient.getAddress().isEmpty()
                && patient.getMedicalHistory() != null && !patient.getMedicalHistory().isEmpty()
                && patient.getHealthStatus() != null && !patient.getHealthStatus().isEmpty();
    }
}
