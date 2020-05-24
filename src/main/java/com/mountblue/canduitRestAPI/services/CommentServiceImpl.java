package com.mountblue.canduitRestAPI.services;

import com.mountblue.canduitRestAPI.DAO.CommentRepository;
import com.mountblue.canduitRestAPI.entity.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comments> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comments findById(int theId) {
        Optional<Comments> result = commentRepository.findById(theId);

        Comments theComment;

        if(result.isPresent()){
            theComment = result.get();
        }else{
            throw new RuntimeException("Did not find comment id - " + theId);
        }

        return theComment;
    }

    @Override
    public void save(Comments theComment) {
        commentRepository.save(theComment);
    }

    @Override
    public void deleteById(int theId) {
        commentRepository.deleteById(theId);
    }

}
