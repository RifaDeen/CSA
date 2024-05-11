/**
 * Resource class for managing Appointment entities.
 * Provides RESTful endpoints for CRUD operations on appointments.
 * Author: Rifa
 * IIT No: 20220701
 */

package resourceClasses;

import daoClasses.AppointmentDAO;
import daoClasses.DoctorDAO;
import daoClasses.PatientDAO;
import exceptionClasses.AlreadyExistsException;
import exceptionClasses.InvalidDataException;
import exceptionClasses.NotFoundException;
import modelClasses.Appointment;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/appointments")
public class AppointmentResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentResource.class);

    //retrieve all appointments
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAppointments() {
        try {
            return Response.status(Response.Status.OK)
                    .entity(AppointmentDAO.getAppointments().values())
                    .build();
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve all appointments: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                 Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    //retrieve appointment by Id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAppointmentById(@PathParam("id") int id) {
        try {
            Appointment appointment = AppointmentDAO.getAppointmentById(id);
            if (appointment != null){
                LOGGER.info("Appointment no. "+ id + " is found");
                return Response.status(Response.Status.OK)
                        .entity(appointment)
                        .build();
            } 
            else{ 
                LOGGER.error("Failed to retrieve appointment by number{}: {}", id);
                throw new NotFoundException("Appointment no. "+ id + " not found");
            }
        } catch(NotFoundException e){
            LOGGER.error("Failed to retrieve appointment: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve appointment no {}: {}", id, e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    //create appointment
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
       public Response addAppointment(Appointment appointment) {
        try {
            //check if the doctor exists
            int docId  = appointment.getDoctor().getId();
            if (!DoctorDAO.doctorExists(docId)){
                LOGGER.warn("Doctor not found for ID: " + docId);
                throw new NotFoundException("Doctor not found for ID: " + docId);
            }
            //check if the patient exists
            int patId  = appointment.getPatient().getId();
            if (!PatientDAO.patientExists(patId)){
                LOGGER.warn("Patient not found for ID: " + patId);
                throw new NotFoundException("Patient not found for ID: " + patId);
            }
            //check if an appointment already exist
            if (AppointmentDAO.appointmentExists(appointment.getAppointmentNo())){
                LOGGER.warn("Appointment no. already exists", appointment.getAppointmentNo());
                throw new AlreadyExistsException("Appointment no. " + appointment.getAppointmentNo() + " already exists");
            }
            //check if appointment data is valid
            if (!AppointmentDAO.isValidAppointment(appointment)) {
                LOGGER.warn("Invalid data for appointment");
                throw new InvalidDataException("Invalid data for appointment");
            }
            //add the appointment
            AppointmentDAO.addAppointment(appointment);
            
            LOGGER.info("Appointment added successfully: {}", appointment);
            return Response.status(Response.Status.CREATED)
                    .entity("Appointment no. " +appointment.getAppointmentNo()+ " created successfully")
                    .build();
        } catch (AlreadyExistsException | InvalidDataException e) {
            LOGGER.error("Failed to create appointment {}", e.getMessage(), e);
            throw e;
        }
    }
     
     //update appointment  
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAppointment(@PathParam("id") int id, Appointment appointment) {
        try {
            //check if the appointment no already exists
            if (AppointmentDAO.appointmentExists(appointment.getAppointmentNo())){
                LOGGER.warn("Appointment no. already exists", appointment.getAppointmentNo());
                throw new AlreadyExistsException("Appointment no. " + appointment.getAppointmentNo() + " already exists");
            }
            ///check if appointment data is valid
            if (!AppointmentDAO.isValidAppointment(appointment)) {
                LOGGER.warn("Invalid data for appointment during update: {}", appointment);
                throw new InvalidDataException("Invalid data for appointment");
            }
            
            //update the appointment
            AppointmentDAO.updateAppointment(id, appointment);
            
            LOGGER.info("Updated appointment successfully: {}", appointment);
            return Response.status(Response.Status.OK)
                    .entity("Appointment with appointment no: " + appointment.getAppointmentNo() + " updated successfully ")
                    .build();
        } catch (NotFoundException | InvalidDataException e) {
            LOGGER.error("Failed to update appointment: {}", e.getMessage(), e);
            throw e;
        }
    }

    //delete appointment
    @DELETE
    @Path("/delete/{id}")
    public Response deleteAppointment(@PathParam("id") int id) {
        try {
            //delete the appointment
            AppointmentDAO.deleteAppointment(id);
            LOGGER.info("Deleted appointment successfully with ID: {}", id);
            return Response.status(Response.Status.OK)
                    .entity("Appointment with appointment no: " + id +"deleted successfully")
                    .build();
        } catch (NotFoundException e) {
            LOGGER.error("Failed to delete appointment: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to delete appointment: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }   
    }
    
}
