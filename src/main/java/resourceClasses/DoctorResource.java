/**
 * Resource class for managing Doctor entities.
 * Provides RESTful endpoints for CRUD operations on doctors.
 * Author: Rifa
 * IIT No: 20220701
 */

package resourceClasses;

import daoClasses.DoctorDAO;
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
import modelClasses.Doctor;
import modelClasses.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/doctors")
public class DoctorResource {
    
    private final Logger LOGGER = LoggerFactory.getLogger(DoctorResource.class);

    //get all doctors
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDoctors() {
        try {
            LOGGER.info("Retrieved all doctors");
            return Response.status(Response.Status.OK)
                    .entity(DoctorDAO.getDoctors().values())
                    .build();
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve all doctors: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    //get doctor by Id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDoctorById(@PathParam("id") int id) {
        try {
            Doctor doctor = DoctorDAO.getDoctorById(id);
            if (doctor != null) {
                LOGGER.info("Doctor found for ID: " + id);
                return Response.status(Response.Status.OK)
                        .entity(doctor)
                        .build();
            } else {
                LOGGER.error("Failed to retrieve doctor by ID {}: {}", id);
                throw new NotFoundException("Doctor not found for ID: " + id);
            }
        } catch(NotFoundException e){
        LOGGER.error("Failed to retrieve doctor: {}", e.getMessage(), e);
        throw e;
        } catch (Exception e) {
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
    public Response addDoctor(Doctor doctor) {
        try {
            //check if doctor exists
            if (DoctorDAO.doctorExists(doctor.getId())) {
                LOGGER.warn("Doctor with ID {} already exists", doctor.getId());
                throw new AlreadyExistsException("Doctor with ID " + doctor.getId() + " already exists");
            }
            //check if data entered is valid
            if (!DoctorDAO.isValidDoctor(doctor)) {
                LOGGER.warn("Invalid data for doctor: {}", doctor);
                throw new InvalidDataException("Invalid data for doctor");
            }
            
            //add doctor
            DoctorDAO.addDoctor(doctor);

            // Check if the doctor is already added as a person
            if (!PersonDAO.personExists(doctor.getId())) {
                // Create a corresponding person
                Person person = new Person();
                person.setId(doctor.getId());
                person.setName(doctor.getName());
                person.setContact(doctor.getContact());
                person.setAddress(doctor.getAddress());
                // Add the person
                PersonDAO.addPerson(person);
            }

            LOGGER.info("Added new doctor successfully: {}", doctor);
            return Response.status(Response.Status.CREATED).entity("Doctor with id " + doctor.getId() + " was created successfully").build();

        } catch (AlreadyExistsException | InvalidDataException e) {
            LOGGER.error("Failed to add doctor: {}", e.getMessage(), e);
            throw e;
        }
    }

    //update doctor
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDoctor(@PathParam("id") int id, Doctor updatedDoctor) {
        try {
            //check if doctor exists
            if (!DoctorDAO.doctorExists(id)) {
                LOGGER.warn("Doctor not found for ID {} during update", id);
                throw new NotFoundException("Doctor not found for ID: " + id);
            }
            //check if data is valid
            if (!DoctorDAO.isValidDoctor(updatedDoctor)) {
                LOGGER.warn("Invalid data for doctor during update: {}", updatedDoctor);
                throw new InvalidDataException("Invalid data for doctor");
            }

            //update
            DoctorDAO.updateDoctor(id, updatedDoctor);

            // Update the corresponding person
            if (PersonDAO.personExists(id+1)) {
                Person person = PersonDAO.getPersonById(id+1);
                person.setName(updatedDoctor.getName());
                person.setContact(updatedDoctor.getContact());
                person.setAddress(updatedDoctor.getAddress());
                PersonDAO.updatePerson(id+1, person);
            } else {
                // Create a new person if not found
                Person person = new Person();
                person.setId(updatedDoctor.getId());
                person.setName(updatedDoctor.getName());
                person.setContact(updatedDoctor.getContact());
                person.setAddress(updatedDoctor.getAddress());
                PersonDAO.addPerson(person);
            }

            LOGGER.info("Updated doctor successfully: {}", updatedDoctor);
            return Response.status(Response.Status.OK).entity("Doctor with ID " + id + " was updated").build();
        } catch (NotFoundException | InvalidDataException e) {
            LOGGER.error("Failed to update doctor: {}", e.getMessage(), e);
            throw e;
        }
    }

    //dete doctors
    @DELETE
    @Path("/delete/{id}")
    public Response deleteDoctor(@PathParam("id") int id) {
        try {
            Doctor existingDoctor = DoctorDAO.getDoctorById(id);
            if (existingDoctor != null) {
                DoctorDAO.deleteDoctor(id);
                
                // Delete the corresponding person
                PersonDAO.deletePerson(id);
            
                return Response.status(Response.Status.OK).entity("Doctor deleted successfully: " + existingDoctor).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Doctor not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }
    
}
