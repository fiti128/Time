package ru.retbansk.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Properties;

import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


import org.apache.log4j.Logger;

public class UsefulMethods {
	protected static Logger logger = Logger.getLogger("service");
	public static Properties loadProperties() throws Exception {
		Properties prop = new Properties();
		InputStream inputStream = null;
		// At first, program will try to read external properties
		try {
			inputStream = new FileInputStream("email.properties");
			prop.load(inputStream);
		} catch (FileNotFoundException e) {
		}
		// If nothing there - internal
		if (inputStream == null)
			inputStream = prop.getClass().getResourceAsStream(
					"/email.properties");

		prop.load(inputStream);
		if (inputStream != null)
			inputStream.close();
		try {
			if (prop.getProperty("continue").equals("no")){
				logger.info("Program was stopped by the User");
				System.exit(0);
			}
		} catch (Exception e) {
			logger.info("Continue parameter is not defined");
		}
		return prop;
	}
	public static String prettyFormat(String input, int indent) {
        try
        {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // This statement works with JDK 6
            transformerFactory.setAttribute("indent-number", indent);
             
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        }
        catch (Throwable e)
        {
            // You'll come here if you are using JDK 1.5
            // you are getting an the following exeption
            // java.lang.IllegalArgumentException: Not supported: indent-number
            // Use this code (Set the output property in transformer.
            try
            {
                Source xmlInput = new StreamSource(new StringReader(input));
                StringWriter stringWriter = new StringWriter();
                StreamResult xmlOutput = new StreamResult(stringWriter);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
                transformer.transform(xmlInput, xmlOutput);
                return xmlOutput.getWriter().toString();
            }
            catch(Throwable t)
            {
                return input;
            }
        }
    }
 
    public static String prettyFormat(String input) {
        return prettyFormat(input, 2);
    }
}
