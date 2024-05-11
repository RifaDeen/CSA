/**
 * Data Access Object (DAO) class for managing Doctor entities.
 * This class provides methods for CRUD operations on Doctor objects.
 * Author: rifa
 * IIT no: 20220701
 */
package daoClasses;

import exceptionClasses.NotFoundException;
import modelClasses.Doctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class DoctorDAO {

    // Logger for logging messages
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDAO.class);

    // In-memory storage for Doctor objects with their IDs
    private static Map<Integer, Doctor> doctorMap = new HashMap<>();

    /**
     * Retrieve all Doctor objects stored in the database and return Map of Doctor objects with their IDs
     */
    public static Map<Integer, Doctor> getDoctors() {
        return doctorMap;
    }

    /**
     * Add a new Doctor object to the database.
     * @param doctor Doctor object to be added
     */
    public static void addDoctor(Doctor doctor) {
        try {
            // Generate a new ID for the doctor
            int newId = PersonDAO.generateId();
            doctor.setId(newId);
            // Add the doctor to the database
            doctorMap.put(newId, doctor);
            LOGGER.info("Added new doctor successfully: {}", doctor);
        } catch (Exception e) {
            // Log any unexpected errors
            LOGGER.error("Failed to add doctor: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add doctor", e);
        }
    }

    /**
     * Retrieve a Doctor object by its ID.
     * @param id ID of the Doctor to retrieve
     * @return Retrieved Doctor object
     */
    public static Doctor getDoctorById(int id) {
        // Check if the doctor exists in the database
        if (doctorExists(id)) {
            return doctorMap.get(id);
        } else {
            // Log a warning if doctor not found
            LOGGER.warn("Doctor not found for ID: {}", id);
            throw new NotFoundException("Doctor not found for ID: " + id);
        }
    }

    /**
     * Update an existing Doctor object in the database.
     * @param id ID of the Doctor to update
     * @param updatedDoctor Updated Doctor object
     */
    public static void updateDoctor(int id, Doctor updatedDoctor) {
        try {
            // Check if the doctor exists in the database
            if (!doctorExists(id)) {
                throw new NotFoundException("Doctor not found for ID: " + id);
            }
            // Set the ID of the updated doctor
            updatedDoctor.setId(id);
            // Update the doctor in the database
            doctorMap.put(id, updatedDoctor);
            LOGGER.info("Updated doctor successfully: {}", updatedDoctor);
        } catch (NotFoundException e) {
            // Log a warning if doctor not found for update
            LOGGER.warn("Doctor not found for ID: {} during update", id);
            throw e;
        } catch (Exception e) {
            // Log any other unexpected errors
            LOGGER.error("Failed to update doctor: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update doctor" + e);
        }
    }

    /**
     * Delete a Doctor object from the database by its ID.
     * @param id ID of the Doctor to delete
     */
    public static void deleteDoctor(int id) {
        try {
            // Check if the doctor exists in the database
            if (!doctorExists(id)) {
                throw new NotFoundException("Doctor not found for ID: " + id);
            }
            // Remove the doctor from the database
            doctorMap.remove(id);
            LOGGER.info("Deleted doctor successfully with ID: {}", id);
        } catch (NotFoundException e) {
            // Log a warning if doctor not found for deletion
            LOGGER.warn("Doctor not found for ID: {}to delete", id);
            throw e;
        } catch (Exception e) {
            // Log any other unexpected errors
            LOGGER.error("Failed to delete doctor: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete doctor", e);
        }
    }

    /**
     * Check if a Doctor with the given ID exists in the database.
     * @param id ID of the Doctor to check
     * @return True if the doctor exists, otherwise False
     */
    public static boolean doctorExists(int id) {
        return doctorMap.containsKey(id);
    }

    /**
     * Validate a Doctor object.
     * This method checks if the Doctor object is not null and has valid attributes.
     * @param doctor Doctor object to validate
     * @return True if the Doctor is valid, otherwise False
     */
    public static boolean isValidDoctor(Doctor doctor) {
        return doctor != null && doctor.getName() != null && !doctor.getName().isEmpty()
                && doctor.getContact() != null && !doctor.getAddress().isEmpty()
                && doctor.getSpecialization() != null && !doctor.getSpecialization().isEmpty();
    }
}
