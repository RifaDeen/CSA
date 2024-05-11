/**
 * @author rifa 20220701
 */

package modelClasses;

public class Prescription {
    
    private int prescriptionId;
    private Patient patient;
    private Doctor doctor;
    private String medication;
    private String dosage;
    private String instructions;
    private int duration;

    //default constructor
    public Prescription() {
    }

    //constructor
    public Prescription(int precriptionId, Patient patient, Doctor doctor, String medication, String dosage, String instructions, int duration) {
        this.prescriptionId = precriptionId;
        this.patient = patient;
        this.doctor = doctor;
        this.medication = medication;
        this.dosage = dosage;
        this.instructions = instructions;
        this.duration = duration;
    }

    //getters and setters
    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int precriptionId) {
        this.prescriptionId = precriptionId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
  
}
