package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Complaint;
import com.example.demo.repository.ComplaintRepository;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    // Save a new complaint
    public void saveComplaint(Complaint complaint) {
        complaint.setSubmittedAt(LocalDateTime.now());
        complaintRepository.save(complaint);
    }

    // Fetch complaints submitted today
    public List<Complaint> getTodaysComplaints() {
        LocalDate today = LocalDate.now();
        return complaintRepository.findAll().stream()
                .filter(c -> c.getSubmittedAt().toLocalDate().equals(today))
                .toList();
    }

    // Optional: Fetch all complaints (if needed)
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }
}
