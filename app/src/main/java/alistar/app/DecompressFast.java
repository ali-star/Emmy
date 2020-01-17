package alistar.app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import com.readystatesoftware.notificationlog.*;

public class DecompressFast {



	private String _zipFile; 
	private String _location; 
	private String folder;

	public DecompressFast(String zipFile, String location) { 
		_zipFile = zipFile; 
		_location = location; 

		_dirChecker(""); 
	} 

	public void unzip() { 
		try  { 
			FileInputStream fin = new FileInputStream(_zipFile); 
			ZipInputStream zin = new ZipInputStream(fin); 
			ZipEntry ze = null;
			int counter = 0;
			while ((ze = zin.getNextEntry()) != null) {
				counter++;
				if(counter==1)
				{
					folder = ze.getName();
				}
				Log.v("Decompress", "Unzipping " + ze.getName()); 

				if(ze.isDirectory()) { 
					_dirChecker(ze.getName()); 
				} else { 
					FileOutputStream fout = new FileOutputStream(_location + ze.getName()); 
					BufferedOutputStream bufout = new BufferedOutputStream(fout);
					byte[] buffer = new byte[1024];
					int read = 0;
					while ((read = zin.read(buffer)) != -1) {
						bufout.write(buffer, 0, read);
					}




					bufout.close();

					zin.closeEntry(); 
					fout.close(); 
				} 

			} 
			zin.close(); 


			Log.d("Unzip", "Unzipping complete. path :  " +_location );
		} catch(Exception e) { 
			Log.e("Decompress", "unzip", e); 

			Log.d("Unzip", "Unzipping failed");
		} 

	} 

	private void _dirChecker(String dir) { 
		File f = new File(_location + dir); 

		if(!f.isDirectory()) { 
			f.mkdirs(); 
		} 
	}
	
	public String getFolderName()
	{
		return folder;
	}


}
