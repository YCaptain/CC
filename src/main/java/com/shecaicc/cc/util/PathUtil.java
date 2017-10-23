package com.shecaicc.cc.util;

public class PathUtil {

	private static String separator = System.getProperty("file.separator");

	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().startsWith("win")) {
			basePath = "D:/shecaicc/image/";
		} else {
			basePath = "/home/shecaicc/image/";
		}
		basePath = basePath.replace("/", separator);
		return basePath;
	}

	public static String getClubImagePath(long clubId) {
		String imagePath = "upload/item/club/" + clubId + "/";
		return imagePath.replace("/", separator);
	}

}
