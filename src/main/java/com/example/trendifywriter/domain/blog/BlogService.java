package com.example.trendifywriter.domain.blog;

import com.example.trendifywriter.domain.post.Post;
import com.example.trendifywriter.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogService {

    @Transactional
    public void publishPost(Post post) {

        // 1. Post 유효성 검사
        // 2. Post 저장 (데이터베이스 혹은 다른 저장소)
        // 3. 블로그에 Post 연동
        // 4. 추가적인 메타데이터 업데이트 (예: 블로그의 총 포스트 수)


    }



}
