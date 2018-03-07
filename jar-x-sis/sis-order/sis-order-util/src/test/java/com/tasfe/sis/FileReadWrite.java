package com.tasfe.sis;

import java.io.File;
import java.io.OutputStream;

import com.tasfe.sis.order.util.FileUtil;

public class FileReadWrite {

	
	public static void writeFile(File tmpDir,Object obj) {
		FileUtil.openFile(tmpDir);
		FileUtil.writeFile(tmpDir, obj);
		OutputStream os =FileUtil.exportFile(tmpDir);
		FileUtil.closeFile(os);
		FileUtil.closeFile();
		/**
		 * os文件流 输出。
		 */
	}
	
	public static void readFile(File tmpDir) {
		/**
		 * 走直接请求模式
		 */
//		FileUtil.readFileToHandle(tmpDir,BaseVO.class);
		/**
		 * os文件流 输出。
		 */
	}

}
