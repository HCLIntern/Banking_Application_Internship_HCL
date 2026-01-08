package com.digitalbanking.controller;

import com.digitalbanking.dto.AssuranceDTO;
import com.digitalbanking.service.AssuranceService;
import com.digitalbanking.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.ASSURANCE_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AssuranceController {
  private final AssuranceService assuranceService;

  @GetMapping("/my-policies")
  public ResponseEntity<List<AssuranceDTO>> getUserPolicies(@RequestParam Long userId) {
    return ResponseEntity.ok(assuranceService.getUserPolicies(userId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AssuranceDTO> getPolicyById(@PathVariable Long id) {
    return ResponseEntity.ok(assuranceService.getPolicyById(id));
  }

  @PostMapping("/apply")
  public ResponseEntity<AssuranceDTO> applyForAssurance(@RequestParam Long userId, @RequestBody AssuranceDTO dto) {
    return ResponseEntity.ok(assuranceService.applyForAssurance(userId, dto));
  }

  @PutMapping("/{id}/renew")
  public ResponseEntity<?> renewPolicy(@PathVariable Long id) {
    assuranceService.renewPolicy(id);
    return ResponseEntity.ok("Policy renewed successfully");
  }
}
