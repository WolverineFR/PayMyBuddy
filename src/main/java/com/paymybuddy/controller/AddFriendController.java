package com.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.UserService;

import org.springframework.ui.Model;

@Controller
public class AddFriendController {
	
	private static final Logger logger = LogManager.getLogger(AddFriendController.class);

	@Autowired
	private UserService userService;

	@GetMapping("/user/add-friend")
	public String showAddFriendPage() {
		return "add-friend";
	}

	@PostMapping("/user/add-friend")
	public String addFriendMethod(@RequestParam("email") String friendEmail, Authentication auth, RedirectAttributes redirectAttributes) {
		String currentUserEmail = auth.getName();
		DBUser currentUser = userService.getUserByEmail(currentUserEmail);
		DBUser friend = userService.getUserByEmail(friendEmail);

		if (friend == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Cet utilisateur n'existe pas.");
			logger.warn("L'utilisateur {} n'existe pas", friendEmail);
		} else if (friend.getId() == currentUser.getId()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Vous ne pouvez pas vous ajouter vous-même.");
			logger.warn("Impossible de s'ajouter sois même en ami");
		} else if (currentUser.getFriends().contains(friend)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Cet utilisateur est déjà votre ami.");
			logger.warn("L'utilisateur {} est déjà dans votre liste d'ami", friend.getEmail());
		} else {
			currentUser.getFriends().add(friend);
			userService.saveUser(currentUser);
			redirectAttributes.addFlashAttribute("successMessage", "Ami ajouté avec succès !");
			logger.info("{} a été ajouté avec succès dans la liste d'ami de {}", friend.getEmail(), currentUser.getEmail());
		}

		return "redirect:/user/add-friend";
	}
	
	@GetMapping("/user/add-friend/")
	public RedirectView redirectAddFriendWithSlash() {
	    return new RedirectView("/user/add-friend");
	}

}
