package com.lister.Project.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.businessobjects.samples.CRJavaHelper;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;

/**
 * Generate Crystal Report with the data in the database.
 */
public class GenerateReport {
	// H2 Database Connection Settings
	private static final String db_user="sa";
	private static final String db_pwd="";
	private static final String db_url="jdbc:h2:tcp://localhost/~/test";
	private static final String db_driver="org.h2.Driver";

	// Input Sample Template
	private final String sampleReport = "sample1.rpt";
	private final String sampleReportFilePath = getClass().getClassLoader().getResource(sampleReport).getPath();

	// Output Crystal Report
	private final String outputDirectory = "C://reports/";

	/**
	 * Takes in a sample Crystal Report, updates that sample with the data from the database and exports the updated
	 * Crystal Report.
	 *
	 * @return
	 * @throws ReportSDKException
	 * @throws IOException
	 */
	public boolean generate() throws ReportSDKException, IOException{

		ReportClientDocument rcd=new ReportClientDocument();
	    rcd.open(sampleReportFilePath, 0);
	    CRJavaHelper crj=new CRJavaHelper();
	    crj.changeDataSource(rcd, db_user, db_pwd, db_url, db_driver, "");
		crj.logonDataSource(rcd, db_user, db_pwd);
	    rcd.checkDatabaseAndUpdate();
	    rcd.refreshReportDocument();
	    System.out.println(rcd.path());
	    ByteArrayInputStream bais=(ByteArrayInputStream)rcd.getPrintOutputController().export(ReportExportFormat.PDF);
	    System.out.println("File loaded succesfully");
	    rcd.close();
	    try{
	    	String currentDate=new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_a'.pdf'").format(new Date());
	    	String fileName = "Employee"+"_" + currentDate;
	    	String savingFilePath = outputDirectory + fileName;
			File file = new File(savingFilePath);
		    FileOutputStream fileOutputStream = new FileOutputStream(file);
		    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bais.available());
		    byte[] byteArray=new byte[bais.available()];
			int x = bais.read(byteArray, 0, bais.available());
		    byteArrayOutputStream.write(byteArray, 0, x);
		    byteArrayOutputStream.writeTo(fileOutputStream);
		    fileOutputStream.close();
		    System.out.println("File exported succesfully");
		    return true;
	    }
	    catch(FileNotFoundException fnfe){
	    	fnfe.printStackTrace();
	    	return false;
	    }
	}
}
