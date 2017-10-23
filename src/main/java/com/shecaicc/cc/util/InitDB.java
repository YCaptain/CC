package com.shecaicc.cc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class InitDB {

	public static void main(String[] args) {
		//createDatabase();
		//createTables();
		//dropDatabase();
		insertDataClubCategory();
	}

	public static void createDatabase() {
		Connection con = null;
		Statement st = null;
		try {
			con = JdbcUtils.getBasicConnection();
			st = con.createStatement();

			String sql = "CREATE DATABASE " + JdbcUtils.DBNAME;
			st.execute(sql);
			System.out.println("Create database: " + JdbcUtils.DBNAME);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(null, st, con);
		}
	}

	public static void dropDatabase() {
		Connection con = null;
		Statement st = null;
		try {
			con = JdbcUtils.getBasicConnection();
			st = con.createStatement();

			String sql = "DROP DATABASE " + JdbcUtils.DBNAME;
			st.execute(sql);
			System.out.println("Drop database: " + JdbcUtils.DBNAME);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(null, st, con);
		}
	}

	public static void createTables() {
		Connection con = null;
		Statement st = null;
		try {
			con = JdbcUtils.getConnection();
			st = con.createStatement();

			String sql = "CREATE TABLE `tb_area`(" + "`area_id` INT(6) NOT NULL AUTO_INCREMENT,"
					+ "`area_name` VARCHAR(200) NOT NULL," + "`priority` INT(3) NOT NULL DEFAULT '0',"
					+ "`create_time` DATETIME DEFAULT NULL," + "`last_edit_time` DATETIME DEFAULT NULL,"
					+ "PRIMARY KEY (`area_id`)," + "UNIQUE KEY `UK_AREA`(`area_name`)"
					+ ")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
			st.execute(sql);
			System.out.println("Create table: tb_area");

			sql = "CREATE TABLE `tb_person_info`(" + "`user_id` INT(10) NOT NULL AUTO_INCREMENT,"
					+ "`name` VARCHAR(32) DEFAULT NULL," + "`profile_img` VARCHAR(1024) DEFAULT NULL,"
					+ "`email` VARCHAR(1024) DEFAULT NULL," + "`gender` VARCHAR(2) DEFAULT NULL,"
					+ "`enable_status` INT(2) NOT NULL DEFAULT '0' COMMENT '0:禁止加入社团	1：允许加入社团',"
					+ "`user_type` INT(2) NOT NULL DEFAULT '0' COMMENT '0:未注册用户	1:注册用户  2:社长  3:超级管理员',"
					+ "`create_time` DATETIME DEFAULT NULL," + "`last_edit_time` DATETIME DEFAULT NULL,"
					+ "PRIMARY KEY (`user_id`)"
					+ ")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
			st.execute(sql);
			System.out.println("Create table: tb_person_info");

			sql = "CREATE TABLE `tb_wechat_auth`(" + "`wechat_auth_id` INT(15) NOT NULL AUTO_INCREMENT,"
					+ "`user_id` INT(15) NOT NULL," + "`open_id` VARCHAR(1024) NOT NULL,"
					+ "`create_time` DATETIME DEFAULT NULL," + "PRIMARY KEY (`wechat_auth_id`),"
					+ "UNIQUE INDEX `uk_wechat_profile` (`open_id`),"
					+ "CONSTRAINT `fk_wechatauth_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info`(`user_id`)"
					+ ")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
			st.execute(sql);
			System.out.println("Create table: tb_wechat_auth");

			sql = "CREATE TABLE `tb_local_auth`("
					+ "`local_auth_id` INT(15) NOT NULL AUTO_INCREMENT,"
					+ "`user_id` INT(15) NOT NULL,"
					+ "`username` VARCHAR(128) NOT NULL,"
					+ "`password` VARCHAR(128) NOT NULL,"
					+ "`create_time` DATETIME DEFAULT NULL,"
					+ "`last_edit_time` DATETIME DEFAULT NULL,"
					+ "PRIMARY KEY (`local_auth_id`),"
					+ "UNIQUE KEY `uk_local_profile` (`username`),"
					+ "CONSTRAINT `fk_localauth_profile` FOREIGN KEY (`user_id`) REFERENCES `tb_person_info` (`user_id`)"
					+ ")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
			st.execute(sql);
			System.out.println("Create table: tb_local_auth");

			sql = "CREATE TABLE `tb_head_line`("
					+ "`line_id` INT(100) NOT NULL AUTO_INCREMENT,"
					+ "`line_name` VARCHAR(1000) DEFAULT NULL,"
					+ "`line_link` VARCHAR(2000) NOT NULL,"
					+ "`line_img` VARCHAR(2000) NOT NULL,"
					+ "`priority` INT(3) DEFAULT NULL,"
					+ "`enable_status` INT(2) NOT NULL DEFAULT '0',"
					+ "`create_time` DATETIME DEFAULT NULL,"
					+ "`last_edit_time` DATETIME DEFAULT NULL,"
					+ "PRIMARY KEY (`line_id`)"
					+ ")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
			st.execute(sql);
			System.out.println("Create table: tb_head_line");

			sql = "CREATE TABLE `tb_club_category` ("
					+ "`club_category_id` INT(11) NOT NULL AUTO_INCREMENT,"
					+ "`club_category_name` VARCHAR(100) NOT NULL DEFAULT '',"
					+ "`club_category_desc` VARCHAR(1000) DEFAULT '',"
					+ "`club_category_img` VARCHAR(2000) DEFAULT NULL,"
					+ "`priority` INT(3) NOT NULL DEFAULT '0',"
					+ "`create_time` DATETIME DEFAULT NULL,"
					+ "`last_edit_time` DATETIME DEFAULT NULL,"
					+ "`parent_id` INT(11) DEFAULT NULL,"
					+ "PRIMARY KEY (`club_category_id`),"
					+ "CONSTRAINT `fk_club_category_self` FOREIGN KEY "
					+ "(`parent_id`) REFERENCES `tb_club_category` (`club_category_id`)"
					+ ")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
			st.execute(sql);
			System.out.println("Create table: tb_club_category");

			sql = "CREATE TABLE `tb_club` ("
					+ "`club_id` INT(10) NOT NULL AUTO_INCREMENT,"
					+ "`captain_id` INT(15) NOT NULL,"
					+ "`area_id` INT(6) DEFAULT NULL,"
					+ "`club_category_id` INT(11) DEFAULT NULL,"
					+ "`club_name` VARCHAR(256) NOT NULL,"
					+ "`club_desc` VARCHAR(1024) DEFAULT NULL,"
					+ "`club_addr` VARCHAR(200) DEFAULT NULL,"
					+ "`phone` VARCHAR(128) DEFAULT NULL,"
					+ "`club_img` VARCHAR(1024) DEFAULT NULL,"
					+ "`priority` INT(3) DEFAULT '0',"
					+ "`create_time` DATETIME DEFAULT NULL,"
					+ "`last_edit_time` DATETIME DEFAULT NULL,"
					+ "`enable_status` INT(2) NOT NULL DEFAULT '0',"
					+ "`advice` VARCHAR(256) DEFAULT NULL,"
					+ "PRIMARY KEY (`club_id`),"
					+ "CONSTRAINT `fk_club_area` FOREIGN KEY (`area_id`) REFERENCES `tb_area` (`area_id`),"
					+ "CONSTRAINT `fk_club_profile` FOREIGN KEY (`captain_id`) REFERENCES `tb_person_info` (`user_id`),"
					+ "CONSTRAINT `fk_club_clubcate` FOREIGN KEY (`club_category_id`) "
					+ "REFERENCES `tb_club_category` (`club_category_id`)"
					+ ")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
			st.execute(sql);
			System.out.println("Create table: tb_club");

			sql = "CREATE TABLE `tb_event_category` ("
					+ "`event_category_id` INT(11) NOT NULL AUTO_INCREMENT,"
					+ "`event_category_name` VARCHAR(100) NOT NULL,"
					+ "`priority` INT(3) DEFAULT '0',"
					+ "`create_time` DATETIME DEFAULT NULL,"
					+ "`club_id` INT(10) NOT NULL DEFAULT '0',"
					+ "PRIMARY KEY (`event_category_id`),"
					+ "CONSTRAINT `fk_procate_club` FOREIGN KEY (`club_id`) REFERENCES `tb_club` (`club_id`)"
					+ ")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
			st.execute(sql);
			System.out.println("Create table: tb_event_category");

			sql = "CREATE TABLE `tb_event` ("
					+ "`event_id` INT(100) NOT NULL AUTO_INCREMENT,"
					+ "`event_name` VARCHAR(100) NOT NULL,"
					+ "`event_desc` VARCHAR(2000) DEFAULT NULL,"
					+ "`img_addr` VARCHAR(2000) DEFAULT '',"
					+ "`capacity` INT(4) NOT NULL DEFAULT '0',"
					+ "`num_person` INT(4) NOT NULL DEFAULT '0',"
					+ "`priority` INT(2) NOT NULL DEFAULT '0',"
					+ "`create_time` DATETIME DEFAULT NULL,"
					+ "`end_time` DATETIME DEFAULT NULL,"
					+ "`last_edit_time` DATETIME DEFAULT NULL,"
					+ "`enable_status` INT(2) NOT NULL DEFAULT '0',"
					+ "`event_category_id` INT(11) DEFAULT NULL,"
					+ "`club_id` INT(10) NOT NULL DEFAULT '0',"
					+ "PRIMARY KEY (`event_id`),"
					+ "CONSTRAINT `fk_event_procate` FOREIGN KEY (`event_category_id`) "
					+ "REFERENCES `tb_event_category` (`event_category_id`),"
					+ "CONSTRAINT `fk_event_club` FOREIGN KEY (`club_id`) REFERENCES `tb_club` (`club_id`)"
					+ ")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
			st.execute(sql);
			System.out.println("Create table: tb_event");

			sql = "CREATE TABLE `tb_event_img` ("
					+ "`event_img_id` INT(20) NOT NULL AUTO_INCREMENT,"
					+ "`img_addr` VARCHAR(2000) NOT NULL,"
					+ "`img_desc` VARCHAR(2000) DEFAULT NULL,"
					+ "`priority` INT(3) DEFAULT '0',"
					+ "`create_time` DATETIME DEFAULT NULL,"
					+ "`event_id` INT(20) DEFAULT NULL,"
					+ "PRIMARY KEY (`event_img_id`),"
					+ "CONSTRAINT `fk_proimg_event` FOREIGN KEY (`event_id`) REFERENCES `tb_event` (`event_id`)"
					+ ")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";
			st.execute(sql);
			System.out.println("Create table: tb_event_img");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(null, st, con);
		}
	}

	public static void insertDataArea() {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = JdbcUtils.getConnection();
			String sql = "INSERT INTO `tb_area` ("
					+ "`area_id`, `area_name`, `priority`, `create_time`, `last_edit_time`) "
					+ "VALUES (?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setInt(1, 2);
			ps.setString(2, "上海");
			ps.setInt(3, 40);
			java.util.Date cTime = new java.util.Date();
			ps.setDate(4, new java.sql.Date(cTime.getTime()));
			ps.setDate(5, new java.sql.Date(cTime.getTime()));
			int count = ps.executeUpdate();
			System.out.println("Insert data to tb_area: " + count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(null, ps, con);
		}
	}

	public static void insertDataPersonInfo() {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = JdbcUtils.getConnection();
			String sql = "INSERT INTO `tb_person_info` ("
					+ "`name`) "
					+ "VALUES (?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, "test_2");
			int count = ps.executeUpdate();
			System.out.println("Insert data to tb_person_info: " + count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(null, ps, con);
		}
	}

	public static void insertDataClubCategory() {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = JdbcUtils.getConnection();
			String sql = "INSERT INTO `tb_club_category` ("
					+ "`club_category_name`, `club_category_desc`, `club_category_img`, `priority`) "
					+ "VALUES (?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, "Arts");
			ps.setString(2, "Arts club");
			ps.setString(3, "arts img");
			ps.setInt(4, 1);
			int count = ps.executeUpdate();
			System.out.println("Insert data to tb_club_category: " + count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.free(null, ps, con);
		}
	}

}
