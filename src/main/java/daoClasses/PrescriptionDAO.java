/**
 * Data Access Object (DAO) class for managing prescription entities.
 * This class provides methods for CRUD operations on prescription objects.
 * Author: rifa
 * IIT no:: 20220701
 */

package daoClasses;

import exceptionClasses.AlreadyExistsException;
import exceptionClasses.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
//import modelClasses.Doctor;
//import modelClasses.Patient;
import modelClasses.Prescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrescriptionDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PrescriptionDAO.class);

    private static Map<Integer, Prescription> prescriptionMap = new HashMap<>();
    private static AtomicInteger idCount = new AtomicInteger(0);

//    // Adding sample prescriptions to the map
//    static {
//        
//        Patient patient1 = new Patient(116, "Rifa", "123234120", "87 Main St", "hypertension", "Stable");
//        Patient patient2 = new Patient(117, "Anisa", "9876543654", "4 weree St", "None", "Good");
//        Doctor doctor1 = new Doctor(118, "Dr.Noor", "987654321", "87 posh St", "Cardiologist");
//        Doctor doctor2 = new Doctor(119, "Dr.Deen", "123456789", "98 fret St", "Neurologist");
//
//        // Sample prescriptions 
//        // Sample Prescriptions
//        prescriptionMap.put(1, new Prescription(1, patient1, doctor1, "Lisinopril", "20 mg", "Take once daily with food", 30));
//        prescriptionMap.put(2, new Prescription(2, patient2, doctor2, "Metformin", "1000 mg", "Take twice daily with meals", 60));
//        
//    }

    
    // Method to generate a new unique prescription ID
    public static int generateId() {
        return idCount.incrementAndGet();
    }
    
    // Method to retrieve all prescriptions
    public static Map<Integer, Prescription> getPrescriptions() {
        return prescriptionMap;
    }

    // Method to add a new prescription
    public static void addPrescription(Prescription prescription) {
        try {
            int newId = generateId();
            // Check if the generated ID is already in use
            while (prescriptionMap.containsKey(newId)) {
                newId = generateId(); // Generate a new ID until it's unique
            }
            prescription.setPrescriptionId(newId);
            prescriptionMap.put(newId, prescription);
            LOGGER.info("Added new prescription successfully: {}", prescription);
        } catch (AlreadyExistsException e) {
            LOGGER.error("Failed to add prescription: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to add prescription: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    // Method to retrieve a prescription by its ID
    public static Prescription getPrescriptionById(int id) {
        Prescription prescription = prescriptionMap.get(id);
        if (prescription == null){
            LOGGER.warn("Prescription not found for ID: {}", id);
            throw new NotFoundException("Prescription not found for ID: " + id);
        }
        return prescription;
    }

    // Method to update an existing prescription
    public static void updatePrescription(int id, Prescription updatedPrescription) {
        try {
            if (prescriptionExists(id)) {
                updatedPrescription.setPrescriptionId(id);
                prescriptionMap.put(id, updatedPrescription);
                LOGGER.info("successfully updated prescription no: " + id);
            } 
        } catch (NotFoundException e) {
            LOGGER.warn("Prescription not found for ID: {} during update", id);
            throw e;
        }catch (Exception e){
            LOGGER.error("Failed to update prescription: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update precription" + e);
        }
    }

    // Method to delete a prescription by its ID
    public static void deletePrescription(int id) {
        try {
            if( !prescriptionExists(id)){
                throw new NotFoundException("Prescription not found for ID: " + id);
            }
            prescriptionMap.remove(id);
            LOGGER.info("successfully deleted prescription no: {}", id);
        }catch (NotFoundException e){
            LOGGER.warn("Prescription not found for ID: {} to delete", id);
            throw e;
        }catch (Exception e){
            LOGGER.error("Failed to delete prescription: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete prescription" + e);
        }
        
    }
    
    // Method to check if a prescription exists by its ID
    public static boolean prescriptionExists(int id) {
        return prescriptionMap.containsKey(id);
    }
    
    // Method to validate a prescription
    public static boolean isValidPrescription(Prescription prescription){
    return prescription.getPrescriptionId() > 0 &&
               prescription.getDoctor() != null &&prescription.getPatient() != null &&
               prescription.getMedication() != null && !prescription.getMedication().isEmpty() &&
               prescription.getDosage() != null && !prescription.getDosage().isEmpty();
    }
    
}
