package album.tools;


import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public class JPEGUtilities {

	//----------------------------------------------
	static public BufferedImage load(InputStream stream) 
	//----------------------------------------------
	{
		BufferedImage image = null;
		try {
			JPEGImageDecoder decoder=JPEGCodec.createJPEGDecoder(stream);
			image = decoder.decodeAsBufferedImage();
		}
		catch (Exception e) {e.printStackTrace();}
		return image;
	}

	//----------------------------------------------
	static public byte[] toBytesArray(String location) 
	//----------------------------------------------
	{
		byte[] result = null;
		try {
			InputStream stream = new URL(location).openStream();
			result = new byte[stream.available()];
			stream.read(result);
		}
		catch (Exception e) {e.printStackTrace();}
		return result;
	}
}
