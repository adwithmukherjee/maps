package edu.brown.cs.amukhe12.maps.stars;

import edu.brown.cs.amukhe12.maps.EntryList;
import edu.brown.cs.amukhe12.maps.stars.MockPerson;

import java.util.ArrayList;
import java.util.List;

public class PersonList implements EntryList {

  List<MockPerson> _people;

  public PersonList() {
    _people = new ArrayList<>();
  }


  @Override
  public void addEntry(List<String> fields) throws Exception {
    MockPerson person = new MockPerson();
    person.setFields(fields);
    _people.add(person);
  }

  @Override
  public void clear() {
    _people.clear();
  }

  @Override
  public int size() {
    return _people.size();
  }

  @Override
  public boolean isEmpty() {
    return _people.isEmpty();
  }


  public List<MockPerson> getPeople() {
    return _people;
  }
}
