package edu.wuyi.fans.web.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.wuyi.fans.annotation.FansLog;
import edu.wuyi.fans.exception.BadRequestException;
import edu.wuyi.fans.model.dto.PictureDTO;
import edu.wuyi.fans.model.entity.Picture;
import edu.wuyi.fans.model.entity.User;
import edu.wuyi.fans.model.param.PictureParam;
import edu.wuyi.fans.model.support.BaseResponse;
import edu.wuyi.fans.service.LikesService;
import edu.wuyi.fans.service.PictureService;
import edu.wuyi.fans.service.UserService;
import edu.wuyi.fans.util.FansUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/picture")
@Api(value = "图片相关接口", tags = "图片相关接口")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikesService likesService;

    @ApiOperation("获取推荐列表")
    @GetMapping("/recommend")
    List<Picture> listPictureByRecommend(){
        return pictureService.listPictureByRecommend();
    }

    @ApiOperation("搜索图片")
    @GetMapping("/search")
    List<Picture> listPictureBySearch(String keyword){
        return pictureService.listPictureBySearch(keyword);
    }

    @ApiOperation("按分类获取图片列表")
    @GetMapping("/category/{cid}")
    List<Picture> listPictureByCategory(@PathVariable("cid") Integer categoryId){
        return pictureService.listPictureByCategory(categoryId);
    }

    @ApiOperation("获取图片列表，按上传时间")
    @GetMapping("/listPictureLatest")
    Page<Picture> listPictureLatest(@RequestParam(value = "pageNo", defaultValue = "1")int pageNo){
        return pictureService.listPictureLatest(pageNo);
    }

    @ApiOperation("获取图片列表，按浏览量")
    @GetMapping("/listPicture")
    Page<Picture> listPictureByViewAccount(@RequestParam(value = "pageNo", defaultValue = "1")int pageNo){
        return pictureService.listPicture(pageNo);
    }

    @ApiOperation("获取图片详情")
    @GetMapping("/getInfo/{pid}")
    PictureDTO getPictureInfo(@PathVariable("pid") String pid){
        Picture picture = pictureService.getById(pid);
        picture.setViewCount(picture.getViewCount()+1);
        pictureService.updateById(picture);
        PictureDTO pictureDTO = new PictureDTO();
        BeanUtils.copyProperties(picture,pictureDTO);
        pictureDTO.setProvider(userService.getUserInfo(picture.getUserId()));
        pictureDTO.setLikes(likesService.listLikes(pid));
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (!ObjectUtils.isEmpty(user)){
            pictureDTO.setLiked(likesService.isLike(pid,user.getUid()));
        }else{
            pictureDTO.setLiked(false);
        }
        return pictureDTO;
    }

    @ApiOperation("删除图片")
    @DeleteMapping("/delete/{pid}")
    @FansLog(value = "删除照片", level = 3)
    void deletePicture(@PathVariable("pid") String pid){
        if (!pictureService.getById(pid).getUserId().equals(FansUtils.getUid())){
            throw new BadRequestException("无操作权限");
        }
        pictureService.deleteById(pid);
    }

    @ApiOperation("修改图片")
    @PutMapping("/edit")
    @FansLog(value = "修改照片", level = 2)
    Picture editPicture(@Valid PictureParam pictureParam,String pid){
        Picture picture = pictureService.getById(pid);
        if (!picture.getUserId().equals(FansUtils.getUid())){
            throw new BadRequestException("无操作权限");
        }
        BeanUtils.copyProperties(pictureParam,picture);
        pictureService.updateById(picture);
        return picture;
    }

    @ApiOperation("上传图片")
    @PostMapping("/upload")
    @FansLog("发布照片")
    BaseResponse postPicture(@RequestPart MultipartFile image,@Valid PictureParam pictureParam){
        StringBuilder sb = new StringBuilder();
        for (String tag:pictureParam.getTags()) {
            sb.append(tag);
            sb.append("-");
        }
        Picture picture = new Picture();
        BeanUtils.copyProperties(pictureParam,picture);
        picture.setTags(sb.substring(0,sb.length()-1));
        picture.setUserId(FansUtils.getUid());
        pictureService.upload(image,picture);
        return BaseResponse.ok("上传成功");
    }

    @GetMapping("/countPicture")
    Integer countPicture(){
        return pictureService.count();
    }
}

