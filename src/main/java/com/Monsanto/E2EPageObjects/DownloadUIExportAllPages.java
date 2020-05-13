package com.Monsanto.E2EPageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DownloadUIExportAllPages {	
	
	public By lnk_Download = By.xpath("//a[@id='download']/ancestor::ul/parent::div/a/i");
	public By lnk_Download_GrowerOpp_DealerList = By.xpath("//section[@id='dealer-table-list']//a[@id='download']/ancestor::ul/parent::div/a/i");
	public By lnk_Download_GrowerOpp_GrowerList = By.xpath("//section[@id='grower-table-list']//a[@id='download']/ancestor::ul/parent::div/a/i");
	
	public By option_Download = By.xpath("//a[@id='download']");
	public By option_Download_GrowerOpp_DealerList= By.xpath("//section[@id='dealer-table-list']//a[@id='download']");
	public By option_Download_GrowerOpp_GrowerList= By.xpath("//section[@id='grower-table-list']//a[@id='download']");

	public List<WebElement> get_lnk_Download(WebDriver driver) {
		return driver.findElements(lnk_Download);         
	}
	public WebElement get_lnk_Download_DealerList(WebDriver driver) {
		return driver.findElement(lnk_Download_GrowerOpp_DealerList);         
	}
	public WebElement get_lnk_Download_GrowerList(WebDriver driver) {
		return driver.findElement(lnk_Download_GrowerOpp_GrowerList);         
	}
	public List<WebElement> get_option_Download(WebDriver driver) {
		return driver.findElements(option_Download);         
	}
	public WebElement get_lnk_DownloadCp(WebDriver driver) {
		return driver.findElement(option_Download);         
	}
	public WebElement get_option_Download_DealerList(WebDriver driver) {
		return driver.findElement(option_Download_GrowerOpp_DealerList);         
	}
	public WebElement get_option_Download_GrowerList(WebDriver driver) {
		return driver.findElement(option_Download_GrowerOpp_GrowerList);         
	}
}
