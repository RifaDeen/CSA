/**
 * Resource class for managing Person entities.
 * Provides RESTful endpoints for CRUD operations on persons.
 * Author: Rifa
 * IIT No: 20220701
 */

package resourceClasses;


import daoClasses.PersonDAO;
import exceptionClasses.AlreadyExistsException;
import exceptionClasses.InvalidDataException;
import exceptionClasses.NotFoundException;
import modelClasses.Person;
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


@Path("/persons")
public class PersonResource {
    
    private final Logger LOGGER = LoggerFactory.getLogger(PersonResource.class);

     // Retrieve all persons
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons() {
        try {
            LOGGER.info("Retrieved all persons");
            return Response.status(Response.Status.OK)
                           .entity(PersonDAO.getPersons().values())
                           .build();
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve all persons: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    // Retrieve person by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonById(@PathParam("id") int id) {
        try {
            // Retrieve person from DAO layer by ID
            Person person = PersonDAO.getPersonById(id);
            if(person != null){
                LOGGER.info("Person with Id "+ id + " is found");
                return Response.status(Response.Status.OK)
                        .entity(person)
                        .build();
            }else{
                 // If person is not found, throw NotFoundException
                LOGGER.error("Failed to retrieve person by ID {}: {}", id);
                throw new NotFoundException("Person with id " + id + " not found");
            } 
        } catch(NotFoundException e){
            LOGGER.error("Failed to retrieve person: {}", e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            LOGGER.error("Failed to retrieve person by ID {}: {}", id, e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    // Create a new person
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPerson(Person person) {
        try {
             // Check if person with the same ID already exists
            if (PersonDAO.personExists(person.getId())) {
                LOGGER.warn("Person with ID {} already exists", person.getId());
                throw new AlreadyExistsException("Person with ID " + person.getId() + " already exists");
            }
            // Validate the data of the new person
            if (!PersonDAO.isValidPerson(person)) {
                LOGGER.warn("Invalid data for person: {}", person);
                throw new InvalidDataException("Invalid data for person");
            }
            // Add the new person to the DAO layer
            PersonDAO.addPerson(person);
            
            LOGGER.info("Added new person successfully: {}", person);
            return Response.status(Response.Status.CREATED)
                    .entity("person with id "+ person.getId() + " was created successfully")
                    .build();
        } catch (AlreadyExistsException | InvalidDataException e) {
            LOGGER.error("Failed to add person: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Update an existing person
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePerson(@PathParam("id") int id, Person updatedPerson) {
        try {
            
            // Check if the person exists
            if (!PersonDAO.personExists(id)) {
                LOGGER.warn("Person not found for ID {} during update", id);
                throw new NotFoundException("Person not found for ID: " + id);
            }
            // Validate the updated person's data
            if (!PersonDAO.isValidPerson(updatedPerson)) {
                LOGGER.warn("Invalid data for person during update: {}", updatedPerson);
                throw new InvalidDataException("Invalid data for person");
            }
            
            // Update the person in the DAO layer
            PersonDAO.updatePerson(id, updatedPerson);
            
            LOGGER.info("Updated person successfully: {}", updatedPerson);
            return Response.status(Response.Status.OK)
                    .entity("Person with ID " + id + " was updated")
                    .build();
        } catch (NotFoundException | InvalidDataException e) {
            LOGGER.error("Failed to update person: {}", e.getMessage(), e);
            throw e;
        } 
    }

    // Delete a person
    @DELETE
    @Path("/delete/{id}")
    public Response deletePerson(@PathParam("id") int id) {
        try {
            PersonDAO.deletePerson(id);
            LOGGER.info("Deleted person successfully with ID: {}", id);
            return Response.status(Response.Status.OK)
                           .entity("Person with ID " + id + " deleted successfully")
                           .build();
        } catch (NotFoundException e) {
            LOGGER.error("Failed to delete person: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to delete person: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
}
