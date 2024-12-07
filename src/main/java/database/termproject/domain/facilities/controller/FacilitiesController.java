package database.termproject.domain.facilities.controller;


import database.termproject.domain.facilities.dto.request.FacilitiesRegisterRequest;
import database.termproject.domain.facilities.service.FacilitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/facilities")
@RequiredArgsConstructor
public class FacilitiesController {

    private final FacilitiesService facilitiesService;

    @PostMapping("/register")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER", "ROLE_ANONYMOUS"})
    public ResponseEntity<?> registerFacilities(@RequestBody FacilitiesRegisterRequest facilitiesRegisterRequest) {
        return ResponseEntity.ok(facilitiesService
                .register(facilitiesRegisterRequest)
        );
    }

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> getFacilities() {
        return ResponseEntity.ok(facilitiesService.getFacilities());
    }

    @PutMapping("/edit")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> editFacilities(@RequestBody FacilitiesRegisterRequest facilitiesRegisterRequest) {
        return ResponseEntity.ok(facilitiesService.editFacilities(facilitiesRegisterRequest));
    }


}
