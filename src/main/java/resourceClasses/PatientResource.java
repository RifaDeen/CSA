/**
 * Resource class for managing Patient entities.
 * Provides RESTful endpoints for CRUD operations on patients.
 * Author: Rifa
 * IIT No: 20220701
 */

package resourceClasses;

import daoClasses.PatientDAO;
import daoClasses.PersonDAO;
import exceptionClasses.AlreadyExistsException;
import exceptionClasses.InvalidDataException;
import exceptionClasses.NotFoundException;
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
import modelClasses.Patient;
import modelClasses.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/patients")
public class PatientResource {
    
    private final Logger LOGGER = LoggerFactory.getLogger(PatientResource.class);
    
    //get all patients
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPatients() {
         try {
            LOGGER.info("Retrieved all patients");
            return Response.status(Response.Status.OK)
                    .entity(PatientDAO.getPatients().values())
                    .build();
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve all patients: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    //get patient by id
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPatientById(@PathParam("id") int id) {
        try {
            Patient patient = PatientDAO.getPatientById(id);
            if (patient != null) {
                LOGGER.info("Patient with Id "+ id + " is found");
                return Response.status(Response.Status.OK)
                        .entity(patient)
                        .build();
            } else {
                LOGGER.error("Failed to retrieve patient by ID {}: {}", id);
                throw new NotFoundException("Patient not found for ID: " + id);
            }
        } catch(NotFoundException e){
            LOGGER.error("Failed to retrieve patient: {}", e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            LOGGER.error("Failed to retrieve doctor by ID {}: {}", id, e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    //create patient
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPatient(Patient patient) {
        try {
            //check if patient exist*
            if (PatientDAO.patientExists(patient.getId())) {
                LOGGER.warn("Patient with ID {} already exists", patient.getId());
                throw new AlreadyExistsException("Patient with ID " + patient.getId() + " already exists");
            }
            //check if entered data is valid
            if (!PatientDAO.isValidPatient(patient)) {
                LOGGER.warn("Invalid data for patient: {}", patient);
                throw new InvalidDataException("Invalid data for patient");
            }
            
            //add patient
            PatientDAO.addPatient(patient);

            // Check if the patient is already added as a person
            if (!PatientDAO.patientIsPerson(patient.getId())) {
                // Create a corresponding person
                Person person = new Person();
                person.setName(patient.getName());
                person.setContact(patient.getContact());
                person.setAddress(patient.getAddress());
                // Add the person
                PersonDAO.addPerson(person);
            }

            LOGGER.info("Added new patient successfully: {}", patient);
            return Response.status(Response.Status.CREATED)
                    .entity("Patient with id " + patient.getId() + " was created successfully")
                    .build();
        } catch (AlreadyExistsException | InvalidDataException e) {
            LOGGER.error("Failed to add patient: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    //update patient
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePatient(@PathParam("id") int id, Patient updatedPatient) {
        try {
            //check if person exists
            if (!PatientDAO.patientExists(id)) {
                LOGGER.warn("Patient not found for ID {} during update", id);
                throw new NotFoundException("Patient not found for ID: " + id);
            }
            ///check id data entered is valid
            if (!PatientDAO.isValidPatient(updatedPatient)) {
                LOGGER.warn("Invalid data for patient during update: {}", updatedPatient);
                throw new InvalidDataException("Invalid data for patient");
            }
            
            
            //update
            PatientDAO.updatePatient(id, updatedPatient);

            // Update the corresponding person
            if (PersonDAO.personExists(id+1)) {
                Person person = PersonDAO.getPersonById(id+1);
                person.setName(updatedPatient.getName());
                person.setContact(updatedPatient.getContact());
                person.setAddress(updatedPatient.getAddress());
                PersonDAO.updatePerson(id+1, person);
            } else {
                // Create a new person if not found
                Person person = new Person();
                person.setId(updatedPatient.getId());
                person.setName(updatedPatient.getName());
                person.setContact(updatedPatient.getContact());
                person.setAddress(updatedPatient.getAddress());
                  PersonDAO.addPerson(person);
            }

            LOGGER.info("Updated patient successfully: {}", updatedPatient);
            return Response.status(Response.Status.OK)
                    .entity("Patient with ID " + id + " was updated")
                    .build();
        } catch (NotFoundException | InvalidDataException e) {
            LOGGER.error("Failed to update patient: {}", e.getMessage(), e);
            throw e;
        }
    }

    //delete patients
    @DELETE
    @Path("/delete/{id}")
    public Response deletePatient(@PathParam("id") int id) {
        try {          
            // Delete the patient
            PatientDAO.deletePatient(id);
            // Delete the corresponding person
            PersonDAO.deletePerson(id);
            LOGGER.info("Patient with Id " + id + " deleted successfully" );
            return Response.status(Response.Status.OK)
                .entity("Patient with ID " + id + " deleted successfully")
                .build();
        } catch(NotFoundException e){
            LOGGER.error("Failed to delete patient: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to delete patient: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
 
}
