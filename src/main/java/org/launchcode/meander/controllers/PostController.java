package org.launchcode.meander.controllers;

import org.launchcode.meander.models.User;
import org.launchcode.meander.models.data.PostRepository;
import org.launchcode.meander.models.Post;
import org.launchcode.meander.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("post_list")
    public String displayPosts(Model model){
        model.addAttribute("title", "All posts");
        List<Post> posts = (List<Post>) postRepository.findAll();
        model.addAttribute("posts", posts);
        return "post_list";
    }

    @GetMapping("post_single/{postId}")
    public String displayASinglePost(Model model, @PathVariable Integer postId){

        Optional optPost = postRepository.findById(postId);
        if (!optPost.isEmpty()) {
            Post post = (Post) optPost.get();
            model.addAttribute("post", post);
            model.addAttribute("title", "Single post");
            return "post_single";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("create")
    public String displayPostForm(Model model, Integer userId){
        model.addAttribute("title", "Create Post");
        model.addAttribute(new Post());
        model.addAttribute("user", userRepository.findById(userId));
        return "post_form";
    }

    @PostMapping("create")
    public String processPost(@RequestParam String title, @RequestParam String text, @RequestParam Integer userId, @ModelAttribute @Valid Post post, Errors errors, Model model){

        Optional<User> result = userRepository.findById(userId) ;
        User user = result.get();

        if(errors.hasErrors()){
            model.addAttribute("title", "Create Post");
            model.addAttribute(post);
            return "post_form";
        }
        post = new Post(title, text, user);
        postRepository.save(post);
        return "redirect:post_list";
    }

    @PostMapping("delete")
    public String renderDeleteEventForm(@RequestParam(required = false) int[] postId){

        if(postId != null){
            for (int id: postId){
                postRepository.deleteById(id);
                //EventData.remove(id);
            }
        }

        return "redirect:";
    }
}
