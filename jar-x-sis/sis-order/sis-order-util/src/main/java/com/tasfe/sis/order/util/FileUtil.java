package com.tasfe.sis.order.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tasfe.sis.base.utils.json.JsonUtil;
import com.tasfe.sis.order.handle.AdapterDispatcher;
	
public class FileUtil {
	
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	protected static AdapterDispatcher dispatcher = AdapterDispatcher.getDefaultInstance();

	private static FileOutputStream writeFileOut = null;

	public static boolean openFile(File tmpDir) {
		if (!tmpDir.exists() && !tmpDir.isDirectory()) {
			try {
				tmpDir.createNewFile();
			} catch (IOException e) {
				return false;
			}
			// 创建目录
			tmpDir.mkdir();
		}
		try {
			writeFileOut = new FileOutputStream(tmpDir,true);
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}

	public static boolean closeFile(OutputStream os) {
		try {
			os.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public static boolean deleteFile(File file) {
		try {
			if (file.exists()) {
				closeFile();
				file.delete();
			}
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean closeFile() {
		try {
			writeFileOut.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public static void waitForWirtenCompleted(File file) {  
        if (!file.exists())  
            return;  
        long old_length;  
        do {  
            old_length = file.length();  
            try {  
                Thread.sleep(5000);
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        } while (old_length != file.length());  
    }
	
	public static boolean fileWirtenCompleted(File file) {  
        if (!file.exists())  
            return false;  
        long old_length;  
        do {  
            old_length = file.length();  
            try {  
                Thread.sleep(3000);
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        } while (old_length != file.length());
        return true;
    }

	public static OutputStream exportFile(File tmpDir) {
		if (writeFileOut == null)
			openFile(tmpDir);
		return writeFileOut;
	}
	
	public static byte[] File2byte(File file)  
    {  
        byte[] buffer = null;  
        try  
        {  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            byte[] b = new byte[1024];  
            int n;  
            while ((n = fis.read(b)) != -1)  
            {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        }  
        catch (FileNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        return buffer;  
    }

	/**
	 * 写临时文件
	 * 
	 * @param file
	 */
	public static void writeFile(File tmpDir, Object obj) {
		try {
			openFile(tmpDir);
			// utf-8
			OutputStreamWriter or = new OutputStreamWriter(writeFileOut, "UTF-8");
			PrintWriter bo = new PrintWriter(or);
			String className = obj.getClass().getSimpleName();
			StringBuffer sb = new StringBuffer(className).append("|").append(JsonUtil.Object2String(obj));
			String encode = EncryptUtil.encrypt(sb.toString());
			bo.append(encode);
			bo.println();
			bo.flush();
			bo.close();
			or.close();
			writeFileOut.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 读取文件(外-》内[订单导入],内-》外[黑名单、授信用户])
	 * 
	 * @param file
	 * @return 
	 */
	public static void outFileToHandle(File file,Class<?> clss) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				try {
					String decode = EncryptUtil.decrypt(tempString);
					String[] tempStr = decode.split("\\|", 2);
					String classpackage = clss.getPackage().getName() + "." + tempStr[0];
					Class<?> cls = Class.forName(classpackage);
					String className = cls.getSimpleName();
					Object obj = JsonUtil.readJson2Entity(tempStr[1], cls);
					logger.info(obj.toString());
					dispatcher.subscribe(className).handle(obj);
					logger.info("subscribe" + className);
				} catch (Exception e) {
					logger.debug(e.getMessage());
					continue;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
	/**
	 * 读取文件(针对内-》外的订单导入)
	 * @param file
	 * @param clss
	 */
	public static void innerFileToHandle(File file,Class<?> clss) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				try {
					String decode = EncryptUtil.decrypt(tempString);
					String[] tempStr = decode.split("\\|", 2);
					String classpackage = clss.getPackage().getName() + "." + tempStr[0];
					Class<?> cls = Class.forName(classpackage);
					String className = cls.getSimpleName();
					Object obj = JsonUtil.readJson2Entity(tempStr[1], cls);
					dispatcher.subscribe(className).insertVo(obj);
				} catch (Exception e) {
					logger.debug(e.getMessage());
					continue;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
	
	public static File generateFile(String fixPath, String fileName,String suffix) {
		return new File(fixPath+fileName+suffix);
	} 
	
	
	public static String generateFilePath(String fixPath, String fileName,String suffix) {
		return fixPath+fileName+suffix;
	} 
}
