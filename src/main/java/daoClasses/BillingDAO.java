/**
 * Data Access Object (DAO) class for managing Billing entities.
 * This class provides methods for CRUD operations on billing objects.
 * Author: Rifa
 * IIT no: 20220701
 */
package daoClasses;

import exceptionClasses.AlreadyExistsException;
import exceptionClasses.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import modelClasses.Billing;
//import modelClasses.Doctor;
//import modelClasses.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BillingDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BillingDAO.class);
    
    private static Map<Integer, Billing> billMap = new HashMap<>();
    private static AtomicInteger idCount = new AtomicInteger(0);

//    // Adding sample bills to the map
//    static {
//        
//        Patient patient1 = new Patient(110, "Maya Doe", "8864567890", "123 Main St", "Allergic to penicillin", "Stable");
//        Patient patient2 = new Patient(111, "Peter Kuray", "9876543210", "456 Lilac St", "None", "Good");
//        Doctor doctor1 = new Doctor(112, "Dr. prithi", "987224321", "4 daisy St", "Cardiologist");
//        Doctor doctor2 = new Doctor(113, "Dr. perera", "123443232789", "7 wesdt St", "Neurologist");
//
//        // Sample Bills
//        billMap.put(1, new Billing(1, patient1, doctor1, 200.0, true));
//        billMap.put(2, new Billing(2, patient2, doctor2, 150.0, false));
//
//    }

    // Method to retrieve all bills
    public static Map<Integer, Billing> getBills() {
        return billMap;
    }
    
    // Method to generate a new unique reference ID
    public static int generateId() {
        return idCount.incrementAndGet();
    }
  
    // Method to add a new bill
    public static void addBill(Billing bill) {
        try {
            int newId = generateId();
            while (billMap.containsKey(newId)) {
                newId = generateId(); 
            }
            bill.setRefID(newId);
            billMap.put(newId, bill);
            LOGGER.info("Added new bill successfully: {}", bill);
        } catch (AlreadyExistsException e) {
            LOGGER.error("Failed to add bill: {}", e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            LOGGER.error("Failed to add bill: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add bill", e);
        }
    }
    
    // Method to retrieve a bill by its reference ID
    public static Billing getBillById(int id) {
        Billing bill = billMap.get(id);
        if (bill == null){
            LOGGER.warn("Bill not found for ID: {}", id);
            throw new NotFoundException("Bill not found for ID: " + id);
        }
        return bill;
    }

    // Method to retrieve a bill by its reference ID
    public static void updateBill(int id, Billing updatedBill) {
        try {
            if (billExists(id)) {
                updatedBill.setRefID(id);
                billMap.put(id, updatedBill);
                LOGGER.info("successfully updated bill no: " + id);
            }
        }catch (NotFoundException e) {
            LOGGER.warn("Bill not found for ID: {} during update", id);
            throw e;
        }catch (Exception e){
            LOGGER.error("Failed to update bill: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update bill" + e);
        }
    }

    // Method to delete a bill by its reference ID
    public static void deleteBill(int id) {
        try {
            if (!billExists(id)){
                throw new NotFoundException("Bill not found for ID: " + id);
            }
            billMap.remove(id);
            LOGGER.info("successfully deleted bill no: " + id);
        } catch (NotFoundException e) {
            LOGGER.warn("Bill not found for ID: {} during update", id);
            throw e;
        }catch (Exception e){
            LOGGER.error("Failed to update bill: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update bill" + e);
        }
    }
    
    // Method to check if a bill exists by its reference ID
    public static boolean billExists(int id) {
        return billMap.containsKey(id);
    }
    
    // Method to validate a billing record
    public static boolean isValidBilling(Billing billing){
        return billing.getRefID() > 0 && billing.getPatient() != null &&
               billing.getDoctor() != null && billing.getBillingAmount() >= 0;
    }
    
}
