package com.microdonation.controller;

import com.microdonation.dto.CampaignDTO;
import com.microdonation.dto.DonationDTO;
import com.microdonation.dto.UserDTO;
import com.microdonation.service.CampaignService;
import com.microdonation.service.DonationService;
import com.microdonation.service.ReportService;
import com.microdonation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserService userService;
    private final CampaignService campaignService;
    private final DonationService donationService;
    private final ReportService reportService;

    @GetMapping("/")
    public String home(Model model) {
        List<CampaignDTO> activeCampaigns = campaignService.getActiveCampaigns();
        model.addAttribute("campaigns", activeCampaigns != null ? activeCampaigns : Collections.emptyList());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDTO userDTO,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userService.registerUser(userDTO);
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/campaigns")
    public String listCampaigns(Model model) {
        List<CampaignDTO> campaigns = campaignService.getAllCampaigns();
        model.addAttribute("campaigns", campaigns != null ? campaigns : Collections.emptyList());
        return "campaigns";
    }

    @GetMapping("/campaigns/{id}")
    public String viewCampaign(@PathVariable Long id, Model model) {
        try {
            CampaignDTO campaign = campaignService.getCampaignById(id);
            model.addAttribute("campaign", campaign);
            model.addAttribute("donations", donationService.getDonationsByCampaign(id));
            model.addAttribute("donation", new DonationDTO());
            return "campaign-details";
        } catch (Exception e) {
            return "redirect:/campaigns";
        }
    }

    @GetMapping("/admin/campaigns/new")
    public String showCreateCampaignForm(Model model, Authentication authentication) {
        model.addAttribute("campaign", new CampaignDTO());

        String email = authentication.getName();
        UserDTO user = userService.getUserByEmail(email);

        List<CampaignDTO> myCampaigns = campaignService.getCampaignsByUser(user.getUserId());
        List<CampaignDTO> allCampaigns = campaignService.getAllCampaigns();

        model.addAttribute("campaigns", myCampaigns != null ? myCampaigns : Collections.emptyList());
        model.addAttribute("allCampaigns", allCampaigns != null ? allCampaigns : Collections.emptyList());

        return "create-campaign";
    }

    @PostMapping("/admin/campaigns/new")
    public String createCampaign(@Valid @ModelAttribute("campaign") CampaignDTO campaignDTO,
                                 BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            return "create-campaign";
        }
        try {
            String email = authentication.getName();
            UserDTO user = userService.getUserByEmail(email);
            campaignDTO.setCreatedBy(user.getUserId());
            campaignService.createCampaign(campaignDTO);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "create-campaign";
        }
    }

    @PostMapping("/donations/make")
    public String makeDonation(@ModelAttribute DonationDTO donationDTO,
                               Authentication authentication, Model model) {
        try {
            String email = authentication.getName();
            UserDTO user = userService.getUserByEmail(email);
            donationDTO.setUserId(user.getUserId());
            donationService.makeDonation(donationDTO);
            return "redirect:/campaigns/" + donationDTO.getCampaignId() + "?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/campaigns/" + donationDTO.getCampaignId() + "?error";
        }
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        UserDTO user = userService.getUserByEmail(email);

        model.addAttribute("user", user);
        model.addAttribute("donations", donationService.getDonationsByUser(user.getUserId()));
        model.addAttribute("totalDonated", donationService.getTotalDonationsByUser(user.getUserId()));

        return "user-dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        UserDTO user = userService.getUserByEmail(email);

        List<CampaignDTO> myCampaigns = campaignService.getCampaignsByUser(user.getUserId());
        List<CampaignDTO> allCampaigns = campaignService.getAllCampaigns();
        List<UserDTO> allUsers = userService.getAllUsers();

        model.addAttribute("campaigns", myCampaigns != null ? myCampaigns : Collections.emptyList());
        model.addAttribute("allCampaigns", allCampaigns != null ? allCampaigns : Collections.emptyList());
        model.addAttribute("allUsers", allUsers != null ? allUsers : Collections.emptyList());

        return "admin-dashboard";
    }

    @GetMapping("/reports/campaign/{id}")
    public String campaignReport(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("report", reportService.getCampaignReport(id));
            return "campaign-report";
        } catch (Exception e) {
            return "redirect:/campaigns";
        }
    }
}
