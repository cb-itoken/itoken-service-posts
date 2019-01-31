package com.cb.itoken.service.posts.controller;

import com.cb.itoken.common.dto.BaseResult;
import com.cb.itoken.common.utils.MapperUtils;
import com.cb.itoken.service.posts.service.PostsService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "v1/posts")
public class PostsController {

    @Autowired
    private PostsService postsService;

    /**
     * 根据 ID 查询文章
     * @param postGuid
     * @return
     */
    @RequestMapping(value = "{postGuid}", method = RequestMethod.GET)
    public BaseResult get(@PathVariable(required = true) String postGuid){
        TbPostsPost tbPostsPost = new TbPostsPost();
        tbPostsPost.setPostGuid(postGuid);
        TbPostsPost obj = postsService.selectOne(tbPostsPost);
        return BaseResult.ok(obj);
    }

    /**
     * 保存文章
     * @param tbPostsPostJson
     * @param optsBy
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public BaseResult sava(
            @RequestParam(required = true) String tbPostsPostJson,
            @RequestParam(required = true) String optsBy
    ){
        int result = 0;

        TbPostsPost tbPostsPost = null;
        try {
            tbPostsPost = MapperUtils.json2pojo(tbPostsPostJson, TbPostsPost.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(tbPostsPost != null){
            // 新增文章
            if(StringUtils.isBlank(tbPostsPost.getPostGuid())){
                tbPostsPost.setPostGuid(UUID.randomUUID().toString());
                tbPostsPost.setTimePublished(new Date());
                result = postsService.insert(tbPostsPost, optsBy);
            }

            // 修改文章
            else {
                result = postsService.update(tbPostsPost, optsBy);
            }

            // 最少有一行数据受影响
            if(result > 0){
                return BaseResult.ok("保存文章成功");
            }
        }

        return BaseResult.ok("保存文章失败");
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param tbPostsPostJson
     * @return
     */
    @RequestMapping(value = "page/{pageNum}/{pageSize}", method = RequestMethod.GET)
    public BaseResult page(
            @PathVariable(required = true) int pageNum,
            @PathVariable(required = true) int pageSize,
            @RequestParam(required = false) String tbPostsPostJson
    ){
        TbPostsPost tbPostsPost = null;

        if(StringUtils.isNotBlank(tbPostsPostJson)){
            try {
                tbPostsPost = MapperUtils.json2pojo(tbPostsPostJson, TbPostsPost.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        PageInfo<TbPostsPost> pageInfo = postsService.page(pageNum, pageSize, tbPostsPost);

        // 分页后的结果集
        List<TbPostsPost> list = pageInfo.getList();

        // 封装 Cursor 对象
        BaseResult.Cursor cursor = new BaseResult.Cursor();
        cursor.setTotal(new Long(pageInfo.getTotal()).intValue());
        cursor.setOffset(pageInfo.getPageNum());
        cursor.setLimit(pageInfo.getPageSize());

        return BaseResult.ok(list, cursor);
    }
}
