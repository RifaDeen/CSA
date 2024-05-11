/**
 * @author rifad 20220701
 */

package modelClasses;

public class Doctor extends Person {
    
    private String specialization;
    
    //constructor
    public Doctor(int id, String name, String contact, String address, String specialization) {
        super(id, name, contact, address);
        this.specialization = specialization;
    }
    
    
    //default constructor
    public Doctor() {
    }

    //getters and setters
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
  
}
