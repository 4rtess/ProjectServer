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

    private static VolgeduParser instance;
    private WebDriver driver;

    private  VolgeduParser() {}

    public static VolgeduParser getInstance() {
        if(instance==null) {
            instance = new VolgeduParser();
            ChromeOptions opt = new ChromeOptions();
            opt.addArguments("--headless");
            instance.driver=new ChromeDriver(opt);
        }
        return instance;
    }
    public List<Day> getDays(String username,String password) {
        System.out.println("200 OK\nusername:"+username+"\npassword:"+password);
        login(driver,username,password);
        diary(driver);
        return getTable(driver);
    }

    /** Залогиниться **/
    private void login(WebDriver driver, String username, String password) {
        driver.get("https://sgo.volganet.ru/about.html");
        while(true) {
            try {
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
        } //#TODO костыль исправить! что-то придумать не знаю че потом придумаю может и нет работает и нормально в целом ну и что что костыль все ок
        // потом в целом можно сделать отдельный поток под получение таблицы, а зачем, в любом случае это тест парсера,
        // если делать серверное приложение, то там другие костыли
        for(int i = 0;i<50;i++) {
            try{Thread.sleep(10);}catch (Exception ignored) {}
            if (driver.getCurrentUrl().equals("https://sgo.volganet.ru/asp/SecurityWarning.asp")) {
                WebElement next = driver.findElement(By.xpath("//button[contains(@title, 'Продолжить')]"));
                next.click();
            }
        }
    }

    /** Открыть вкладку Дневник **/
    private void diary(WebDriver driver) {

        WebElement diaryLi = driver.findElements(By.xpath("//li[contains(@class,'dropdown')]")).get(1);
        diaryLi.click();
        WebElement diary = driver.findElements(By.xpath("//a[contains(text(),'Дневник')]")).get(1);
        diary.click();
    }

    /** Получить расписание **/
    private List<Day> getTable(WebDriver driver) {
        WebElement wait = driver.findElement(By.xpath("//div[contains(@class,'apploader')]"));
        while (wait.isDisplayed()){}
        List<WebElement> daysTables = driver.findElements(By.xpath(".//div[contains(@class,'day_table')]/table[position()>1]"));
        List<Day> days = new ArrayList<>();
        String dayOfWeek="";
        for(int l = 0; l < daysTables.size();l++) {

            WebElement dayOfWeekEl = daysTables.get(l).findElement(By.xpath(".//td[contains(@class,'day_name hidden-mobile day_name')]"));
            //System.out.println(dayOfWeekEl.getText());
            dayOfWeek=dayOfWeekEl.getText().substring(0,dayOfWeekEl.getText().indexOf(','));


            List<WebElement> tr = daysTables.get(l).findElements(By.xpath(".//tr"));
            tr.remove(0);

            List<WebElement> td = new ArrayList<>();
            for (WebElement e : tr)
                td.addAll(e.findElements(By.xpath(".//td")));

            List<String> num = new ArrayList<>();
            List<String> lesson = new ArrayList<>();
            List<String> homework = new ArrayList<>();
            for (int i = 0; i < td.size(); i += 4) {
                num.add(td.get(i).getText());
            }// 0+4 - номер урока | 1+4-урок+время | 2+4 - дз
            for (int i = 1; i < td.size(); i += 4) {
                lesson.add(td.get(i).getText());
            }
            for (int i = 2; i < td.size(); i += 4) {
                homework.add(td.get(i).getText());
            }

            List<Lesson> lessons = new ArrayList<>();
            for(int i = 0;i<num.size();i++){
                String lntr = lesson.get(i);
                Lesson les;
                if(!lntr.isEmpty()) {
                    les = new Lesson(num.get(i), lntr.substring(0,lntr.indexOf('\n')),
                            lntr.substring(lntr.indexOf('\n')+1, lntr.indexOf(',',lntr.indexOf('\n'))),
                            lntr.substring(lntr.indexOf(',',lntr.indexOf('\n'))+1), homework.get(i));
                }
                else
                    les = new Lesson(num.get(i),"","","","");
                lessons.add(les);
            }

            days.add(new Day(dayOfWeek,lessons));
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
    }

}
