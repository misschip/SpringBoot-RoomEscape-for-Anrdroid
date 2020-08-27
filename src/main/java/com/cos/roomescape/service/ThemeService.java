package com.cos.roomescape.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.roomescape.dto.ReviewRespDto;
import com.cos.roomescape.dto.StoreNameDto;
import com.cos.roomescape.dto.ThemeDetailRespDto;
import com.cos.roomescape.dto.ThemeRespDto;
import com.cos.roomescape.model.Store;
import com.cos.roomescape.model.Theme;
import com.cos.roomescape.model.User;
import com.cos.roomescape.repository.ReviewRepository;
import com.cos.roomescape.repository.StoreRepository;
import com.cos.roomescape.repository.ThemeRepository;
import com.cos.roomescape.repository.UserRepository;
import com.cos.roomescape.util.IpUtil;

//Controller,Repository, Configuration,Service,Component
//RestController,Bean

@Service // IOC
public class ThemeService {
	@Autowired
	private ThemeRepository themeRepository; // DI
    @Autowired
    private ReviewRepository reviewRepository;
	@Autowired
	private StoreRepository storeRepository;
    
	@Transactional(readOnly = true)
	public List<Theme> 테마보기() {
        
		List<Theme> themes = themeRepository.findAll();
		
		
	    for (Theme theme : themes) {
	    	// System.out.println(theme.getThemeImg());
	    	String oldPath = theme.getThemeImg();
	    	if (oldPath.startsWith("http://")) continue;
	    	String newPath = IpUtil.convertAddress(oldPath);
	    	theme.setThemeImg(newPath);
	    }
	  
	    return themes;
	}
   
	@Transactional(readOnly = true)
	public ThemeDetailRespDto 상세보기(int themeId) {
		 ThemeDetailRespDto themeDto = new ThemeDetailRespDto();
		 Theme theme = themeRepository.findById(themeId); 
		 
	     String storeName = storeRepository.findByStoreId(theme.getStoreId());	 
		 System.out.println("storeName" +storeName);
	     //StoreNameDto storeDto = storeRepository.findByStoreId(theme.getStoreId());	 
		 List<ThemeRespDto> themes = themeRepository.findByStoreId(theme.getStoreId());
		 
		 for (ThemeRespDto themeRespDto : themes) {
			String oldPath = themeRespDto.getThemeImg();
		    if (oldPath.startsWith("http://")) continue;
		    String newPath = IpUtil.convertAddress(oldPath);
		    themeRespDto.setThemeImg(newPath);
		 }
		 
		 List<ReviewRespDto> reviewDto = reviewRepository.findByThemeId(themeId);
		 
		 
		 themeDto.setTheme(theme);
		 themeDto.setStoreName(storeName);
		 themeDto.setThemes(themes);
		 themeDto.setReviews(reviewDto);
		 
		 return themeDto;
	}
	
}