package edu.wuyi.fans.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.wuyi.fans.exception.BadRequestException;
import edu.wuyi.fans.mapper.PictureMapper;
import edu.wuyi.fans.model.constant.WebConst;
import edu.wuyi.fans.model.entity.Picture;
import edu.wuyi.fans.model.entity.User;
import edu.wuyi.fans.model.param.PictureESParam;
import edu.wuyi.fans.service.CategoryService;
import edu.wuyi.fans.service.PictureService;
import edu.wuyi.fans.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@Service
@Slf4j
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {
    @Autowired
    private PictureMapper pictureMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Value("${fans.config.upload-path}")
    private String UPLOAD_PATH;

    @Value("${fans.config.base-url}")
    private String BASE_URL;

    @Override
    public void upload(MultipartFile image, Picture picture) {
        if(image.isEmpty()){
            throw new BadRequestException("图片不能为空");
        }
        String pid = IdUtil.simpleUUID();
        picture.setPid(pid);
        picture.setStatus(1);
        picture.setViewCount(0);
        picture.setTop(false);

        DateTime date = DateUtil.date();

        //插入elastic_search
        IndexRequest request = new IndexRequest("fans");
        request.id(pid);
        request.timeout("10s");
        PictureESParam esParam = new PictureESParam();
        BeanUtils.copyProperties(picture,esParam);
        esParam.setCategory(categoryService.getById(picture.getCategory()).getName());
        esParam.setUsername(userService.getById(picture.getUserId()).getUsername());
        request.source(JSONUtil.parseObj(esParam).toStringPretty(), XContentType.JSON);
        try {
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            log.info(response.status().name());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int year = DateUtil.year(date);
        int month = DateUtil.month(date);
        int dayOfMonth = DateUtil.dayOfMonth(date);

        String urn ="picture/"+ year+"/"+month+"/"+dayOfMonth+"/";
        String extName = FileNameUtil.extName(image.getOriginalFilename());
        String fileName = pid+"."+extName;
        String smFileName = pid+"-sm."+extName;

        String path = UPLOAD_PATH + urn;

        Path uploadPath = Paths.get(path,fileName);
        Path smUploadPath = Paths.get(path,smFileName);

        try {
            Files.createDirectories(uploadPath.getParent());
            //压缩图片
            InputStream uploadFileInputStream = image.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(uploadFileInputStream);
            Thumbnails.of(bufferedImage).height(800).outputQuality(0.8).toFile(uploadPath.toString());
            Thumbnails.of(bufferedImage).height(400).outputQuality(0.6).toFile(smUploadPath.toString());
            picture.setUrl(BASE_URL+urn+fileName);
            picture.setSmUrl(BASE_URL+urn+smFileName);
            pictureMapper.insert(picture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Page<Picture> listPicture(int pageNo) {
        Page<Picture> pictureIPage = new Page<>(pageNo, WebConst.PAGE_SIZE);
        QueryWrapper pictureWrapper = new QueryWrapper();
        pictureWrapper.orderByDesc("view_count");
        return pictureMapper.selectPage(pictureIPage,pictureWrapper);
    }

    @Override
    public Page<Picture> listPictureLatest(int pageNo) {
        Page<Picture> pictureIPage = new Page<>(pageNo,WebConst.PAGE_SIZE);
        QueryWrapper pictureWrapper = new QueryWrapper();
        pictureWrapper.orderByDesc("create_time");
        return pictureMapper.selectPage(pictureIPage,pictureWrapper);
    }

    @Override
    public List<Picture> listMyPictures(String uid) {
        QueryWrapper<Picture> pictureWrapper = new QueryWrapper<>();
        pictureWrapper.eq("user_id",uid);
        return pictureMapper.selectList(pictureWrapper);
    }

    @Override
    public List<Picture> listPictureByCategory(Integer categoryId) {
        QueryWrapper<Picture> pictureWrapper = new QueryWrapper<>();
        pictureWrapper.eq("category",categoryId);
        return pictureMapper.selectList(pictureWrapper);
    }

    @Override
    public List<Picture> listPictureByRecommend() {
        QueryWrapper<Picture> pictureWrapper = new QueryWrapper<>();
        pictureWrapper.orderByDesc("view_count");
        pictureWrapper.last("LIMIT 5");
        return pictureMapper.selectList(pictureWrapper);
    }

    @Override
    public List<Picture> listPictureIgnoreStatus() {
        return pictureMapper.listPictureIgnoreStatus();
    }

    @Override
    public void recoverPicture(String pid) {
        pictureMapper.recoverPicture(pid);
    }

    @Override
    public String getUid(String pictureId) {
        return pictureMapper.selectById(pictureId).getUserId();
    }

    @Override
    public List<Picture> listPictureBySearch(String keyword) {
        List<String> batchIds = null;
        SearchRequest request = new SearchRequest("fans");
        //构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(keyword, "title", "summary", "pid", "tags", "category", "username").minimumShouldMatch("50%");
        sourceBuilder.size(1024);
        sourceBuilder.query(multiMatchQuery);
        sourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));
        request.source(sourceBuilder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            batchIds = new ArrayList<>();
            for (SearchHit hit : response.getHits().getHits()) {
                String pid = (String)hit.getSourceAsMap().get("pid");
                batchIds.add(pid);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (batchIds.size()>0){
            return pictureMapper.selectBatchIds(batchIds);
        }else {
            return null;
        }
    }

    @Override
    public void deleteById(String pid) {
        pictureMapper.deleteById(pid);
        DeleteRequest request= new DeleteRequest("fans",pid);
        request.timeout("3s");
        try {
            client.delete(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
