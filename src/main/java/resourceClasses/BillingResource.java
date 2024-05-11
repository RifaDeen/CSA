/**
 * Resource class for managing Billing entities.
 * Provides RESTful endpoints for CRUD operations on bills.
 * Author: Rifa
 * IIT No: 20220701
 */

package resourceClasses;

import daoClasses.BillingDAO;
import daoClasses.DoctorDAO;
import daoClasses.PatientDAO;
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
import modelClasses.Billing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/bills")
public class BillingResource {
    
    private final Logger LOGGER = LoggerFactory.getLogger(BillingResource.class);

    // Retrieve all bills
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBillings() {
        try {
            LOGGER.info("Retrieved all bills");
            return Response.status(Response.Status.OK)
                    .entity(BillingDAO.getBills().values())
                    .build();
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve all bills: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    // Retrieve bill by Id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBillingById(@PathParam("id") int id) {
        try {
            Billing billing = BillingDAO.getBillById(id);
            if (billing != null) {
                LOGGER.info("Bill with Id "+ id + " is found");
                return Response.status(Response.Status.OK)
                        .entity(billing)
                        .build();
            } else {
                LOGGER.error("Failed to retrieve bill by ID {}: {}", id);
                throw new NotFoundException("Bill with id " + id + " not found");
            } 
        } catch(NotFoundException e){
            LOGGER.error("Failed to retrieve bill: {}", e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            LOGGER.error("Failed to retrieve bill by ID {}: {}", id, e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    // Create billing
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBilling(Billing billing) {
        try {
            // Check if the doctor exists
            int docId  = billing.getDoctor().getId();
            if (!DoctorDAO.doctorExists(docId)){
                LOGGER.warn("Doctor not found for ID: " + docId);
                throw new NotFoundException("Doctor not found for ID: " + docId);
            }
            // Check if the patient exists
            int patId  = billing.getPatient().getId();
            if (!PatientDAO.patientExists(patId)){
                LOGGER.warn("Patient not found for ID: " + patId);
                throw new NotFoundException("Patient not found for ID: " + patId);
            }
            // Check if the bill exists
            if (BillingDAO.billExists(billing.getRefID())) {
                LOGGER.warn("Bill with ref ID {} already exists", billing.getRefID());
                throw new AlreadyExistsException("Bill with ref ID " + billing.getRefID()+ " already exists");
            }
            // Check if the billing data is valid
            if (!BillingDAO.isValidBilling(billing)) {
                LOGGER.warn("Invalid data for person: {}", billing);
                throw new InvalidDataException("Invalid data for person");
            }
            // Add the billing
            BillingDAO.addBill(billing);
            LOGGER.info("Added new billing successfully: {}", billing);
            return Response.status(Response.Status.CREATED).entity("Billing created successfully: " + billing).build();
        } catch (AlreadyExistsException | InvalidDataException  e) {
            LOGGER.error("Failed to add bill: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    //update billing
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBilling(@PathParam("id") int id, Billing updatedBilling) {
        try {
            //check if bill exists
            if (!BillingDAO.billExists(id)) {
                LOGGER.warn("Bill with ref ID {} already exists", id);
                throw new NotFoundException("Bill with ref ID " + id + " not found");
            }
            // Check if the billing data is valid
            if (!BillingDAO.isValidBilling(updatedBilling)) {
                LOGGER.warn("Invalid data for billing: {}", updatedBilling);
                throw new InvalidDataException("Invalid data for billing");
            }
            // Update the billing
            BillingDAO.updateBill(id, updatedBilling);
            
            LOGGER.info("Updated bill successfully: {}", updatedBilling);
            return Response.status(Response.Status.OK)
                    .entity("Billing with ref ID " + id + " was updated")
                    .build();
        } catch (NotFoundException | InvalidDataException e) {
            LOGGER.error("Failed to update bill: {}", e.getMessage(), e);
            throw e;
        } 
    }
    
    // Delete billing
    @DELETE
    @Path("/delete/{id}")
    public Response deleteBilling(@PathParam("id") int id) {
        try {
            //delete the billing
            BillingDAO.deleteBill(id);
            LOGGER.info("Deleted bill successfully with ID: {}", id);
            return Response.status(Response.Status.OK)
                           .entity("Bill with ref ID " + id + " deleted successfully")
                           .build();
        } catch (NotFoundException e) {
            LOGGER.error("Failed to delete bill: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to delete bill: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
}
