/**
 * @author rifad 20220701
 */
package modelClasses;

public class Appointment {
    
    private int appointmentNo;
    private String date;
    private String time;
    private Patient patient;
    private Doctor doctor;

    public Appointment() {
    }

    public Appointment(int appointmentNo, String date, String time, Patient patient, Doctor doctor) {
        this.appointmentNo = appointmentNo;
        this.date = date;
        this.time = time;
        this.patient = patient;
        this.doctor = doctor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(int appointmentNo) {
        this.appointmentNo = appointmentNo;
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
}
