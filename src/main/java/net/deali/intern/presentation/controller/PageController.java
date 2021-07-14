package net.deali.intern.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.deali.intern.application.CreativeService;
import net.deali.intern.application.PageService;
import net.deali.intern.domain.Creative;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class PageController {
    private final PageService pageService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/create")
    public String create() {
        return "create";
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView();
        Creative creative = pageService.detail(id);
        mv.setViewName("detail");
        mv.addObject("creative", creative);
        mv.addObject("creativeImage", creative.getCreativeImages().get(0));
        return mv;
    }

    @GetMapping("/advertise")
    public String advertise() {
        return "advertise";
    }
}
