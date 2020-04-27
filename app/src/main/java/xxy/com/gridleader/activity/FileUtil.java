package xxy.com.gridleader.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileUtil {
	public List<Bell> getCustomBell(String path){
		List<Bell> list = new ArrayList<Bell>();
		File folder  = new File(path);
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			Bell bell = new Bell();
			bell.setBellName(files[i].getName());
			bell.setBellPath(files[i].getAbsolutePath());
			list.add(bell);
		}
		return list;
	}
}
