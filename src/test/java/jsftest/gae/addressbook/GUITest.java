package jsftest.gae.addressbook;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.assertTrue;


public class GUITest {

    private static final String BASE_URL = "http://localhost:8080";
    private WebDriver driver = new HtmlUnitDriver();


    @Test
    public void addAndRemoveUserSucceeds() throws Exception {
        logIn();
        deleteAllUsers();
        createUser();
        checkUserExists();
        removeUser();
        logOut();
    }

    private void deleteAllUsers() {
        listContacts();
        driver.findElement(By.id("listContactsForm:removeAllButton")).click();
    }


    private void removeUser() {
        listContacts();
        driver.findElement(By.id("listContactsForm:contactTable:0:removeButton")).click();
    }

    private void createUser() {

        driver.findElement(By.linkText("Home")).click();
        assertTrue(driver.getPageSource().contains("New contact"));
        driver.findElement(By.id("contactForm:name")).sendKeys("Palace");
        driver.findElement(By.id("contactForm:email")).sendKeys("Palace@palace.se");
        driver.findElement(By.id("contactForm:street")).sendKeys("palace");

        driver.findElement(By.id("contactForm:city")).sendKeys("Stockholm");
        driver.findElement(By.id("contactForm:country")).sendKeys("Sweden");
        driver.findElement(By.id("contactForm:createContact")).click();
    }

    private void logOut() {
        driver.findElement(By.linkText("Sign out")).click();
        assertTrue(driver.getPageSource().contains("Not logged in"));
    }

    private void checkUserExists() {
        listContacts();
        assertTrue(driver.getPageSource().contains("Palace"));
    }

    private void listContacts() {
        driver.findElement(By.linkText("Address Book")).click();
        driver.findElement(By.linkText("List Contacts")).click();
    }

    private void logIn() {
        driver.get(BASE_URL);
        assertTrue(driver.getPageSource().contains("Not logged in"));
        driver.findElement(By.xpath("//input[@name='action' and @value='Log In']")).click();
    }
}
