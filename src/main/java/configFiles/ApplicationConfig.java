/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configFiles;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author rifad
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(exceptionClasses.AlreadyExistsExceptionMapper.class);
        resources.add(exceptionClasses.InvalidDataExceptionMapper.class);
        resources.add(exceptionClasses.NotFoundExceptionMapper.class);
        resources.add(resourceClasses.AppointmentResource.class);
        resources.add(resourceClasses.BillingResource.class);
        resources.add(resourceClasses.DoctorResource.class);
        resources.add(resourceClasses.MedicalRecordResource.class);
        resources.add(resourceClasses.PatientResource.class);
        resources.add(resourceClasses.PersonResource.class);
        resources.add(resourceClasses.PrescriptionResource.class);
    }
    
}
