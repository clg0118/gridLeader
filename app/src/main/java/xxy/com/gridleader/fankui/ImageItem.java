package xxy.com.gridleader.fankui;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;


public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	private Bitmap bitmap;
	public boolean isSelected = false;

	public void setFile(File file) {
		this.file = file;
	}

	private File file;


	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public Bitmap getBitmap() {		
		if(bitmap == null){
			try {
				bitmap = Bimp.revitionImageSize(imagePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public File getFile(String name) {

		if (file == null){
			try {
				file = Bimp.revitionImageFile(getBitmap(),name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	
}
