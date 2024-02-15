package com.api.contact;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.utils.ApiConstant;
import com.api.utils.ApiResponse;
import com.api.utils.PaginatedData;

@RestController
@RequestMapping("/contact")
public class ContactController {
        @Autowired
        private ContactService contactService;

        @GetMapping()
        public ResponseEntity<Object> getContactList(
                        @RequestParam(name = "limit", defaultValue = "10") String limit,
                        @RequestParam(name = "p", defaultValue = "1") String page,
                        @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
                        @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
                        @RequestParam(name = "q", defaultValue = "") String keyword) {
                Page<Contact> contactPage = this.contactService.paginate(Integer.parseInt(limit),
                                Integer.parseInt(page),
                                sortBy, sortType, keyword);
                return ResponseEntity
                                .ok(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                .data(PaginatedData.<Contact>builder()
                                                                .count(contactPage.getTotalElements())
                                                                .rows(contactPage.getContent())
                                                                .totalPages(contactPage.getTotalPages()).build())
                                                .build());
        }

        @PostMapping()
        public ResponseEntity<Object> createContact(@RequestBody Contact contact) {
                System.out.println("CReate");
                Optional<Contact> contactOptional = this.contactService.create(contact);

                if (contactOptional.isPresent()) {
                        return ResponseEntity.status(ApiConstant.STATUS_201)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                        .data(contactOptional.get()).build());
                }

                return ResponseEntity.status(ApiConstant.STATUS_500)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Something went wrong").build());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Object> deleteContact(@PathVariable("id") Long id) {
                Optional<Contact> contactOptional = this.contactService.findById(id);

                if (contactOptional.isPresent()) {
                        boolean isDeleted = this.contactService.delete(id);
                        if (isDeleted) {
                                return ResponseEntity.status(ApiConstant.STATUS_201)
                                                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                                .data(isDeleted)
                                                                .build());
                        }
                }

                return ResponseEntity.status(ApiConstant.STATUS_500)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Something went wrong").build());
        }

}
