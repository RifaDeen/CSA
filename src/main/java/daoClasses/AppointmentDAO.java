/**
 * Data Access Object (DAO) class for managing Appointment entities.
 * This class provides methods for CRUD operations on appointment objects.
 * Author: Rifa
 * IIT no: 20220701
 */
package daoClasses;

import exceptionClasses.AlreadyExistsException;
import exceptionClasses.NotFoundException;
import modelClasses.Appointment;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppointmentDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentDAO.class);

    // Map to store appointments with appointment number as key
    private static Map<Integer, Appointment> appointmentMap = new HashMap<>();
    
    // Atomic integer to generate unique appointment IDs
    private static AtomicInteger idCount = new AtomicInteger(100);

    // Method to generate a new unique appointment ID
    public static int generateId() {
        return idCount.incrementAndGet();
    }

    // Method to retrieve all appointments
    public static Map<Integer, Appointment> getAppointments() {
        return appointmentMap;
    }

    // Method to add a new appointment
    public static void addAppointment(Appointment appointment) {
        try {
            // Generate a new appointment ID
            int newId = generateId();
            // Check if the appointment ID already exists
            while (appointmentExists(newId)) {
                newId = generateId();
            }
            // Set the appointment number and add it to the map
            appointment.setAppointmentNo(newId);
            appointmentMap.put(newId, appointment);
            LOGGER.info("Added new appointment successfully: {}", appointment);
        } catch (AlreadyExistsException e) {
            LOGGER.error("Failed to add appointment: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to add appointment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add appointment", e);
        }
    }

    // Method to retrieve an appointment by its ID
    public static Appointment getAppointmentById(int id) {
        Appointment appointment = appointmentMap.get(id);
        if (appointment == null) {
            LOGGER.warn("Appointment not found for ID: {}", id);
            throw new NotFoundException("Appointment not found for ID: " + id);
        }
        return appointment;
    }

    // Method to update an existing appointment
    public static void updateAppointment(int id, Appointment updatedAppointment) {
        try {
            if (appointmentExists(id)) {
                // Set the appointment number and update it in the map
                updatedAppointment.setAppointmentNo(id);
                appointmentMap.put(id, updatedAppointment);
                LOGGER.info("Successfully updated appointment no: {}", id);
            }
        } catch (NotFoundException e) {
            LOGGER.warn("Appointment not found for ID: {} during update", id);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to update appointment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update appointment" + e);
        }
    }

    // Method to delete an appointment by its ID
    public static void deleteAppointment(int id) {
        try {
            if (!appointmentExists(id)) {
                throw new NotFoundException("Appointment not found for ID: " + id);
            }
            // Remove the appointment from the map
            appointmentMap.remove(id);
            LOGGER.info("Successfully deleted appointment no: {}", id);
        } catch (NotFoundException e) {
            LOGGER.warn("Appointment not found for ID: {} to delete", id);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to delete appointment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete appointment" + e);
        }
    }

    // Method to check if an appointment exists by its ID
    public static boolean appointmentExists(int id) {
        return appointmentMap.containsKey(id);
    }

    // Method to validate an appointment
    public static boolean isValidAppointment(Appointment appointment) {
        return appointment != null 
                && appointment.getDate() != null && !appointment.getDate().isEmpty() 
                && appointment.getTime() != null && !appointment.getTime().isEmpty()
                && appointment.getPatient() != null && appointment.getDoctor() != null;
    }
}
