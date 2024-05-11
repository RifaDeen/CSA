/**
 * Resource class for managing Prescription entities.
 * Provides RESTful endpoints for CRUD operations on prescriptions.
 * Author: Rifa 
 * IIT no: 20220701
 */

package resourceClasses;

import daoClasses.DoctorDAO;
import daoClasses.PatientDAO;
import daoClasses.PrescriptionDAO;
import exceptionClasses.AlreadyExistsException;
import exceptionClasses.InvalidDataException;
import exceptionClasses.NotFoundException;
import java.util.Collection;
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
import modelClasses.Prescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/prescriptions")
public class PrescriptionResource {
    
    private final Logger LOGGER = LoggerFactory.getLogger(PrescriptionResource.class);

    //retrieve all prescriptions
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPrescriptions() {
        try {
            LOGGER.info("Retrieved all prescriptions");
            Collection<Prescription> prescriptions = PrescriptionDAO.getPrescriptions().values();
            return Response.status(Response.Status.OK)
                    .entity(prescriptions)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal Server Error").build();
        }
    }

    //retrieve prescription by id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrescriptionById(@PathParam("id") int id) {
        try {
            Prescription prescription = PrescriptionDAO.getPrescriptionById(id);
            if (prescription != null) {
                LOGGER.info("Prescription with Id "+ id + " is found");
                return Response.status(Response.Status.OK)
                        .entity(prescription)
                        .build();
            } else {
                LOGGER.error("Failed to retrieve prescription by ID {}: {}", id);
                throw new NotFoundException("Prescription with id " + id + " not found");
            }
        } catch(NotFoundException e){
            LOGGER.error("Failed to retrieve prescription: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve prescription by ID {}: {}", id, e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                Response.Status.INTERNAL_SERVER_ERROR);}
    }

    //add prescription
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPrescription(Prescription prescription) {
        try {
            //check if doctor exists
            int docId  = prescription.getDoctor().getId();
            if (!DoctorDAO.doctorExists(docId)){
                LOGGER.warn("Doctor not found for ID: " + docId);
                throw new NotFoundException("Doctor not found for ID: " + docId);
            }
            //check if patient exists
            int patId  = prescription.getPatient().getId();
            if (!PatientDAO.patientExists(patId)){
                LOGGER.warn("Patient not found for ID: " + patId);
                throw new NotFoundException("Patient not found for ID: " + patId);
            }
            //check if a prescription exists
            if (PrescriptionDAO.prescriptionExists(prescription.getPrescriptionId())){
                LOGGER.warn("Prescription no. already exists", prescription.getPrescriptionId());
                throw new AlreadyExistsException("Prescription no. " +prescription.getPrescriptionId()+ " already exists");
            }
            //check if data is valid
            if (!PrescriptionDAO.isValidPrescription(prescription)) {
                LOGGER.warn("Invalid data for prescription");
                throw new InvalidDataException("Invalid data for prescription");
            }
            //add prescription
            PrescriptionDAO.addPrescription(prescription);
            return Response.status(Response.Status.CREATED)
                    .entity("Prescription created successfully: " + prescription)
                    .build();
        } catch (AlreadyExistsException | InvalidDataException e) {
            LOGGER.error("Failed to create appointment {}", e.getMessage(), e);
            throw e;
        }
    }

    //update prescription
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePrescription(@PathParam("id") int id, Prescription prescription) {
        try {
            //check if prescription exists
            if (PrescriptionDAO. prescriptionExists(prescription.getPrescriptionId())){
                LOGGER.warn("Prescription no. already exists", prescription.getPrescriptionId());
                throw new AlreadyExistsException("Prescription no. " + prescription.getPrescriptionId() + " already exists");
            }
            //check id data iis valid
            if (!PrescriptionDAO.isValidPrescription(prescription)) {
                LOGGER.warn("Invalid data for prescription during update: {}", prescription);
                throw new InvalidDataException("Invalid data for prescription");
            }
            PrescriptionDAO.updatePrescription(id, prescription);
            
            LOGGER.info("Updated prescription successfully: {}", prescription);
            return Response.status(Response.Status.OK)
                    .entity("Prescription with prescription no: " + prescription.getPrescriptionId() + " updated successfully ")
                    .build();
        } catch (NotFoundException | InvalidDataException e) {
            LOGGER.error("Failed to update prescription: {}", e.getMessage(), e);
            throw e;
        }
    }

    //delete prescription
    @DELETE
    @Path("/delete/{id}")
    public Response deletePrescription(@PathParam("id") int id) {
        try {
            PrescriptionDAO.deletePrescription(id);
            LOGGER.info("Deleted prescription successfully with ID: {}", id);
            return Response.status(Response.Status.OK)
                    .entity("Prescription with prescription no: " + id +" deleted successfully")
                    .build();
        } catch (NotFoundException e) {
            LOGGER.error("Failed to delete prescription: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to delete prescription: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }   
    }
    
}
