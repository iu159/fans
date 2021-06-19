package edu.wuyi.fans.web.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.wuyi.fans.annotation.FansLog;
import edu.wuyi.fans.model.constant.WebConst;
import edu.wuyi.fans.model.entity.Picture;
import edu.wuyi.fans.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/14
 */
@RestController
@RequestMapping("/admin/picture")
public class AdminPictureController {
    @Autowired
    private PictureService pictureService;

    @GetMapping("/listPicture")
    PageInfo<Picture> listPicture(@RequestParam(value = "pageNo", defaultValue = "1")int pageNo){
        PageHelper.startPage(pageNo, WebConst.PAGE_COMM_SIZE);
        List<Picture> pictureList = pictureService.listPictureIgnoreStatus();
        return new PageInfo<>(pictureList);
    }

    @DeleteMapping("/delete/{pid}")
    @FansLog(value = "管理员删除照片", level = 3)
    void deletePicture(@PathVariable("pid") String pid){
        pictureService.deleteById(pid);
    }

    @PutMapping("/recover/{pid}")
    @FansLog(value = "管理员恢复照片", level = 3)
    void recoverPicture(@PathVariable("pid") String pid){
        pictureService.recoverPicture(pid);
    }
}
