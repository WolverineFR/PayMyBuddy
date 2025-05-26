package com.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

import org.springframework.ui.Model;

@Controller
public class AddFriendController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/user/add-friend")
	public String showAddFriendPage() {
		return "add-friend";
	}

	@PostMapping("/user/add-friend")
	public String addFriendMethod(@RequestParam("email") String friendEmail, Authentication auth, Model model) {
		String currentUserEmail = auth.getName();
		DBUser currentUser = userRepository.findByEmail(currentUserEmail);
		DBUser friend = userRepository.findByEmail(friendEmail);

		if (friend == null) {
			model.addAttribute("errorMessage", "Cet utilisateur n'existe pas.");
		} else if (friend.getId() == currentUser.getId()) {
			model.addAttribute("errorMessage", "Vous ne pouvez pas vous ajouter vous-même.");
		} else if (currentUser.getFriends().contains(friend)) {
			model.addAttribute("errorMessage", "Cet utilisateur est déjà votre ami.");
		} else {
			currentUser.getFriends().add(friend);
			userRepository.save(currentUser);
			model.addAttribute("successMessage", "Ami ajouté avec succès !");
		}

		return "add-friend";
	}

}
