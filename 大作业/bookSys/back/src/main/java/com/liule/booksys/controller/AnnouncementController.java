package com.liule.booksys.controller;

import com.liule.booksys.entitys.Announcement;
import com.liule.booksys.service.AnnouncementService;
import com.liule.booksys.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    // 发布公告
    @PostMapping("/")
    @PreAuthorize("hasRole('admin')")
    public R<Announcement> addAnnouncement(@RequestBody Announcement announcement) {
        // 设置公告发布时间
       // announcement.setCreateTime(new Timestamp(new Date(System.currentTimeMillis()), null));

        // 保存公告
        boolean saved = announcementService.save(announcement);
        if (saved) {
            return R.ok(announcement);
        } else {
            return R.failed("发布公告失败");
        }
    }

    // 删除公告
    @DeleteMapping("/{announcementId}")
    @PreAuthorize("hasRole('admin')")
    public R<String> deleteAnnouncement(@PathVariable("announcementId") Integer announcementId) {
        boolean removed = announcementService.removeById(announcementId);
        if (removed) {
            return R.ok("删除成功");
        } else {
            return R.failed("删除失败，公告不存在");
        }
    }

    // 更新公告
    @PutMapping("/")
    @PreAuthorize("hasRole('admin')")
    public R<Announcement> updateAnnouncement(@RequestBody Announcement announcement) {
        boolean updated = announcementService.updateById(announcement);
        if (updated) {
            return R.ok(announcement);
        } else {
            return R.failed("更新公告失败");
        }
    }

    // 查看所有公告
    @GetMapping("/list")
    public R<List<Announcement>> listAnnouncements() {
        List<Announcement> announcementList = announcementService.list();
        return R.ok(announcementList);
    }

    // 根据公告ID查询公告
    @GetMapping("/{announcementId}")
    public R<Announcement> getAnnouncementById(@PathVariable("announcementId") Integer announcementId) {
        Announcement announcement = announcementService.getById(announcementId);
        if (announcement != null) {
            return R.ok(announcement);
        } else {
            return R.failed("公告不存在");
        }
    }
}

