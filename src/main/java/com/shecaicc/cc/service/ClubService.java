package com.shecaicc.cc.service;

import java.io.InputStream;

import com.shecaicc.cc.dto.ClubExecution;
import com.shecaicc.cc.entity.Club;
import com.shecaicc.cc.exceptions.ClubOperationException;

public interface ClubService {
	ClubExecution addClub(Club club, InputStream clubImgInputStream, String fileName) throws ClubOperationException;
}
