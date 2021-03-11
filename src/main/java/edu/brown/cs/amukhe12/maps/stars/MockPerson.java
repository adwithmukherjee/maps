package edu.brown.cs.amukhe12.maps.stars;

import edu.brown.cs.amukhe12.maps.csvparser.CSVEntry;

import java.util.ArrayList;
import java.util.List;

public class MockPerson implements CSVEntry {

  private String _firstName;
  private String _lastName;
  private String _gender;
  private String _email;
  private String _streetAddress;


  public MockPerson() {

  }

  @Override
  public void setFields(List<String> fields) throws Exception {
    if (fields.size() != 5) {
      throw new Exception("incorrect number of fields in csv row");
    }
    _firstName = fields.get(0);
    _lastName = fields.get(1);
    _gender = fields.get(2);
    _email = fields.get(3);
    _streetAddress = fields.get(4);

  }


  @Override
  public List getFields() {
    ArrayList fields = new ArrayList();
    fields.add(_firstName);
    fields.add(_lastName);
    fields.add(_gender);
    fields.add(_email);
    fields.add(_streetAddress);
    return fields;
  }

  @Override
  public String toString() {
    return "MockPerson{"
        + " firstName='" + _firstName + '\''
        + ", lastName='" + _lastName + '\''
        + ", gender='" + _gender + '\''
        + ", email='" + _email + '\''
        + ", streetAddress='" + _streetAddress + '\''
        + '}';
  }
}
