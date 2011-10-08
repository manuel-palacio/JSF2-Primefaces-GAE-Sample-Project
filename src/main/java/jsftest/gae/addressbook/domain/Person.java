package jsftest.gae.addressbook.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Palace
 * Date: 1/4/11
 * Time: 9:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class Person {


    private String name, lastName;
    private int age;

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Name: " + name + " Age: " + age;
    }
}
