package edu.brown.cs.amukhe12.maps.stars;

import edu.brown.cs.amukhe12.maps.csvparser.CSVEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock.
 */
public class MockPerson implements CSVEntry {

  private String firstName;
  private String lastName;
  private String gender;
  private String email;
  private String streetAddress;


  /**
   * mock constructor.
   */
  public MockPerson() {

  }

  @Override
  public void setFields(List<String> fields) throws Exception {
    if (fields.size() != 5) {
      throw new Exception("incorrect number of fields in csv row");
    }
    firstName = fields.get(0);
    lastName = fields.get(1);
    gender = fields.get(2);
    email = fields.get(3);
    streetAddress = fields.get(4);

  }


  @Override
  public List getFields() {
    ArrayList fields = new ArrayList();
    fields.add(firstName);
    fields.add(lastName);
    fields.add(gender);
    fields.add(email);
    fields.add(streetAddress);
    return fields;
  }

  @Override
  public String toString() {
    return "MockPerson{"
        + " firstName='" + firstName + '\''
        + ", lastName='" + lastName + '\''
        + ", gender='" + gender + '\''
        + ", email='" + email + '\''
        + ", streetAddress='" + streetAddress + '\''
        + '}';
  }
}
