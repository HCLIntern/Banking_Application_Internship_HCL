package com.digitalbanking.service;

import org.springframework.stereotype.Service;

@Service
public interface AdminService {
  // Dashboard statistics
  Object getDashboardStats();
  
  // User management
  Object getAllUsers();
  void disableUser(Long userId);
  void enableUser(Long userId);
  
  // Reports
  Object generateReport(String reportType);
}
