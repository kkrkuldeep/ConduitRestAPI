package com.mountblue.canduitRestAPI.services;

import com.mountblue.canduitRestAPI.DAO.PostRepository;
import com.mountblue.canduitRestAPI.controller.PostNotFoundException;
import com.mountblue.canduitRestAPI.entity.Post;
import com.mountblue.canduitRestAPI.entity.Tags;
import com.mountblue.canduitRestAPI.entity.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;

import java.security.Principal;
import java.util.*;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    public PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll() {
        return (List<Post>) postRepository.findAll();
    }

    @Override
    public Post findById(int theId) {

        Optional<Post> result = postRepository.findById(theId);

        Post thePost;

        if (result.isPresent()) {
            thePost = result.get();
        } else {
            throw new RuntimeException("Did not find post id - " + theId);
        }

        return thePost;

    }

    @Override
    public void save(Post thePost) {
        postRepository.save(thePost);
    }

    @Override
    public String deleteById(int theId, Principal principal) {

        Post post = postService.findById(theId);
        if (post == null) {
            throw new PostNotFoundException("Did not find Post id : " + theId);
        }

        String currentUserLoggedIn = principal.getName();
        String postAuthority = post.getUser().getAuthorities().getRoleName();
        String postUsername = post.getUser().getName();

        if (postUsername.equalsIgnoreCase(currentUserLoggedIn) &&
                (postAuthority.equals("ROLE_ADMIN") || postAuthority.equals("ROLE_USER"))) {
            postRepository.deleteById(theId);
        }else{
            return "You are not authentic user to delete thi post";
        }

        return "Deleted post id : " + theId;
    }

    @Override
    public void addPostIntoAccount(Post post, int userId, String actionPerformed) {
        int postId = post.getId();
        User user = userService.findById(userId);

        List<String> postEntryTags = Arrays.asList(post.getTags().split("#"));

        // To not put two same tags on a single post
        Set<String> getUniqueSetCollection = new HashSet<>();

        for (String inputTag : postEntryTags) {
            if (inputTag.length() > 1) {
                getUniqueSetCollection.add(inputTag.trim());
            }
        }

        List<Tags> tagsList = tagService.findAll();
        Date date = new Date();

        boolean flag;

        for (String tagName : getUniqueSetCollection) {  // Tags coming from post
            flag = true;
            for (Tags tag : tagsList) {  // get TagList from Database
                if (tag.getName().equals(tagName)) {

                    flag = false;

                    post.getTagList().add(tag);
                    tag.getPostList().add(post);
                }
            }
            if (flag == true) {
                Tags tag = new Tags();
                tag.setName(tagName);
                tag.setCreatedAt(date.toString());
                tag.setUpdatedAt(date.toString());

                tag.getPostList().add(post);
                post.getTagList().add(tag);
            }
        }

        if (actionPerformed.equals("draft")) {
            post.setIs_published(false);
        } else {
            post.setIs_published(true);
        }

        if (post.getId() != 0) {
            Post updatePost = postService.findById(post.getId());
            updatePost.setTitle(post.getTitle());
            updatePost.setAuthor(user.getName());
            updatePost.setExcerpt(post.getExcerpt());
            updatePost.setContent(post.getContent());
            updatePost.setUpdated_at(date.toString());
            updatePost.setPublish_at(date.toString());
            updatePost.setUser(user);
            postService.save(updatePost);

        } else {
            post.setAuthor(user.getName());
            post.setCreated_at(date.toString());
            post.setPublish_at(date.toString());
            post.setUpdated_at(date.toString());
            post.setUser(user);
            postService.save(post);
        }
    }
}
