package com.mobanker.selenium.tianya;

import com.mobanker.selenium.ReadWord;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.util.Scanner;

/**
 * Created by lixiaofeng on 2017-7-23.
 *  天涯论坛自动发帖功能
 *
 */
public class Tianya {

    /**
     *  天涯论坛发帖主方法调用
     * @param args
     */
    public static void main(String[] args) throws Exception{

        //控制台扫描
        //Scanner scanner = new Scanner(System.in);
        System.out.println("输入登录用户名");
        //读取一个字符串：用户
        String usersName = "lxftuiguang";//scanner.next();

        System.out.println("输入用户密码");
        //读取一个字符串：密码
        String pwd = "lixiaofeng520";//scanner.next();

        //谷歌浏览器驱动加载
        System.setProperty("webdriver.chrome.driver", "F:\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //把浏览器设置到最大状态
        //driver.manage().window().setSize(new Dimension(600, 400));
        driver.get("http://focus.tianya.cn/");


        //设置等待时间为10秒
        WebDriverWait webDriverWait = new WebDriverWait(driver,5);
        //等元素加载出来，再找登录按钮的元素
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("js_login")));
        //点击登录按钮
        WebElement webElement = driver.findElement(By.id("js_login"));
        webElement.click();

        //用户登录
        login(driver, usersName, pwd);

    }

    /**
     * description：天涯论坛，用户自动登录
     * @param driver 打开浏览器驱动
     * @param userName 用户
     * @param pwd 密码
     */
    public static void login(WebDriver driver, String userName, String pwd) throws Exception{
        //设置等待时间为10秒
        WebDriverWait webDriverWait = new WebDriverWait(driver,3);

        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("vwriter")));
        //输入用户名
        WebElement name = driver.findElement(By.name("vwriter"));
        name.sendKeys(userName);

        //输入密码
        //webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("vpassword")));
        WebElement password = driver.findElement(By.name("vpassword"));
        password.sendKeys(pwd);

        //点击登录，实现登录
        WebElement login = driver.findElement(By.className("loginWin-submit-btn"));
        login.click();

        //直接加载你要想发表的论坛链接,跳转到这个链接
        driver.get("http://bbs.tianya.cn/list-170-1.shtml");

        //找到发帖按钮
        WebElement buttonPost = driver.findElement(By.cssSelector(".btn-gray.btn-post"));
        buttonPost.click();

        //论坛发帖：填写标题
        WebElement bbsTitle = driver.findElement(By.cssSelector(".bbsTitle.fl"));
        bbsTitle.sendKeys("网上网络工作室刚成立，有志向的朋友可以加入！！");

        //读取到word排版的文案
        //webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("textAreaContainer")));
        WebElement textAreaContainer = driver.findElement(By.id("textAreaContainer"));
        //读取word文档的数据
        String area = ReadWord.readWord();
        textAreaContainer.sendKeys(area);

        //设置图片
        textAreaContainer.sendKeys("F:\\33.bmp");

        //设置为原创
        WebElement isself = driver.findElement(By.name("isSelf"));
        isself.click();

        //点击发布
        WebElement saveBtn = driver.findElement(By.cssSelector("save-btn fr"));
        //saveBtn.click();


    }






}
