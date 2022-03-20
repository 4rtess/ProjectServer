package com.example.ProjectServer.parser;

import com.example.ProjectServer.entity.Day;
import com.example.ProjectServer.entity.Lesson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class VolgeduParser {

    private WebDriver driver;


    /** Singleton because site, which should be parsed, very-very bad. It can crash, when 200 users use it...
     * And this project for me.
     * In plans were create android app, where I can see school timetable and homework, without login into e-diary. **/
    public VolgeduParser() {
            ChromeOptions opt = new ChromeOptions();
            opt.addArguments("--headless");
            driver = new ChromeDriver(opt);
    }
    public List<Day> getDays(String username,String password) {
        login(driver,username,password);
        diary(driver);
        return getTable(driver);
    }

    /** Log in **/
    private void login(WebDriver driver, String username, String password) {
        driver.get("https://sgo.volganet.ru/about.html");
        while(true) {
            try {
                // Set my school
                WebElement district = driver.findElement(By.id("provinces"));
                district.click();
                district.sendKeys("Городской округ Волжский");
                WebElement type = driver.findElement(By.id("funcs"));
                type.click();
                type.sendKeys("Общеобразовательная");
                WebElement school = driver.findElement(By.id("schools"));
                school.click();
                school.sendKeys("МОУ \"Лицей № 1\"");

                WebElement usernameElem = driver.findElement(By.xpath("//input[contains(@name, 'UN')]"));
                usernameElem.sendKeys(username);

                WebElement passwordElem = driver.findElement(By.xpath("//input[contains(@name, 'PW')]"));
                passwordElem.sendKeys(password);

                WebElement buttonLogin = driver.findElements(By.xpath("//a[contains(@class, 'button-login button-login-marker')]")).get(0);
                buttonLogin.click();
                break;
            } catch (Exception ignored) {
            }
        } //#TODO bad realisation, fix if have time, but it works okay.
        for(int i = 0;i<50;i++) {
            try{Thread.sleep(10);}catch (Exception ignored) {}
            if (driver.getCurrentUrl().equals("https://sgo.volganet.ru/asp/SecurityWarning.asp")) {
                WebElement next = driver.findElement(By.xpath("//button[contains(@title, 'Продолжить')]"));
                next.click();
            }
        }
    }

    /** Open diary **/
    private void diary(WebDriver driver) {

        WebElement diaryLi = driver.findElements(By.xpath("//li[contains(@class,'dropdown')]")).get(1);
        diaryLi.click();
        WebElement diary = driver.findElements(By.xpath("//a[contains(text(),'Дневник')]")).get(1);
        diary.click();
    }

    /** Get timetable **/
    private List<Day> getTable(WebDriver driver) {
        WebElement wait = driver.findElement(By.xpath("//div[contains(@class,'apploader')]"));
        while (wait.isDisplayed()){}
        List<WebElement> daysTables = driver.findElements(By.xpath(".//div[contains(@class,'day_table')]/table[position()>1]"));
        List<Day> days = new ArrayList<>();
        String dayOfWeek="";
        for (WebElement daysTable : daysTables) {

            WebElement dayOfWeekEl = daysTable.findElement(By.xpath(".//td[contains(@rowspan, '10')]"));
            System.out.println(dayOfWeekEl.getText());
            try {dayOfWeek = dayOfWeekEl.getText().substring(0, dayOfWeekEl.getText().indexOf(','));}
            catch (Exception e) {dayOfWeek="Понедельник";}



            List<WebElement> tr = daysTable.findElements(By.xpath(".//tr"));
            tr.remove(0);

            List<WebElement> td = new ArrayList<>();
            for (WebElement e : tr)
                td.addAll(e.findElements(By.xpath(".//td")));

            List<String> num = new ArrayList<>();
            List<String> lesson = new ArrayList<>();
            List<String> homework = new ArrayList<>();
            for (int i = 0; i < td.size(); i += 4) {
                num.add(td.get(i).getText());
            }// 0+4 - number of lesson | 1+4-lesson+time | 2+4 - homework
            for (int i = 1; i < td.size(); i += 4) {
                lesson.add(td.get(i).getText());
            }
            for (int i = 2; i < td.size(); i += 4) {
                String homeworkText = td.get(i).getText();
                try {
                    if (homeworkText.contains("смотри")) {
                        td.get(i).click();
                        for (int l = 0; l < 200; l++) {
                            if (driver.findElement(By.xpath("//div[contains(@class, 'modal-header')]"))
                                    .getText().equals("Информация о задании")) {
                                break;
                            }
                        }
                        homeworkText += "\n Подробности от учителя:" + driver.findElement(By.xpath("//div[contains(@class, 'form-group ng-scope')][1]/div")).getText();
                        driver.findElement(By.xpath("//div[@class='bootstrap-dialog-close-button']/button[@class='close']")).click();
                    }
                }catch (Exception e ) {}
                homework.add(homeworkText);
            }

            List<Lesson> lessons = new ArrayList<>();
            for (int i = 0; i < num.size(); i++) {
                String lessonTr = lesson.get(i);
                Lesson les;
                if (!lessonTr.isEmpty()) {
                    les = new Lesson(num.get(i), lessonTr.substring(0, lessonTr.indexOf('\n')),
                            lessonTr.substring(lessonTr.indexOf('\n') + 1, lessonTr.indexOf(',', lessonTr.indexOf('\n'))),
                            lessonTr.substring(lessonTr.indexOf(',', lessonTr.indexOf('\n')) + 1), homework.get(i));
                } else {
                    //No lesson
                    les = new Lesson(num.get(i), "", "", "", "");
                }
                lessons.add(les);
            }

            days.add(new Day(dayOfWeek, lessons));
        }
        leave(driver);
        return days;
    }
    private void leave(WebDriver driver) {
        WebElement leave = driver.findElement(By.xpath("//span[contains(@title, 'Выход')]"));
        leave.click();
        for(int i=0;i<100;i++) {
            try {
                WebElement agree = driver.findElement(By.xpath("//button[contains(text(),'Да')]"));
                agree.click();
            }catch (Exception ignored) {}
        }
        driver.quit();
    }

}
