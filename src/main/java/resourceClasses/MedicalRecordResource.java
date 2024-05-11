/**
 * Resource class for managing Medical Record entities.
 * Provides RESTful endpoints for CRUD operations on medical records.
 * Author: Rifa
 * IIT no: 20220701
 */

package resourceClasses;

import daoClasses.MedicalRecordDAO;
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
import modelClasses.MedicalRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/medicalRecords")
public class MedicalRecordResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentResource.class);

    //retrieve all medical records
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMedicalRecords() {
        try {
            LOGGER.info("Retrieved all medical records");
            return Response.status(Response.Status.OK)
                    .entity(MedicalRecordDAO.getMedicalRecords().values())
                    .build();
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve all medical records: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    //retrieve medical record by id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicalRecordById(@PathParam("id") int id) {
        try {
            MedicalRecord medicalRecord = MedicalRecordDAO.getMedicalRecordById(id);
            if(medicalRecord != null){
            return Response.status(Response.Status.OK)
                    .entity(medicalRecord)
                    .build();
            }else {
            LOGGER.error("Failed to retrieve medical record by ID{}: {}", id);
            throw new NotFoundException("Medical record with Id " + id + " not found");
            }
        } catch(NotFoundException e){
            LOGGER.error("Failed to retrieve medical record: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve medical record no {}: {}", id, e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    //add medical record
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMedicalRecord(MedicalRecord medicalRecord) {
        try {
            //check if patient exists
            int patientId  = medicalRecord.getPatient().getId();
            if (!PatientDAO.patientExists(patientId)){
                throw new NotFoundException("Patient not found for ID: " + patientId);
            }
            //check if medical record exists for that id
            if (MedicalRecordDAO.recordExists(medicalRecord.getRecordId())){
                LOGGER.warn("Medical record Id already exists", medicalRecord.getRecordId());
                throw new AlreadyExistsException("Medical record Id " + medicalRecord.getRecordId() + " already exists");
            }
            //check if data is valid
            if (!MedicalRecordDAO.isValidRecord(medicalRecord)) {
                LOGGER.warn("Invalid data for medical record: {}", medicalRecord.getRecordId());
                throw new InvalidDataException("Invalid data for medical record");
            }
    
            //add medical record
            MedicalRecordDAO.addMedicalRecord(medicalRecord);
           
            LOGGER.info("Medical record added successfully: {}", medicalRecord);
            return Response.status(Response.Status.CREATED)
                    .entity("Medical Record addded successfully: " + medicalRecord.getRecordId())
                    .build();
        } catch (AlreadyExistsException | InvalidDataException e) {
            LOGGER.error("Failed to add medical record {}", e.getMessage(), e);
            throw e;
        }
    }

    //update medical record
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMedicalRecord(@PathParam("id") int id, MedicalRecord medicalRecord) {
        try {
            //check if medical record Id exists
            if (MedicalRecordDAO.recordExists(medicalRecord.getRecordId())){
                LOGGER.warn("Medical record Id already exists", medicalRecord.getRecordId());
                throw new AlreadyExistsException("Medical record Id " + medicalRecord.getRecordId() + " already exists");
            }
            //check if data is valid
            if (!MedicalRecordDAO.isValidRecord(medicalRecord)) {
                LOGGER.warn("Invalid data for medical record during update: {}", medicalRecord.getRecordId());
                throw new InvalidDataException("Invalid data for medical record");
            }
    
            //update medical record
            MedicalRecordDAO.updateMedicalRecord(id,medicalRecord);
           
            LOGGER.info("Medical record updated successfully: {}", medicalRecord);
            return Response.status(Response.Status.OK)
                    .entity("Medical Record with Id: " + medicalRecord.getRecordId() + "updated successfully")
                    .build();
        } catch (NotFoundException | InvalidDataException e) {
            LOGGER.error("Failed to update record: {}", e.getMessage(), e);
            throw e;
        }    
    }

    //delete medical record
    @DELETE
    @Path("/delete/{id}")
    public Response deleteMedicalRecord(@PathParam("id") int id) {
        try {
            MedicalRecord medicalRecord = MedicalRecordDAO.getMedicalRecordById(id);
            LOGGER.info("Successfully deleted medical record with Id: {}", medicalRecord);
            MedicalRecordDAO.deleteMedicalRecord(id);
            return Response.status(Response.Status.OK)
                    .entity("Medical Record with record Id: "+ id + " deleted successfully")
                    .build();
        } catch (NotFoundException e){
            LOGGER.error("Failed to delete medical record: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to delete medical record: {}", e.getMessage(), e);
            throw new WebApplicationException("Internal Server Error", 
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
}
