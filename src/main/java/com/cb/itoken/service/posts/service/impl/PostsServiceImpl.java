package com.cb.itoken.service.posts.service.impl;

import com.cb.itoken.common.mapper.TbPostsPostMapper;
import com.cb.itoken.common.service.impl.BaseServiceImpl;
import com.cb.itoken.service.posts.service.PostsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostsServiceImpl extends BaseServiceImpl<TbPostsPost, TbPostsPostMapper> implements PostsService {
}
