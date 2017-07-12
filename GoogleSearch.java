package com.datalicious;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class GoogleSearch {

	public static void main(String[] args) throws IOException, ScriptException {
		// PhanthomJs Driver initialization using selenium

		File file = new File("C://Users//Gangadhar//Desktop//Phantom Js//phantomjs-2.1.1-windows//bin//phantomjs.exe");
		System.setProperty("phantomjs.binary.path", file.getAbsolutePath());
		WebDriver driver = new PhantomJSDriver();
		driver.get("https://www.google.co.in");
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
		driver.findElement(By.name("q")).sendKeys("Datalicious");
		driver.findElement(By.name("q")).submit();
		driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
		driver.findElement(By.linkText("Datalicious: Marketing Data Specialists")).click();
		driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
		String pageUrl = driver.getCurrentUrl();
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(src,
				new File("C://Users//Gangadhar//Desktop//Phantom Js//phantomjs-2.1.1-windows//bin//test.png"), true);
		
		PhantomJSDriver pjs = new PhantomJSDriver();
		
		//Phanthom script for finding requests
		pjs.executePhantomJS(
				"var resourceWait  = 300,maxRenderWait = 10000,url= 'https://www.datalicious.com';var page= require('webpage').create(),"
						+ "count= 128,forcedRenderTimeout,renderTimeout; "
						+ "var fs=require('fs');var path='E:/output.txt';var content ='';var content1 ='';" + ";"
						+ "page.viewportSize = { width: 1280, height : 1024 };function doRender() {"
						+ "page.render('datalicious.png');phantom.exit();}page.onResourceRequested = function (req) {count += 1;"
						+ "console.log('> ' + req.id + ' - ' + req.url); content = content + req.id;content1 = content1 + req.url;"
						+ "fs.write(path, content + content1, 'w');"
						+ "clearTimeout(renderTimeout);};page.onResourceReceived = function (res) {"
						+ "if (!res.stage || res.stage === 'end') {count -= 1;"
						+ "console.log(res.id + ' ' + res.status + ' - ' + res.url);if (count === 0) {renderTimeout = setTimeout(doRender, resourceWait);"
						+ "}}};page.open(url, function (status) {if (status !== " + "success"
						+ ") {console.log('Unable to load url');phantom.exit();"
						+ "} else {forcedRenderTimeout = setTimeout(function () {console.log(count);doRender();}, maxRenderWait);}});");
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new FileReader("E:/output.txt"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			if (everything.contains("https://www.google-analytics.com")) {
				System.out.println("A request was made to google analytics");
			} else {
				System.out.println("A request does not made to google analytics");
			}

			if (everything.contains("https://dc.optimahub.com")) {
				System.out.println("A request was made to host optimahub");
			} else {
				System.out.println("A request does not made to host optimahub");
			}
			System.out.println(everything);
			String dt = "", dp = "";
			Pattern pattern = Pattern.compile("https://www.google-analytics.com/r/collect(.*?)&vp");
			Matcher matcher = pattern.matcher(everything);
			if (matcher.find()) {
				String gooAn = matcher.group(1);
				Pattern pattern1 = Pattern.compile("dt=(.*?)");
				Matcher matcher1 = pattern1.matcher(gooAn);
				if (matcher1.find()) {
					dt = matcher1.group(0);
					System.out.println(matcher1.group(0));
				}
			} else {
				dt = "dt not found";
			}
			pattern = Pattern.compile("dp=(.*?)");
			matcher = pattern.matcher(everything);
			if (matcher.find()) {
				dp = matcher.group(1);
			} else {
				dp = "dp not found";
			}
			PrintWriter pw = new PrintWriter(new File("E:/test.csv"));
			StringBuilder sbBuil = new StringBuilder();
			sbBuil.append("DT");
			sbBuil.append(',');
			sbBuil.append("DP");
			sbBuil.append('\n');

			sbBuil.append(dt);
			sbBuil.append(',');
			sbBuil.append(dp);
			sbBuil.append('\n');
			pw.write(sbBuil.toString());
			pw.close();
		} finally {
			br.close();
		}
	}
}
