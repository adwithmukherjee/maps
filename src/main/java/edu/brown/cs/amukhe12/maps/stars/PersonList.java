package edu.brown.cs.amukhe12.maps.stars;

import edu.brown.cs.amukhe12.maps.EntryList;


import java.util.ArrayList;
import java.util.List;

public class PersonList implements EntryList {

  private List<MockPerson> people;

  public PersonList() {
    people = new ArrayList<>();
  }


  @Override
  public void addEntry(List<String> fields) throws Exception {
    MockPerson person = new MockPerson();
    person.setFields(fields);
    people.add(person);
  }

  @Override
  public void clear() {
    people.clear();
  }

  @Override
  public int size() {
    return people.size();
  }

  @Override
  public boolean isEmpty() {
    return people.isEmpty();
  }


  public List<MockPerson> getPeople() {
    return people;
  }
}
