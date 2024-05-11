/**
 * @author rifad 20220701
 */
package modelClasses;

public class MedicalRecord {
   
    private int recordId;
    private Patient patient;
    private String diagnosis;
    private String treatment;

    //default constructor
    public MedicalRecord() {
    }

    //constructor
    public MedicalRecord(int recordId, Patient patient, String diagnosis, String treatment) {
        this.recordId = recordId;
        this.patient = patient;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }

    //getters and setters
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
      
}
