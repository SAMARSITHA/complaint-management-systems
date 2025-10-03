package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Complaint;
import com.example.demo.service.ComplaintService;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("complaint", new Complaint());
        return "index";
    }

    @PostMapping("/submit")
    public String submitComplaint(@ModelAttribute Complaint complaint, Model model) {
        complaintService.saveComplaint(complaint);

        List<Complaint> todaysComplaints = complaintService.getTodaysComplaints();

        // Group by normalized description (trimmed, lowercase)
        Map<String, Long> summary = todaysComplaints.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getDescription().trim().toLowerCase(),
                        Collectors.counting()));

        // Keep original formatting for display
        Map<String, Long> displaySummary = summary.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> todaysComplaints.stream()
                                .filter(c -> c.getDescription().trim().equalsIgnoreCase(e.getKey()))
                                .findFirst()
                                .get()
                                .getDescription(), // original formatting
                        Map.Entry::getValue));

        model.addAttribute("summary", displaySummary);

        return "summary";
    }
}
