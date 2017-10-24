package com.shecaicc.cc.service;

import java.io.InputStream;

import com.shecaicc.cc.dto.ClubExecution;
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.exceptions.ClubOperationException;

public interface ClubService {
	/**
	 * 根据clubCondition分页返回相应社团数据
	 * @param clubCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ClubExecution getClubList(Club clubCondition, int pageIndex, int pageSize);

	/**
	 * 通过社团Id获取社团信息
	 * @param clubId
	 * @return
	 */
	Club getByClubId(long clubId);

	/**
	 * 更新社团信息，包括对图片的处理
	 * @param club
	 * @param clubImgInputStream
	 * @param fileName
	 * @return
	 * @throws ClubOperationException
	 */
	ClubExecution modifyClub(Club club, InputStream clubImgInputStream, String fileName) throws ClubOperationException;

	/**
	 * 注册社团信息，包括图片处理
	 * @param club
	 * @param clubImgInputStream
	 * @param fileName
	 * @return
	 * @throws ClubOperationException
	 */
	ClubExecution addClub(Club club, InputStream clubImgInputStream, String fileName) throws ClubOperationException;
}
