package com.smartmanager.controller;

import com.smartmanager.dao.ContactRepository;
import com.smartmanager.dao.UserRepository;
import com.smartmanager.entities.Contact;
import com.smartmanager.entities.User;
import com.smartmanager.helper.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.web.IWebSession;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
	UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;

    //by the use of modelattibute , this addCommonData runs for all mappings
     @ModelAttribute
    public void addCommonData(Model model, Principal principal){
        String name= principal.getName();
        User user= userRepository.getUserByUserName(name);

        model.addAttribute("user",user);
    }
	@RequestMapping("/index")
   public String dashboard(Model model, Principal principal) {
        model.addAttribute("title","User dashboard");

        return "normal/user_dashboard";
   }
   @GetMapping("/add-contact")
   public String addContact(Model model){
         model.addAttribute("title","Add-Contact");
         model.addAttribute("contact",new Contact());
        return "normal/add_contact";
   }
   @PostMapping("/process-form")
   public String processContact(@ModelAttribute Contact contact,
                                Principal principal,
                                @RequestParam("profileImage") MultipartFile file,
                                HttpSession session){
          try{
              String name=principal.getName();
              User user= this.userRepository.getUserByUserName(name);
              if(file.isEmpty()){
                  System.out.println("File is empty");
                  contact.setImageUrl("contact.png");
              }else{
                  contact.setImageUrl(file.getOriginalFilename());
                  File saveFile= new ClassPathResource("static/profile_pic").getFile();
                  Path path= Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                  Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
              }


              contact.setUser(user);
              System.out.println(contact.getUser());
              user.getContacts().add(contact);
              this.userRepository.save(user);

              System.out.println("added successfully");

              //Success Message
              session.setAttribute("message",new Message("Contact added successfully","alert-success"));

          }catch(Exception e){
              System.out.println("ERRor: "+ e.getMessage());
              e.printStackTrace();
              //Error message
        session.setAttribute("message",new Message("Something went wrong","alert-danger"));

          }

         return "normal/add_contact";
   }
   //per page: 5
    //current page: 0
   @GetMapping("/show-contacts/{page}")
   public String showContact(@PathVariable("page")Integer page,Model model, Principal principal){
         model.addAttribute("title","Show Contacts");
         String userName= principal.getName();
         User user= this.userRepository.getUserByUserName(userName);

       Pageable pageable= PageRequest.of(page,5);

         Page<Contact> contacts= this.contactRepository.findContactsByUser(user.getId(),pageable);
         model.addAttribute("contacts",contacts);
         model.addAttribute("currentPage",page);
         model.addAttribute("totalPages",contacts.getTotalPages());
         return "normal/show_contacts";
   }
   @GetMapping("/show-contacts/user/{cId}")
   public String showContactDetails(@PathVariable("cId") Integer cId, Model model,Principal principal){

       Optional<Contact> contactOptional =this.contactRepository.findById(cId);

       String userName= principal.getName();
       User user= this.userRepository.getUserByUserName(userName);
       Contact contact= contactOptional.get();

       if(user.getId()==contact.getUser().getId()) {
           model.addAttribute("contact", contact);
           model.addAttribute("title",contact.getName());
       }
         return "normal/contact_detail";
   }
   @GetMapping("/delete/{id}")
   public String deleteContact(@PathVariable("id")int cId,Principal principal,HttpSession session) {
       Optional<Contact> contactOptional = this.contactRepository.findById(cId);
       Contact contact = contactOptional.get();

       String userName = principal.getName();
       User user = this.userRepository.getUserByUserName(userName);

       if (user.getId() == contact.getUser().getId()) {
           contact.setUser(null);
           this.contactRepository.delete(contact);
           System.out.println("Deleted. ");
           session.setAttribute("message", new Message("Contact delete successfully..", "success"));
       }

       return "redirect:/user/show-contacts/0";
   }


   //YOUR profile Handler
    @GetMapping("/profile")
    public String profile(Model model){
         model.addAttribute("title","Profile page");
         return "normal/profile";
    }


}
