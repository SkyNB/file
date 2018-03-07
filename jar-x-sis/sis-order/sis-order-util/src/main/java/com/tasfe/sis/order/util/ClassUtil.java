package com.tasfe.sis.order.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tasfe.sis.Constant;

public class ClassUtil {

	private static Logger logger = LoggerFactory.getLogger(ClassUtil.class);
	
	public static List<Class> getAllClassByInterface(Class c) {
		List returnClassList = new ArrayList<Class>();
		// 判断是不是接口,不是接口不作处理
		if (c.isInterface()) {
//			String packageName = c.getPackage().getName(); // 获得当前包名
			String packageName = Constant.class.getPackage().getName();
			logger.info("packageName="+packageName);
			try {
				List<Class> allClass = getClasses(packageName);// 获得当前包以及子包下的所有类
				logger.info("allClass="+allClass.size());
				
				// 判断是否是一个接口
				for (int i = 0; i < allClass.size(); i++) {
					logger.info("allClass="+allClass.get(i).getSimpleName());
					if (c.isAssignableFrom(allClass.get(i))) {
						if (!c.equals(allClass.get(i))) {
							logger.info("allClass1="+allClass.get(i).getSimpleName());
							returnClassList.add(allClass.get(i));
						}
					}
				}
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
		return returnClassList;
	}

	/**
	 * 
	 * @Description: 根据包名获得该包以及子包下的所有类不查找jar包中的
	 * @param pageName
	 *            包名
	 * @return List<Class> 包下所有类
	 */
	private static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace(".", "/");
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			logger.info("resource"+resource.getFile().toString());
			String newPath = resource.getFile().replace("%20", " ");
			dirs.add(new File(newPath));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClass(directory, packageName));
		}
		return classes;
	}

	private static List<Class> findClass(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			logger.info("file.getPath="+file.getPath());
			if (file.isDirectory()) {
				logger.info("!file.getName()="+(file.getName()!=null));
				assert !file.getName().contains(".");
				logger.info("file.getPath="+file.getPath());
				classes.addAll(findClass(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				logger.info("Class.forName="+packageName + "." + file.getName().substring(0, file.getName().length() - 6));
				
				Class cls= Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6));
				logger.info("Class.for11111Name="+cls);
				classes.add(
						Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public static String getLast(String str) {
		int post = str.lastIndexOf('.');
		return str.substring(post+1);
	}
}
