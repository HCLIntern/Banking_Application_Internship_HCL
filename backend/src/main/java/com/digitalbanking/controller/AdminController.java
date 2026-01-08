package com.digitalbanking.controller;

import com.digitalbanking.service.AdminService;
import com.digitalbanking.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.ADMIN_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {
  private final AdminService adminService;

  @GetMapping("/dashboard")
  public ResponseEntity<?> getDashboard() {
    return ResponseEntity.ok(adminService.getDashboardStats());
  }

  @GetMapping("/users")
  public ResponseEntity<?> getAllUsers() {
    return ResponseEntity.ok(adminService.getAllUsers());
  }

  @PutMapping("/users/{id}/disable")
  public ResponseEntity<?> disableUser(@PathVariable Long id) {
    adminService.disableUser(id);
    return ResponseEntity.ok("User disabled");
  }

  @PutMapping("/users/{id}/enable")
  public ResponseEntity<?> enableUser(@PathVariable Long id) {
    adminService.enableUser(id);
    return ResponseEntity.ok("User enabled");
  }

  @PostMapping("/reports")
  public ResponseEntity<?> generateReport(@RequestParam String reportType) {
    return ResponseEntity.ok(adminService.generateReport(reportType));
  }
}
