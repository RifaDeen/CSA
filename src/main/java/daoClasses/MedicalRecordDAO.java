/**
 * Data Access Object (DAO) class for managing MedicalRecord entities.
 * This class provides methods for CRUD operations on medical record objects.
 * Author: rifa
 * IIT no:: 20220701
 */
package daoClasses;

import exceptionClasses.AlreadyExistsException;
import exceptionClasses.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import modelClasses.MedicalRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MedicalRecordDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MedicalRecordDAO.class);

    private static Map<Integer, MedicalRecord> medicalRecordsMap = new HashMap<>();
    private static AtomicInteger idCount = new AtomicInteger(0);

    // Adding sample medical records to the map
//    static {
//        
//        // Sample Patients
//        Patient patient1 = new Patient(114, "Mary Davis", "2464567890", "14 rose St", "High blood pressure", "Stable");
//        Patient patient2 = new Patient(115, "Fazly Kay", "5676543210", "6 Lirk St", "None", "Good");
//        // Sample Medical Records
//        medicalRecordsMap.put(1,new MedicalRecord(1, patient1, "Hypertension", "Prescription medication and lifestyle changes"));
//        medicalRecordsMap.put(2,new MedicalRecord(2, patient2, "Diabetes", "Insulin therapy and dietary modifications"));
//
//    }

    // Method to retrieve all medical records
    public static Map<Integer, MedicalRecord> getMedicalRecords() {
        return medicalRecordsMap;
    }

    // Method to retrieve a medical record by its record ID
    public static MedicalRecord getMedicalRecordById(int id) {
        MedicalRecord medical = medicalRecordsMap.get(id);
        if (medical == null){
            LOGGER.warn("Medical record not found for ID: {}", id);
            throw new NotFoundException("Medical record not found for ID: " + id);
        }
        return medical; 
    }

    // Method to generate a new unique record ID
    public static int generateId() {
        return idCount.incrementAndGet();
    }

    // Method to add a new medical record
    public static void addMedicalRecord(MedicalRecord record) {
        try{
            int newId = generateId();
            // Check if the generated ID is already in use
            while (recordExists(newId)) {
                newId = generateId(); // Generate a new ID until it's unique
            }
            record.setRecordId(newId);
            medicalRecordsMap.put(newId, record);
            LOGGER.info("Added new record successfully: {}", record);
        } catch (AlreadyExistsException e) {
            LOGGER.error("Failed to add medical record: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to add medical record: {}", e);
            throw new RuntimeException("Failed to add medical record", e);
        }
              
    }

    // Method to update an existing medical record
    public static void updateMedicalRecord(int id, MedicalRecord updatedRecord) {
        try{
            if (recordExists(id)) {
                updatedRecord.setRecordId(id);
                medicalRecordsMap.put(id, updatedRecord);
                LOGGER.info("successfully updated medical record id: {}", id);
            }
        }catch (NotFoundException e) {
            LOGGER.warn("Medical record not found for ID: {} during update", id);
            throw e;
        }catch (Exception e) {
            LOGGER.error("Failed to update medical record: {}", e.getMessage());
            throw new RuntimeException("Failed to update medical record", e);
        }
     
    }

    // Method to delete a medical record by its record ID
    public static void deleteMedicalRecord(int id) {
        try{
            if (!recordExists(id)){
                throw new NotFoundException("Medical record not found for ID: " + id);
            }
            medicalRecordsMap.remove(id);
            LOGGER.info("successfully deleted medical record no: {}", id);
        }catch (NotFoundException e) {
            LOGGER.warn("Medical record not found for ID: {} to delete", id);
            throw e;
        }catch (Exception e) {
            LOGGER.error("Failed to delete medical record: {}", e);
            throw new RuntimeException("Failed to delete medical record", e);
        }      
    }
    
    // Method to check if a medical record exists by its record ID
    public static boolean recordExists(int id) {
        return medicalRecordsMap.containsKey(id);
    }

     // Method to validate a medical record
    public static boolean isValidRecord(MedicalRecord medicalRecord) {
        return medicalRecord != null && medicalRecord.getPatient() != null 
                && medicalRecord.getDiagnosis() != null 
                && !medicalRecord.getTreatment().isEmpty();
    }
}
