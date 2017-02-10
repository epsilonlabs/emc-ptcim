package org.eclipse.epsilon.emc.ptcim.benchmarking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.epsilon.emc.ptcim.PtcimCachedPropertyManager;
import org.eclipse.epsilon.emc.ptcim.PtcimModel;
import org.eclipse.epsilon.emc.ptcim.PtcimPropertyManager;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.evl.EvlModule;

import com.google.common.base.Stopwatch;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;

public class EvlStandalone {
	
	public static void main(String[] args) throws Exception {
		
		//String modelId = args[0];
		//String resourceStr = args[1];
		//String outputFile = args[2];

		String outputFile = "C:\\Users\\astal\\Documents\\test.txt";
		String resourceStr = "\\\\Enabler\\KB_WORK\\Examples\\Cabin\\14";
		String modelId = "51d7a85a-64dd-4c18-8652-ed88d032b8c9";

		EvlStandalone evlStndln = new EvlStandalone();
		StringProperties properties = new StringProperties();		
		boolean propertiesAttributesCacheEnabled = true;
		boolean propertiesValuesCacheEnabled = true;
		String[] info = evlStndln.modelReferenceToFields(resourceStr);
		String server = info[3];
		String repository = info[4];
		String reference = info[5];
		String version = info[6];
		//System.out.println("Server: " + server + " Repository: " + repository + " Name: " + reference + " Version: " + version);
		//Thread.sleep(10000);
		
		properties.put("modelRef", reference);
		properties.put("server", server);
		properties.put("repository", repository);
		properties.put("version", version);
		properties.put("fromSelection", false);
		properties.put("propertiesAttributesCacheEnabled", propertiesAttributesCacheEnabled);
		properties.put("propertiesValuesCacheEnabled", propertiesValuesCacheEnabled);
		properties.put("elementId", "");
		properties.put("readOnLoad", true);
		
		IRelativePathResolver resolver = null;
		
		File evlFile = new File("C:\\Users\\astal\\Documents\\Eclipse32ModellingWorkspaceSECTAIR\\org.eclipse.epsilon.emc.ptcim.benchmarking\\files\\evlFile.eol");
		
		EolModule m = new EolModule();
		PtcimModel p = new PtcimModel();
		
		//p.setServer(server);
		//p.setServer(server);
		//p.setRepository(repository);
		//p.setVersion(version);
		//p.setPropertiesAttributesCacheEnabled(propertiesAttributesCacheEnabled);
		//p.setPropertiesValuesCacheEnabled(propertiesValuesCacheEnabled);
		//p.setFromSelection(false);
		//p.setModelId(modelId);
		//p.load(); 
		p.load(properties,resolver);
		
		m.getContext().getModelRepository().addModel(p);
		
		m.parse(evlFile);
		long startTime = System.currentTimeMillis();
		m.execute();
		Thread.sleep(3000);
		long stopTime = System.currentTimeMillis();
		long timeElapsed = stopTime - startTime;
		System.out.println("Time passed: " + (timeElapsed));
		try {
		    FileWriter fw = new FileWriter(outputFile,true);
		    fw.write(modelId + "," + timeElapsed + "," + "Epsilon" + "\n");
		    fw.close();
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException: " + ioe.getMessage());
		}
	}
	
	private String[] modelReferenceToFields(String ref) {
		return ref.split("\\\\");
	}

}
