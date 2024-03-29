package community.community.controller;

import community.community.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {

    @Autowired
    private BookService bookservice;

    @GetMapping("/books")
    public String getBookList(Model model){
        model.addAttribute("bookList", bookservice.getBookList());
        return "book";
    }
}
