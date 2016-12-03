/**
 * 
 */
package lifecycle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Poehler
 *
 */
public class MyImageHandler {

	public static ImageRegistry imageRegistry;
	private static List<String> imageList = new ArrayList<String>();
	protected static final String IMAGES_DIRECTORY = "/icons/16x16/";
	protected static final String BUNDLE_DIRECTORY = "com.hydra.project";

	public static void iconListeErzeugen(){
		System.out.println("Icons werden gesucht und geladen");
		imageRegistry = getLocalImageRegistry();
	}


	/**
	 * Test loading the image descriptors.
	 * @return 
	 */
	public static ImageRegistry getLocalImageRegistry() {
//		imageList = new ArrayList <String>();
		ImageRegistry reg = new ImageRegistry();
		String filename ="";	
		
		Bundle bundle = FrameworkUtil.getBundle(MyImageHandler.class);
		Enumeration<?> bundleEntries = bundle.getEntryPaths(IMAGES_DIRECTORY);
		int counter = 0;
		
		while (bundleEntries.hasMoreElements()) {
			counter = counter+1;
//			if (counter > 9) break;
			ImageDescriptor descriptor;
			String localImagePath = (String) bundleEntries.nextElement();
			URL[] files = FileLocator.findEntries(bundle, new Path(localImagePath));

			for (int i = 0; i < files.length; i++) {
				// Skip any subdirectories added by version control
//				if (files[i].getPath().lastIndexOf('.') < 0)
//					continue;

				try {
					filename = FileLocator.toFileURL(files[i]).getFile();

					int index = filename.lastIndexOf("/")+1;
					filename = filename.substring(index);
					
					if (!(filename == null)){
						imageList.add(filename); //Liste alle eingelagerten Images erstellen
					}
					
					URL urlneu = Platform.getBundle(BUNDLE_DIRECTORY).getEntry(IMAGES_DIRECTORY + filename);
					descriptor = ImageDescriptor.createFromURL(urlneu);					

					if (descriptor == null){
						System.out.println("Zu ladender descriptor is leer:" + filename);
					} else{
//						System.out.println("Dateipfad:" + filename);
					}
					
					
				} catch (IOException e) {
//					fail(e.getLocalizedMessage());
					continue;
				}
//				System.out.println("filename :" + filename);
				Image image1 = reg.get(filename);  //prüfen, ob schon vorhanden
				if (image1 == null){
					reg.put(filename, descriptor);
				}
			}
		}
		System.out.println("geladenene Bilder :" + counter);
		return reg;	
	}
	
	
	/**
	 * @return the imageRegistry
	 */
	public static ImageRegistry getImageRegistry() {
		return imageRegistry;
	}



	/**
	 * @return the Image
	 * @param the Name of the Image
	 */
	public static Image getImage(String string) {
		Image image = imageRegistry.get(string);
		if (image == null) {
//			image = imageRegistry.get("sample.gif");
//			System.out.println("Image nicht gefunden und ersetzt durch Standard");
		}
		
		return image;
	}
	
	/**
	 * @return the check
	 * @param the string of the Image
	 */
	public static boolean imageExists(String string) {
		Image image = imageRegistry.get(string);
		boolean check;
		if (image == null) check=false;
		else check=true;
		
		return check;
	}
	
	/**
	 * @return the Descriptor
	 * @param the Name of the Image
	 */
	public static ImageDescriptor getImageDescriptor(String string) {
		
		return imageRegistry.getDescriptor(string);
	}


	/**
	 * @return the imageList
	 * liefert eine Liste mit allen Namen der Images
	 */
	public static List<String> getImageList() {
		return imageList;
	}
	
}
