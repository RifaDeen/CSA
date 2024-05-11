/**
 * @author rifad 20220701
 */

package modelClasses;

public class Patient extends Person {
  
    private String medicalHistory;
    private String healthStatus;

    //default constructor
    public Patient() {
    }

    //constructor
    public Patient( int id, String name, String contact, String address, String medicalHistory, String healthStatus) {
        super(id, name, contact, address);
        this.medicalHistory = medicalHistory;
        this.healthStatus = healthStatus;
    }
    
    //getters and setters
    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }   
}
