package com.mountblue.canduitRestAPI.DAO;

import com.mountblue.canduitRestAPI.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    public List<Post> findAllByOrderByIdAsc();

    @Query(value = "select * from posts where is_published = true", nativeQuery = true)
    Page<Post> findByIs_publishedTrue(PageRequest pageRequest);

    @Query(value = "select * from posts where is_published = false", nativeQuery = true)
    Page<Post> findByIs_publishedFalse(PageRequest pageRequest);

    @Query(value =
            "select p from Post p where p.is_published = true and concat(p.tags,p.title,p.excerpt,p.content,p.author)" +
                    " like %?1%")
    Page<Post> findBySearchString(String title, Pageable pageable);

    @Query(value =
            "select p from Post p where p.is_published = true and p.tags " +
                    " like %?1%")
    Page<Post> findByTags(String tag, Pageable pageable);
}
