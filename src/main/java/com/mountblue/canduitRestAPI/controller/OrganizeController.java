package com.mountblue.canduitRestAPI.controller;

import com.mountblue.canduitRestAPI.DAO.PostRepository;
import com.mountblue.canduitRestAPI.DAO.TagsRepository;
import com.mountblue.canduitRestAPI.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrganizeController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @GetMapping("/search")
    public Page<Post> getSearchResult(@RequestParam() Optional<String> title, Model model,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam Optional<String> sortBy) {

        return postRepository.findBySearchString(title.orElse("_"),
                PageRequest.of(page, 4, Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @GetMapping("/filter")
    public Page<Post> getFilter(@RequestParam() int id, @RequestParam(defaultValue = "0") int page,
                                @RequestParam Optional<String> sortBy) {

        String getTagName = tagsRepository.getTagName(id);

        System.out.println("Get tag name: " + getTagName);

        return postRepository.findByTags(getTagName, PageRequest.of(page, 4, Sort.Direction.ASC,
                sortBy.orElse("id")));
    }
}
