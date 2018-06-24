package com.jobapplication;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;
import com.github.javafaker.Faker;
import static org.testng.Assert.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class personalInfoTests {
	WebDriver driver;
	String firstName;
	String lastName;
	int gender;
	String dateOfBirth;
	String email;
	String phoneNumber;
	String city;
	String state;
	String country;
	int annualSalary;
	List<String> technologies;
	int yearsOfExperience;
	String education;
	String github;
	List<String> certifications;
	String additionalSkills;
	Faker data = new Faker();
	
	@BeforeClass // runs once for all test
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().fullscreen();	
	}
	
	@BeforeMethod // runs before each @Test
	public void navigatToHomePage() {
		System.out.println("Navigating to homepage in @BeforeMethod");
		driver.get("https://forms.zohopublic.com/murodil/form/JobApplicationForm/formperma/kOqgtfkv1dMJ4Df6k4_mekBNfNLIconAHvfdIk3CJSQ");
		firstName = data.name().firstName();
		lastName = data.name().lastName();
		gender = data.number().numberBetween(1, 2);
		dateOfBirth = data.date().birthday().toString();
		email = "farhod_karimov@yahoo.com";//data.internet().emailAddress();
		phoneNumber = data.phoneNumber().cellPhone().replace(".", "");
		city = data.address().city();
		state = data.address().stateAbbr();
		country = data.address().country();
		annualSalary = data.number().numberBetween(60000, 150000);
		technologies = new ArrayList<>();
		technologies.add("Java-" + data.number().numberBetween(1, 4));
		technologies.add("HTML-" + data.number().numberBetween(1, 4));
		technologies.add("Selenium WebDriver-" + data.number().numberBetween(1, 4));
		technologies.add("Maven-" + data.number().numberBetween(1, 4));
		technologies.add("Git-" + data.number().numberBetween(1, 4));
		technologies.add("TestNG-" + data.number().numberBetween(1, 4));
		technologies.add("JUnit-" + data.number().numberBetween(1, 4));
		technologies.add("Cucumber-" + data.number().numberBetween(1, 4));
		technologies.add("API Automation-" + data.number().numberBetween(1, 4));
		technologies.add("JDBC-" + data.number().numberBetween(1, 4));
		technologies.add("SQL-" + data.number().numberBetween(1, 4));
		yearsOfExperience = data.number().numberBetween(1, 11);
		education = data.number().numberBetween(1, 4) + "";
		github = "https://github.com/farkhodkarimov";
		certifications = new ArrayList<>();
		certifications.add("Java OCA");
		certifications.add("AWS");
		certifications.add("Scrum Master");
		additionalSkills = data.job().keySkills();
	}
	
	@Test
	public void submitFullApplication() {
		driver.findElement(By.xpath("//input[@name='Name_First']")).sendKeys(firstName);
		driver.findElement(By.xpath("//input[@name='Name_Last']")).sendKeys(lastName);
		setGender(gender);
		setDateOfBirth(dateOfBirth);
		driver.findElement(By.xpath("//input[@name='Email']")).sendKeys(email);
		driver.findElement(By.xpath("//input[@name='countrycode']")).sendKeys(phoneNumber);
		driver.findElement(By.xpath("//input[@name='Address_City']")).sendKeys(city);
		driver.findElement(By.xpath("//input[@name='Address_Region']")).sendKeys(state);
		Select countryElem = new Select(driver.findElement(By.xpath("//select[@id='Address_Country']")));
		countryElem.selectByIndex(data.number().numberBetween(1, countryElem.getOptions().size()));
//hmwrk
		WebElement cntr = countryElem.getFirstSelectedOption();
		String cntry = cntr.getText();
//hmwrk
		driver.findElement(By.xpath("//input[@name='Number']")).sendKeys(String.valueOf(annualSalary)+Keys.TAB);
		verifySalaryCalculations(annualSalary);
		driver.findElement(By.xpath("//em[.=' Next ']")).click();
		
		// second page action
		setSkillset(technologies);
//hmwrk
		String techSkills = "";
		for (int i = 0; i < technologies.size(); i++) {
			techSkills += technologies.get(i).substring(0, technologies.get(i).length()-2) +
					" : " +
					driver.
					findElement(By.xpath("//table//tr[" + (i+1) + "]//input[@matrixselectval='true']")).
					getAttribute("columnvalue") + "  ";
		}
//hmwrk
		if(yearsOfExperience > 0) {
			driver.findElement(By.xpath("//a[@rating_value='" + yearsOfExperience + "']")).click();	
		}
		Select educationList = new Select(driver.findElement(By.xpath("//select[@name='Dropdown']")));
		educationList.selectByIndex(data.number().numberBetween(1, educationList.getOptions().size()));
		
		/*
		Homework:
			  1) Finish all step and click on Apply
			  2) Validate each value
			    IP address: goto google and search for what is my ip
			  3) Goto your email and find the email and click on it
			  by id. SDET Application #id
		*/
		// homework starts here
		driver.findElement(By.xpath("//input[@name='Website']")).sendKeys(github);
		setCertifications(certifications);
		driver.findElement(By.xpath("//textarea[@name='MultiLine']")).clear();
		driver.findElement(By.xpath("//textarea[@name='MultiLine']")).sendKeys(additionalSkills);
		driver.findElement(By.xpath("//em[.='Apply']")).click();
		
		String dear = driver.findElement(By.xpath("//div[contains (text(), 'Dear')]")).getText();
		assertEquals(dear.substring(5, dear.length()-1), firstName + " " + lastName);
		String ip = driver.findElement(By.xpath("//div[contains (text(), 'IP address')]")).getText();
		assertEquals(ip.substring(ip.length()-13), "73.154.189.99");
		
		String appId = driver.findElement(By.xpath("//div[contains (text(), 'Application ID')]")).getText();
		
		int g = driver.
				findElement(By.xpath("//div[contains (text(), 'Gender')]")).
				getText().
				contains("Male") ? 1 : 2;
		assertEquals(g, gender);
		
		String dob = driver.findElement(By.xpath("//div[contains (text(), 'Date of birth')]")).getText();
		String[] pieces = dateOfBirth.split(" ");
		String birthDay = pieces[2] + "-" + pieces[1] + "-" + pieces[5];
		assertEquals(dob.substring(dob.length()-11), birthDay);
		
		String em = driver.findElement(By.xpath("//div[contains (text(), 'Email')]")).getText();
		assertEquals(em.substring(7), email);
		
		String phone = driver.findElement(By.xpath("//div[contains (text(), 'Phone')]")).getText();
		assertEquals(phone.substring(7), phoneNumber);
		
		String address = driver.findElement(By.xpath("//div[contains (text(), 'Address')]")).getText();
		assertEquals(address.substring(9), city + ", " + state + ", " + cntry);
		
		String aSalary = driver.findElement(By.xpath("//div[contains (text(), 'Annual Salary')]")).getText();
		assertEquals(aSalary.substring(15), String.valueOf(annualSalary));
		
		String techs = driver.findElement(By.xpath("//div[contains (text(), 'Technologies')]")).getText();
		assertEquals(techs.substring(14), techSkills.trim());
		
		String yoe = driver.findElement(By.xpath("//div[contains (text(), 'Years of Experience')]")).getText();
		assertEquals(yoe.substring(21), String.valueOf(yearsOfExperience));
		
		driver.get("https://mail.yahoo.com/neo/launch?.src=ym&reason=myc");
		driver.findElement(By.xpath("//input[@id='login-username']")).sendKeys("farhod_karimov");
		driver.findElement(By.xpath("//input[@id='login-signin']")).click();
		driver.findElement(By.xpath("//input[@id='login-passwd']")).sendKeys("Pittsburgh2018PA");
		driver.findElement(By.xpath("//button[@id='login-signin']")).click();
		//driver.findElement(By.xpath("//a[@class='skip-now text-sm link-clicked']")).click();
		
		driver.findElement(By.xpath("//div[.='training@cybertekschool.com']")).click();
		String uniqueId = driver.findElement(By.xpath("//td[.='Unique ID']/following-sibling::td[2]")).getText();
		assertEquals(appId.substring(16), uniqueId);

	}
	
	public void setCertifications(List<String> cert) { // homework
		for (int i = 0; i < cert.size(); i++) {
			int num = data.number().numberBetween(0,1);
			if (num == 1) {
				driver.findElement(By.xpath("//input[@id='Checkbox_" + (i+1) + "']")).click();
			}			
		}
	}
	
	public void setSkillset(List<String> tech) {	
		for (String skill : tech) {
			String technology = skill.substring(0, skill.length()-2);
			int rate = Integer.parseInt(skill.substring(skill.length()-1));
			String level = "";
			switch(rate) {
				case 1:
					level = "Expert";
					break;
				case 2:
					level = "Proficient";
					break;
				case 3:
					level = "Beginner";
					break;
				default:
					fail(rate + " is not a valid level");
			}
			String xpath = "//input[@rowvalue='"+ technology +"' and @columnvalue='"+ level +"']";
			driver.findElement(By.xpath(xpath)).click();
		}	
	}
	
	public void verifySalaryCalculations(int annual) {
		String monthly = driver.findElement(By.xpath("//input[@name='Formula']")).getAttribute("value");
		String weekly = driver.findElement(By.xpath("//input[@name='Formula1']")).getAttribute("value");
		String hourly = driver.findElement(By.xpath("//input[@name='Formula2']")).getAttribute("value");
		System.out.println(monthly);
		System.out.println(weekly);
		System.out.println(hourly);
		
		DecimalFormat formatter = new DecimalFormat("#.##");
		
		assertEquals(Double.parseDouble(monthly),Double.parseDouble(formatter.format((double)annual/12.0)));
		assertEquals(Double.parseDouble(weekly),Double.parseDouble(formatter.format((double)annual/52.0)));
		assertEquals(Double.parseDouble(hourly),Double.parseDouble(formatter.format((double)annual/52.0/40.0)));
	}
	
	public void setDateOfBirth(String bday) {
		String[] pieces = bday.split(" ");
		String birthDay = pieces[2] + "-" + pieces[1] + "-" + pieces[5];
		driver.findElement(By.xpath("//input[@id='Date-date']")).sendKeys(birthDay);
	}
	
	public void setGender(int n) {
		if (n==1) {
			driver.findElement(By.xpath("//input[@value='Male']")).click();
		} else {
			driver.findElement(By.xpath("//input[@value='Female']")).click();
		}
	}

	@Test
	public void fullNameEmptyTest() {
		// first assert that you are on the correct page
		assertEquals(driver.getTitle(), "SDET Job Application");
		driver.findElement(By.xpath("//input[@elname='first']")).clear();
		driver.findElement(By.xpath("//input[@elname='last']")).clear();
		driver.findElement(By.xpath("//em[.=' Next ']")).click();
		
		// get the text and assert text equals to Enter a value for this field
		String nameError = driver.findElement(By.xpath("//p[@id='error-Name']")).getText();
		assertEquals(nameError, "Enter a value for this field.");
		
	}
	

}
