package com.liule.booksys.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liule.booksys.entitys.Announcement;
import com.liule.booksys.mapper.AnnouncementMapper;
import com.liule.booksys.service.AnnouncementService;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {
}
