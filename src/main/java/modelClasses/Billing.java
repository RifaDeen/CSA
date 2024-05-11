/**
 * @author rifad 20220701
 */

package modelClasses;

public class Billing {
    private int refID;
    private Patient patient;
    private Doctor doctor;
    private double billingAmount;
    private boolean paid;

    //default constructor
    public Billing() {
    }

    //constructor
    public Billing(int refID, Patient patient, Doctor doctor, double billingAmount, boolean paid) {
        this.refID = refID;
        this.patient = patient;
        this.doctor = doctor;
        this.billingAmount = billingAmount;
        this.paid = paid;
    }

    //getters and setters
    public int getRefID() {
        return refID;
    }

    public void setRefID(int refID) {
        this.refID = refID;
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

    public double getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(double billingAmount) {
        this.billingAmount = billingAmount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
 
}
