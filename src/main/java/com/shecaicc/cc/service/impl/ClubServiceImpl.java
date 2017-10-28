package com.shecaicc.cc.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shecaicc.cc.dao.ClubDao;
import com.shecaicc.cc.dto.ClubExecution;
import com.shecaicc.cc.dto.ImageHolder;
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.enums.ClubStateEnum;
import com.shecaicc.cc.exceptions.ClubOperationException;
import com.shecaicc.cc.service.ClubService;
import com.shecaicc.cc.util.ImageUtil;
import com.shecaicc.cc.util.PageCalculator;
import com.shecaicc.cc.util.PathUtil;

@Service
public class ClubServiceImpl implements ClubService {
	@Autowired
	private ClubDao clubDao;

	@Override
	public ClubExecution getClubList(Club clubCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Club> clubList = clubDao.queryClubList(clubCondition, rowIndex, pageSize);
		int count = clubDao.queryClubCount(clubCondition);
		ClubExecution ce = new ClubExecution();
		if (clubList != null) {
			ce.setClubList(clubList);
			ce.setCount(count);
		} else {
			ce.setState(ClubStateEnum.INNER_ERROR.getState());
		}
		return ce;
	}

	@Override
	@Transactional
	public ClubExecution addClub(Club club, ImageHolder thumbnail) throws ClubOperationException {
		// 控制判断
		if (club == null) {
			return new ClubExecution(ClubStateEnum.NULL_CLUB);
		}
		// 待添加category, area非空判断
		try {
			// 给社团信息赋初始值
			club.setEnableStatus(0);
			club.setCreateTime(new Date());
			club.setLastEditTime(new Date());
			// 添加社团信息
			int effectedNum = clubDao.insertClub(club);
			if (effectedNum <= 0) {
				throw new ClubOperationException("社团创建失败");
			} else {
				if (thumbnail.getImage() != null) {
					// 存储图片
					try {
						addClubImg(club, thumbnail);
					} catch (Exception e) {
						throw new ClubOperationException("addClubImg error: " + e.getMessage());
					}
					// 更新社团图片地址
					effectedNum = clubDao.updateClub(club);
					if (effectedNum <= 0) {
						throw new ClubOperationException("更新图片地址失败");
					}
				}
			}
		} catch (Exception e) {
			throw new ClubOperationException("addClub error: " + e.getMessage());
		}
		return new ClubExecution(ClubStateEnum.CHECK, club);
	}

	private void addClubImg(Club club, ImageHolder thumbnail) {
		// 获取club图片目录的相对路径
		String dest = PathUtil.getClubImagePath(club.getClubId());
		String clubImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		club.setClubImg(clubImgAddr);
	}

	@Override
	public Club getByClubId(long clubId) {
		return clubDao.queryByClubId(clubId);
	}

	@Override
	public ClubExecution modifyClub(Club club, ImageHolder thumbnail) throws ClubOperationException {
		if (club == null || club.getClubId() == null) {
			return new ClubExecution(ClubStateEnum.NULL_CLUB);
		} else {
			try {
				// 1.判断是否需要处理图片
				if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())) {
					Club tempClub = clubDao.queryByClubId(club.getClubId());
					if (tempClub.getClubImg() != null) {
						ImageUtil.deleteFileOrPath(tempClub.getClubImg());
					}
					addClubImg(club, thumbnail);
				}
				// 2.更新社团信息
				club.setLastEditTime(new Date());
				int effectedNum = clubDao.updateClub(club);
				if (effectedNum <= 0) {
					return new ClubExecution(ClubStateEnum.INNER_ERROR);
				} else {
					club = clubDao.queryByClubId(club.getClubId());
					return new ClubExecution(ClubStateEnum.SUCCESS, club);
				}
			} catch (Exception e) {
				throw new ClubOperationException("modifyClub error: " + e.getMessage());
			}
		}
	}

}
